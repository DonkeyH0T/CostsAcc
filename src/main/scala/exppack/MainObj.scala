package exppack

import exppack.Controllers._
import exppack.domain.Request._
import exppack.domain.{Data, User}
import exppack.repository.{DbDataRepository, DbUserRepository}
import exppack.services.{DbDataServiceImpl, DbUserServiceImpl}
import org.joda.time._
import slick.jdbc.MySQLProfile
import slick.jdbc.JdbcBackend.Database

import scala.concurrent.{Await, ExecutionContext}
import scala.concurrent.duration.Duration


object MainObj extends App {

  val profile = MySQLProfile
  val db = Database.forConfig("db")
  implicit val userService = new DbUserServiceImpl(profile, db)
  implicit val dataService = new DbDataServiceImpl(profile, db)
  implicit val dbdr = new DbDataRepository
  implicit val dbur = new DbUserRepository
  implicit val ec = ExecutionContext.global

  val uc = new UserController
  val dc = new AddDataController
  val gsc = new GetStatController
  val dsc = new DetailedStatController
  val rc = new RemindController
  /*
  val k = uc(User("John Doe", "qwerty", None), AddExpense(Data(new DateTime("2018-02-15T00:00:00.000+03:00"), 100,
    Some("food"), Some("Prisma"), None, None, None), None))
    .flatMap(dc)
  */
  /*
    val k = uc(User("John Doe", "qwerty", None),WithDateShop(new DateTime("2018-01-15T00:00:00.000+03:00"),
      new DateTime("2018-06-15T00:00:00.000+03:00"),"Prisma",None))
      .flatMap(gsc)
  */
  /*
    val k = for {
      withUser <- uc(User("John Doe", "qwerty", None), WithCategory(new DateTime("2018-01-15T00:00:00.000+03:00"),
        new DateTime("2018-06-15T00:00:00.000+03:00"), "food", None))
      result <- gsc(withUser)
    } yield result
  */
  /*
  val k = uc(User("John Doe", "qwerty", None), WithDate(new DateTime("2018-01-15T00:00:00.000+03:00"),
    new DateTime("2018-06-15T00:00:00.000+03:00"),None))
    .flatMap(dsc)
  */

  val k = uc(User("John Doe", "qwerty", None),Remind(None))
    .flatMap(rc)

  Await.ready(k, Duration.Inf)
}
