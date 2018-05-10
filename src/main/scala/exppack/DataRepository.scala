package exppack

import java.time.{LocalDate, Year}

import scala.concurrent.{ExecutionContext, Future}
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger
import java.time.Period
import scala.collection.JavaConverters._


trait DataService {
  def nextDataId: Int

  def isWithinRange(dateFrom: LocalDate, dateTo: LocalDate, date: LocalDate): Boolean
}

trait DataServiceImpl extends DataService {
  val idData: AtomicInteger = new AtomicInteger(0)

  def nextDataId: Int = idData.incrementAndGet

  override def isWithinRange(dateFrom: LocalDate, dateTo: LocalDate, date: LocalDate): Boolean = {
    !(date.isBefore(dateFrom) || date.isAfter(dateTo))
  }
}

trait DataRepositoryUtils {
  this: Repository[Int, Data] =>
  def sumByCategory(dateFrom: LocalDate, dateTo: LocalDate, category: String, user: User): Future[BigDecimal]

  def sumByShop(dateFrom: LocalDate, dateTo: LocalDate, shop: String, user: User): Future[BigDecimal]

  def statByDate(dateFrom: LocalDate, dateTo: LocalDate, user: User): Future[Seq[Sample]]

  def getReminds(user: User): Future[Seq[RegSample]]
}

trait DataRepository extends Repository[Int, Data] with DataRepositoryUtils

final class DataRepositoryClass(implicit ec: ExecutionContext) extends DataRepository with DataServiceImpl {

  private[this] val storage = new ConcurrentHashMap[Int, Data]()

  override def put(item: Data): Future[Unit] = Future {
    val itemWithId = item.copy(id = Some(nextDataId))
    itemWithId.id match {
      case Some(i) => storage.put(i, itemWithId)
    }
  }

  override def all(): Future[Seq[Data]] = Future {
    storage.values().asScala.toSeq
  }

  override def sumByCategory(dateFrom: LocalDate, dateTo: LocalDate, category: String, user: User): Future[BigDecimal] = {
    all().map(x => x.filter(y => isWithinRange(dateFrom, dateTo, y.date) && y.category == Some(category) &&
      y.userId == Some(user.id)).map(x => BigDecimal(x.cost)).sum)
  }

  override def sumByShop(dateFrom: LocalDate, dateTo: LocalDate, shop: String, user: User): Future[BigDecimal] = {
    all().map(x => x.filter(y => isWithinRange(dateFrom, dateTo, y.date) && y.shop == Some(shop) &&
      y.userId == Some(user.id)).map(x => BigDecimal(x.cost)).sum)
  }

  override def statByDate(dateFrom: LocalDate, dateTo: LocalDate, user: User): Future[Seq[Sample]] = {
    all().map {
      _
        .filter(y => isWithinRange(dateFrom, dateTo, y.date) && y.userId.contains(user.id))
        .sortBy(_.date)
        .map(x => Sample((x.date.getMonth, Year.of(x.date.getYear)), x.category, BigDecimal(x.cost)))
        .groupBy(x => (x.monthYear, x.category))
        .map { case ((monthYear, category), samples) => Sample(monthYear, category, samples.map(_.sum).sum) }
        .toSeq
    }
  }

  override def getReminds(user: User): Future[Seq[RegSample]] = {
    all().map {
      _.filter(y => y.userId.contains(user.id) && y.nextPayment != None)
        .sortBy(_.nextPayment)
        .groupBy(_.category)
        .map { case (Some(category), data) => {
          val last = data.last
          last.nextPayment match {
            case Some(x) if Period.between(LocalDate.now(), x).getDays() < 5 => RegSample(x, category, last.cost)
          }
        }
        }.toSeq
    }
  }

}


