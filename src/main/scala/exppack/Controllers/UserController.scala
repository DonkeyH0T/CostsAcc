package exppack.Controllers

import exppack.repository.UserRepository
import exppack.domain.{Request, User, UserRequest}

import scala.concurrent.{ExecutionContext, Future}

class UserController(implicit repository: UserRepository, ec: ExecutionContext) extends Controller2[User, Request, Request] {
  override def apply(user: User, request: Request): Future[Request] = repository.userAuth(user).map(request.withUser)
}

class AddUserController(implicit repository: UserRepository, ec: ExecutionContext) extends Controller[UserRequest, User] {

  override def apply(request: UserRequest): Future[User] = request match {
    case UserRequest.AddUser(user) => repository.put(user)
  }
}
