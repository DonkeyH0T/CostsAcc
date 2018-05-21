package exppack.repository
import exppack.domain.{Data, RegSample, Sample, User}
import exppack.services.DbDataService
import org.joda.time.DateTime

import scala.concurrent.Future



class DbDataRepository(implicit dbService: DbDataService) extends DataRepository {

  override def all(): Future[Seq[Data]] = dbService.getAll()

  override def put(item: Data): Future[Data] = dbService.insert(item)

  override def getRemind(user: User): Future[Seq[RegSample]] =   dbService.notify(user)

  override def statByDate(dateFrom: DateTime, dateTo: DateTime, user: User): Future[Seq[Sample]] = {
    dbService.filterByDate(dateFrom, dateTo, user)
  }

  override def sumByCategory(dateFrom: DateTime, dateTo: DateTime, category: String, user: User): Future[BigDecimal] = {
    dbService.filterByCat(dateFrom, dateTo, category, user)
  }

  override def sumByShop(dateFrom: DateTime, dateTo: DateTime, shop: String, user: User): Future[BigDecimal] = {
    dbService.filterByShop(dateFrom, dateTo, shop, user)
  }
}



