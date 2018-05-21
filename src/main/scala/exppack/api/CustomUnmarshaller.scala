package exppack.api

import org.joda.time.DateTime
import akka.http.scaladsl.server.util.ConstructFromTuple
import akka.http.scaladsl.unmarshalling.Unmarshaller
import akka.stream.Materializer
import exppack.domain.{Data, User}

import scala.concurrent.{ExecutionContext, Future}

object CustomUnmarshaller {
  val toDateTime: Unmarshaller[String, DateTime] = new Unmarshaller[String, DateTime] {


    override def apply(s: String)(implicit ec: ExecutionContext, materializer: Materializer): Future[DateTime] = try {
      Future.successful(DateTime.parse(s))
    } catch {
      case e: IllegalArgumentException =>
        Future.failed(e)
    }
  }

  val toBigDecimal: Unmarshaller[String, BigDecimal] = Unmarshaller.strict[String, BigDecimal] {
    BigDecimal.exact
  }
  val toUser: ConstructFromTuple[(String, String), User] = (logpass: (String, String)) => User(logpass._1, logpass._2, None)
  val toData: ConstructFromTuple[(DateTime,BigDecimal,Option[String],Option[String],Option[DateTime]), Data] = Data(_:DateTime, _:BigDecimal, _:Option[String],_:Option[String], _:Option[DateTime])
}
