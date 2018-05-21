package exppack.Controllers

import exppack.repository.UserRepository
import exppack.domain.{Request, User, UserRequest}

import scala.concurrent.{ExecutionContext, Future}

class UserController(implicit repository: UserRepository, ec: ExecutionContext) extends Controller2[User, Request, Request] {
  override def apply(user: User, request: Request): Future[Request] = repository.userAuth(user).map(request.withUser)
}

class AddUserController(implicit repository: UserRepository, ec: ExecutionContext) extends Controller[UserRequest.AddUser, User] {
  override def apply(request: UserRequest.AddUser): Future[User] = repository.put(request.user)
}

class ExistsUserController(implicit repository: UserRepository, ec: ExecutionContext) extends Controller[UserRequest.Exists, Boolean] {

  override def apply(request: UserRequest.Exists): Future[Boolean] = repository.userAuth(request.user).map(_.id.nonEmpty)
}