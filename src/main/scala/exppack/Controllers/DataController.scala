package exppack.Controllers

import exppack.repository.DataRepository
import exppack.domain.{Request, _}

import exppack.domain.{RegSample, Sample}

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class AddDataController(implicit repository: DataRepository) extends Controller[Request, Boolean] {

  override def apply(request: Request): Future[Boolean] = request match {
    case Request.AddExpense(item, user) => repository.put(item.withUser(user)).map(_ => true)
      .recover {
        case _: Exception => false
      }
  }
}

class GetStatController(implicit repository: DataRepository) extends Controller[Request, BigDecimal] {

  override def apply(request: Request): Future[BigDecimal] = request match {
    case Request.WithDateShop(dateFrom, dateTo, shop, user) => user match {
      case Some(u) =>
        repository.sumByShop(dateFrom, dateTo, shop, u)
    }

    case Request.WithCategory(dateFrom, dateTo, category, user) => user match {
      case Some(u) =>
        repository.sumByCategory(dateFrom, dateTo, category, u)
    }
  }
}

class DetailedStatController(implicit repository: DataRepository) extends Controller[Request, Seq[Sample]] {
  override def apply(request: Request): Future[Seq[Sample]] = request match {
    case Request.WithDate(dateFrom, dateTo, user) => user match {
      case Some(u) =>
        repository.statByDate(dateFrom,dateTo,u)
    }
  }
}

class RemindController(implicit repository: DataRepository) extends Controller[Request, Seq[RegSample]] {

  override def apply(request: Request): Future[Seq[RegSample]] = request match {
    case Request.WithDate(dateFrom, dateTo, user) => user match {
      case Some(u) =>
        repository.getReminds(u)
    }
  }
  
}