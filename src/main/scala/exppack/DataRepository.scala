package exppack

import java.time.LocalDate

import scala.concurrent.{ExecutionContext, Future}
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

import scala.collection.JavaConverters._




trait DataService {
  def nextDataId: Int
  def isWithinRange(dateFrom: LocalDate,dateTo: LocalDate, date: LocalDate): Boolean
}

trait DataServiceImpl extends DataService {
  val idData: AtomicInteger = new AtomicInteger(0)
  def nextDataId: Int = idData.incrementAndGet
  override def isWithinRange(dateFrom: LocalDate,dateTo: LocalDate, date: LocalDate): Boolean = {
    !(date.isBefore(dateFrom) || date.isAfter(dateTo))
  }
}

trait DataRepositoryUtils {
  this : Repository[Int, Data] =>
  def SumByCategory(dateFrom: LocalDate, dateTo: LocalDate, category: String, user: User): Future[BigDecimal]
  def SumByShop(dateFrom: LocalDate, dateTo: LocalDate, shop: String, user: User): Future[BigDecimal]
  def StatByDate(dateFrom: LocalDate, dateTo: LocalDate, user: User): Future[Seq[Sample]]
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

  override def SumByCategory(dateFrom: LocalDate, dateTo: LocalDate, category: String, user: User): Future[BigDecimal] ={
    all().map(x => x.filter(y => isWithinRange(dateFrom,dateTo, y.date) && y.category==Some(category) &&
      y.userId == Some(user.id)).map(x => BigDecimal(x.cost)).sum)
  }

  override def SumByShop(dateFrom: LocalDate, dateTo: LocalDate, shop: String, user: User): Future[BigDecimal] ={
    all().map(x => x.filter(y => isWithinRange(dateFrom,dateTo, y.date) && y.shop==Some(shop) &&
      y.userId == Some(user.id)).map(x => BigDecimal(x.cost)).sum)
  }

  override def StatByDate(dateFrom: LocalDate, dateTo: LocalDate, user: User): Future[Seq[Sample]] ={
    ???
  //  all().map(x => x.filter(y => isWithinRange(dateFrom,dateTo, y.date) &&
  //    y.userId == Some(user.id)).map(x => Sample(x.date.getMonth,x.category,BigDecimal(0))))
  }

}


