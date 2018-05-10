package exppack

import exppack.Controllers.{AddUserController, RemindController, UserController}
import exppack.repository.MemoryUserRepository
import exppack.domain.{Data, RegSample, UserRequest}
import org.joda.time._

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global
object MainObj extends App{

  implicit val rep = new UserRepositoryClass
  val z = new UserController
  val x = new AddUserController
  import Request._

  val f = for {
    a <- x(AddUser("aa","bb",None))
    b <- x(AddUser("aa","bb",None))
  } yield {println(a,b)}

  Await.ready(f, Duration.Inf)

}