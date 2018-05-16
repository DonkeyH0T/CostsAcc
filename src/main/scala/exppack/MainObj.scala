package exppack

import org.joda.time._

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global
object MainObj extends App{


  implicit val rep = new MemoryUserRepository
  val z = new UserController
  val x = new AddUserController
  import Request._



  val f = for {
    a <- x(AddUser("aa","bb",None))
    b <- x(AddUser("aa","bb",None))
  } yield {println(a,b)}

  Await.ready(f, Duration.Inf)


  val qq = new DateTime("2018-05-20T00:00:00.000+03:00")
  println(Days.daysBetween(qq.toLocalDate, LocalDate.now()).getDays)


  val tmp =Data(new DateTime("2017-04-15T00:00:00.000+03:00"), 300, Some("internet"), None, Some(new DateTime("2018-05-15T00:00:00.000+03:00")), None, Some(1))

 val k = tmp.nextPayment match {
    case Some(x)      if (Days.daysBetween(x.toLocalDate, LocalDate.now()).getDays) < 5 =>
      RegSample(x, "11", tmp.cost)
    case _ => false

  }

println(k)


}