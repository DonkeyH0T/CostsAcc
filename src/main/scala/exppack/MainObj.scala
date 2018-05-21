package exppack

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

}