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
  val userService = new DbUserServiceImpl(profile,db)
val dataService = new DbDataServiceImpl(profile,db)
  //val f = dataService.getAll()
  val c = userService.signIn(User("Misha","123"))
  val p = dataService.insert(Data(new DateTime("2018-01-15T00:00:00.000+03:00"), 100, Some("internet"), None, Some(new DateTime("2018-03-15T00:00:00.000+03:00")),None,Some(3)))
 // val p1 = dataService.filterByCat(new DateTime("2018-02-15T00:00:00.000+03:00"),new DateTime("2018-06-15T00:00:00.000+03:00"),"food",User("Misha","123",Some(3)))
 // val p2 = dataService.filterByDate(new DateTime("2018-02-15T00:00:00.000+03:00"),new DateTime("2018-06-15T00:00:00.000+03:00"),User("Misha","123",Some(3)))
  val p3 = dataService.notify(User("Misha","123",Some(3)))
  Await.ready(p3, Duration.Inf)
}
