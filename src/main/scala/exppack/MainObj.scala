package exppack

import exppack.Controllers.{AddUserController, RemindController, UserController}
import exppack.repository.MemoryUserRepository
import exppack.domain.{Data, RegSample, User, UserRequest}
import exppack.services.DbUserServiceImpl
import org.joda.time._
import slick.jdbc.{JdbcBackend, JdbcProfile, MySQLProfile}
import slick.jdbc.JdbcBackend.Database
import scala.concurrent.Future
import scala.util.{Success, Failure}
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global

object MainObj extends App{

  val profile = MySQLProfile
  val db = Database.forConfig("db")
  val dataService = new DbUserServiceImpl(profile,db)

  val f = dataService.getAll()

  Await.ready(f, Duration.Inf)
}
