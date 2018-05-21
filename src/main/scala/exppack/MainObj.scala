package exppack

<<<<<<< HEAD
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import exppack.Controllers._
import exppack.repository.{DataRepository, UserRepository}

import scala.concurrent.{ExecutionContext, Future}
object MainObj extends App {

  implicit val system: ActorSystem = ActorSystem("system")
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  implicit val ec = ExecutionContext.global
  implicit val userRepository = ???
  implicit val dataRepository = ???
  val add = new AddUserController
  val exists = new ExistsUserController
  val addExp = new AddDataController
  val getStat = new GetStatController
  val remind = new RemindController

  val controller = new api.HttpController(add,exists, addExp, getStat, remind, userRepository, dataRepository, ec)

  val routes =
  /* get {
    pathPrefix("index") {
      getFromResource("index.html")
    } ~ pathPrefix("js") {
      getFromResourceDirectory("js")
    } ~ pathPrefix("css") {
      getFromResourceDirectory("css")
    }
  } ~ */
    controller.routes

  Http().bindAndHandle(routes, "localhost", 8080)
=======
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
>>>>>>> origin/database

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
