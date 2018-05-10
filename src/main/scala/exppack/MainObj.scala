package exppack

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
    c <- z("aa","bb", Remind(None))
  } yield {println(a,b,c)}

  Await.ready(f, Duration.Inf)








}