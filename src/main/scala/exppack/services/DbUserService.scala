package exppack.services

import slick.jdbc.{JdbcBackend, JdbcProfile}
import exppack.db.models.UserModel
import exppack.domain.User
import exppack.repository.{UserAuthenticationException, UserRegistrationException}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait DbUserService {
  def getAll(): Future[Seq[User]]

  def create(user: User): Future[User]

  //def signIn(user: User): Future[User]
}

class DbUserServiceImpl(val profile: JdbcProfile,db: JdbcBackend.Database) extends DbUserService with UserModel {

  import profile.api._
  //import slick.jdbc.MySQLProfile.api._

  override def getAll(): Future[Seq[User]] = db.run {
    users.result
  }

  /*
    override def create(user: User): Future[User] =  db.run {
      users.filter(_.name === user.name).result.headOption.flatMap {
        case None => (users returning users.map(_.id) += user).map(id => user.copy(id = id))
        case _ => DBIO.failed(new UserRegistrationException("user with provided name already exists"))
      }
    }
  */
  /*
  override def create(user: User): Future[User] =
    for {
      opt <- db.run {
        users.filter(_.name === user.name).result.headOption}
      s <- db.run{
       opt match {
       case None => (users returning users.map(_.id) += user).map(id => user.copy(id = id))
       case _ => DBIO.failed(new UserRegistrationException("user with provided name already exists"))
       }
    }
} yield s

*//*
  override def create(user: User): Future[User] =  db.run {

    users.filter(_.name === user.name.bind).result.headOption.flatMap {

      case None => users += user

      case _ => DBIO.failed(new UserRegistrationException("user with provided name already exists"))

    }.map(_ => user)

  }
*/
  override def create(user: User): Future[User] = { db.run( users += user).map(_ => user)}






/*
override def signIn (user: User): Future[User] = db.run {
  users.filter (x => x.name === user.name && x.pass === user.pass).result.headOption.map {
  case Some (a) => DBIO.successful (a)
  case None => DBIO.failed (new UserAuthenticationException ("invalid username or password") )
}
}
*/

}


