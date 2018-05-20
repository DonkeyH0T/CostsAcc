package exppack.repository

import exppack.domain.User
import exppack.services.DbUserService
import scala.concurrent.Future

class DbUserRepository(implicit dbService: DbUserService) extends UserRepository{


  override def all(): Future[Seq[User]] = dbService.getAll()

  override def put(item: User): Future[User] = dbService.create(item)

  override def userAuth(user: User): Future[User] = dbService.signIn(user)

}
