package exppack.repository

import exppack.domain._
import exppack.services.DbUserService
import scala.concurrent.Future

class DbUserRepository(dbService: DbUserService) extends UserRepository{


  override def all(): Future[Seq[User]] = ???

  override def put(item: User): Future[User] = ???

  override def userAuth(user: User): Future[User] = ???

}
