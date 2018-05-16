package exppack

import scala.concurrent.{ExecutionContext, Future}

class UserController(implicit repository: UserRepository, ec: ExecutionContext) extends Controller3[String, String, Request, Request] {
  override def apply(login: String, pass: String, request: Request): Future[Request] = repository.userAuth(login, pass).map(request.withUser)
}

class AddUserController(implicit repository: UserRepository, ec: ExecutionContext) extends Controller[Request, User] {
/*
  override def apply(request: Request): Future[Boolean] = request match {
    case req: Request.AddUser => repository.checkAndSave(req.name, req.pass).map(_ => true).recover{
        case _ : UserRegistrationException => false}
  }
*/
  override def apply(request: Request): Future[User] = request match {
    case req: Request.AddUser => repository.checkAndSave(req.name, req.pass)
  }
}
