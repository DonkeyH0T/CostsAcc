package exppack.services

import exppack.db.models.CostsModel
import exppack.domain.{Data, RegSample, Sample, User}
import org.joda.time._
import slick.jdbc.{JdbcBackend, JdbcProfile}

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

trait DbDataService {

  def getAll(): Future[Seq[Data]]

  def insert(item: Data): Future[Data]

  def notify(user: User): Future[Seq[RegSample]]

  def filterByDate(dateFrom: DateTime, dateTo: DateTime, user: User): Future[Seq[Sample]]

  def filterByCat(dateFrom: DateTime, dateTo: DateTime, category: String, user: User): Future[BigDecimal]

  def filterByShop(dateFrom: DateTime, dateTo: DateTime, shop: String, user: User): Future[BigDecimal]

}


class DbDataServiceImpl(val profile: JdbcProfile, db: JdbcBackend.Database) extends DbDataService with CostsModel {

  import profile.api._

  override def getAll(): Future[Seq[Data]] = db.run {
    costs.result
  }

  override def insert(item: Data): Future[Data] = db.run {
    (costs returning costs.map(_.id) += item).map(id => item.copy(id = id))
  }

  override def filterByDate(dateFrom: DateTime, dateTo: DateTime, user: User): Future[Seq[Sample]] = {
    db.run {
      costs.filter(x => x.userId === user.id && !(x.date < dateFrom || x.date > dateTo)).sortBy(_.date).result
    }
      .map(y => y.map(x => Sample(new YearMonth(x.date.getYear, x.date.getMonthOfYear).toString, x.category, x.cost))
        .groupBy(x => (x.yearMonth, x.category))
        .map { case ((monthYear, category), samples) => Sample(monthYear, category, samples.map(_.sum).sum) }
        .toSeq)
    //т.к. появились новый тип  Year month с последующем вызовом метода groupBy, не нашел способа сделать это в одном
    //sql запросе
  }

  override def filterByCat(dateFrom: DateTime, dateTo: DateTime, category: String, user: User): Future[BigDecimal] =
    db.run {
      costs.filter(x => x.category === Option(category) && x.userId === user.id && !(x.date < dateFrom || x.date > dateTo))
        .map(_.cost).sum.result.map(_.getOrElse(0))
    }

  override def filterByShop(dateFrom: DateTime, dateTo: DateTime, shop: String, user: User): Future[BigDecimal] = {
    db.run {
      costs.filter(x => x.shop === Option(shop) && x.userId === user.id && !(x.date < dateFrom || x.date > dateTo))
        .map(_.cost).sum.result.map(_.getOrElse(0))
    }
  }

  override def notify(user: User): Future[Seq[RegSample]] = {
    val cd = LocalDate.now().toDateTimeAtStartOfDay().plusDays(5)
    db.run {
      costs.filter(x => x.userId === user.id && x.nextPayment.isDefined && x.category.isDefined)
        .filter(_.nextPayment.getOrElse(new DateTime) < cd)
        .sortBy(_.nextPayment).result
    }
      .map { x =>
        x.groupBy(_.category)
          .map { case (category, data: Seq[Data]) => data.last }.toSeq
          .map { y: Data => RegSample(y.nextPayment.get, y.category.get, y.cost) }
      }
  }


}

