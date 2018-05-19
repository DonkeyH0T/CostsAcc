package exppack

import exppack.Controllers.{AddUserController, RemindController, UserController}
import exppack.repository.MemoryUserRepository
import exppack.domain.{Data, RegSample, User, UserRequest}
import exppack.services.{DbDataServiceImpl, DbUserServiceImpl}
import org.joda.time._
import slick.jdbc.{JdbcBackend, JdbcProfile, MySQLProfile}
import slick.jdbc.JdbcBackend.Database

import scala.concurrent.Future
import scala.util.{Failure, Success}
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global

object MainObj extends App{

  val profile = MySQLProfile
  val db = Database.forConfig("db")
  val dataService = new DbUserServiceImpl(profile,db)
val serv = new DbDataServiceImpl(profile,db)
  //val f = dataService.getAll()
  val c = dataService.signIn(User("Misha","123"))
  val p = serv.getAll()
  Await.ready(c, Duration.Inf)
}
