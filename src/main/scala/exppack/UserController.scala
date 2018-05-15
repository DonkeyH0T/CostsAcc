package exppack

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class UserController(implicit repository: UserRepository) extends Controller3[String, String, Request, Request] {
  override def apply(login: String, pass: String, request: Request): Future[Request] =
    repository.userAuth(login, pass).map(request.withUser)
}

class AddUserController(implicit repository: UserRepository) extends Controller[Request, Boolean] {

  override def apply(request: Request): Future[Boolean] = request match {
    case req: Request.AddUser => repository.checkAndSave(req.name, req.pass).map(_ => true).recover{
        case _ : UserRegistrationException => false}
  }
}
