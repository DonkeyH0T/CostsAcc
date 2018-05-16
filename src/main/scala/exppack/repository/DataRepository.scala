package exppack.repository

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

import exppack.domain._
import org.joda.time._

import scala.collection.JavaConverters._
import scala.concurrent.{ExecutionContext, Future}

object DataService {

  val idData: AtomicInteger = new AtomicInteger(0)

  def nextDataId: Int = idData.incrementAndGet

  def isWithinRange(dateFrom: DateTime, dateTo: DateTime, date: DateTime): Boolean = {
    !(date.isBefore(dateFrom) || date.isAfter(dateTo))
  }

  implicit val orderingLocalDate: Ordering[DateTime] = Ordering.by(d => (d.getYear, d.getDayOfYear, d.getSecondOfDay))
}

trait DataRepositoryUtils {
  this: Repository[Int, Data] =>
  def sumByCategory(dateFrom: DateTime, dateTo: DateTime, category: String, user: User): Future[BigDecimal]

  def sumByShop(dateFrom: DateTime, dateTo: DateTime, shop: String, user: User): Future[BigDecimal]

  def statByDate(dateFrom: DateTime, dateTo: DateTime, user: User): Future[Seq[Sample]]

  def getRemind(user: User): Future[Seq[RegSample]]
}

trait DataRepository extends Repository[Int, Data] with DataRepositoryUtils

final class MemoryDataRepository(implicit ec: ExecutionContext) extends DataRepository {

  import DataService._

  private[this] val storage = new ConcurrentHashMap[Int, Data]()

  override def put(item: Data): Future[Data] = Future {
    val dataId = nextDataId
    val itemWithId = item.copy(id = Some(dataId))
    storage.put(dataId, itemWithId)
    itemWithId
  }

  override def all(): Future[Seq[Data]] = Future {
    storage.values().asScala.toSeq
  }

  override def sumByCategory(dateFrom: DateTime, dateTo: DateTime, category: String, user: User): Future[BigDecimal] = {
    all().map(x => x.filter(y => isWithinRange(dateFrom, dateTo, y.date) && y.category.contains(category) &&
      y.userId==user.id).map(_.cost).sum)
  }

  override def sumByShop(dateFrom: DateTime, dateTo: DateTime, shop: String, user: User): Future[BigDecimal] = {
    all().map(x => x.filter(y => isWithinRange(dateFrom, dateTo, y.date) && y.shop.contains(shop) &&
      y.userId==user.id).map(_.cost).sum)
  }

  override def statByDate(dateFrom: DateTime, dateTo: DateTime, user: User): Future[Seq[Sample]] = {
    all().map {
      _.filter(y => isWithinRange(dateFrom, dateTo, y.date) && y.userId==user.id)
        .sortBy(_.date)
        .map(x => Sample(new YearMonth(x.date.getYear, x.date.getMonthOfYear).toString, x.category, x.cost))
        .groupBy(x => (x.yearMonth, x.category))
        .map { case ((monthYear, category), samples) => Sample(monthYear, category, samples.map(_.sum).sum) }
        .toSeq
    }
  }

  override def getRemind(user: User): Future[Seq[RegSample]] = {
    all().map {
      _.filter(y => y.userId==user.id && y.nextPayment.isDefined && y.category.isDefined)
        .sortBy(_.nextPayment)
        .groupBy(_.category)
        .filter { case (Some(category), data) =>
          val last = data.last
          last.nextPayment match {
            case Some(x)
              if Days.daysBetween(x.toLocalDate, LocalDate.now()).getDays >= -5 => true
            case _ => false
          }
        }.map { case (Some(category), data) =>
        val last = data.last
        last.nextPayment match {
          case Some(x)
            if Days.daysBetween(x.toLocalDate, LocalDate.now()).getDays >= -5 =>
            RegSample(x, category, last.cost)
        }
      }.toSeq
    }
  }

}


