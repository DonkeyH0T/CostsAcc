package exppack.Controllers

import exppack.repository.DataRepository
import exppack.domain.{Request, _}

import scala.concurrent.{ExecutionContext, Future}
class UserNotFoundException(msg: String) extends Exception(msg)

class AddDataController(implicit repository: DataRepository, ec: ExecutionContext) extends Controller[Request, Boolean] {

  def total(request: Request.AddExpense): Future[Boolean] = request.currentUser match {
    case Some(u) => repository.put(request.exp.withUser(u)).map(_ => true).recover {
      case _: Exception => false
    }
    case None => Future.failed(new UserNotFoundException("user is not provided"))
  }
  override def apply(request: Request): Future[Boolean] = request match {
    case r: Request.AddExpense => total(r)
  }
}

class GetStatController(implicit repository: DataRepository) extends Controller[Request, BigDecimal] {

  override def apply(request: Request): Future[BigDecimal] = request match {
    case Request.WithDateShop(dateFrom, dateTo, shop, user) => user match {
      case Some(u) =>
        repository.sumByShop(dateFrom, dateTo, shop, u)
      case None => Future.failed(new UserNotFoundException("user is not provided"))
    }

    case Request.WithCategory(dateFrom, dateTo, category, user) => user match {
      case Some(u) =>
        repository.sumByCategory(dateFrom, dateTo, category, u)
      case None => Future.failed(new UserNotFoundException("user is not provided"))
    }
  }
}

class DetailedStatController(implicit repository: DataRepository) extends Controller[Request, Seq[Sample]] {
  def total(request: Request.WithDate): Future[Seq[Sample]] = request.currentUser match {
    case Some(u) =>
      repository.statByDate(request.dateFrom,request.dateTo, u)
    case None => Future.failed(new UserNotFoundException("user is not provided"))
  }

  override def apply(request: Request): Future[Seq[Sample]] = request match {
    case r: Request.WithDate => total(r)
  }
}

class RemindController(implicit repository: DataRepository) extends Controller[Request, Seq[RegSample]] {

  def total(request: Request.Remind): Future[Seq[RegSample]] = request.currentUser match {
    case Some(u) =>
      repository.getRemind(u)
    case None => Future.failed(new UserNotFoundException("user is not provided"))
  }

  override def apply(request: Request): Future[Seq[RegSample]] = request match {
    case r: Request.Remind => total(r)
  }
}