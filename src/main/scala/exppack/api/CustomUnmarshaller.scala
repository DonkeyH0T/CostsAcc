package exppack.api

import akka.http.scaladsl.model.DateTime
import akka.http.scaladsl.unmarshalling.Unmarshaller

import scala.concurrent.Future

object CustomUnmarshaller {
  val toDateTime: Unmarshaller[String, DateTime] = Unmarshaller.apply[String, DateTime] {
    DateTime.fromIsoDateTimeString(_).andThen {
      case Some(a) => Future.successful(a)
      case None => Future.failed(???) // TODO: обработка ошибок парсинга дат
    }
  }
  val toBigDecimal: Unmarshaller[String, BigDecimal] = Unmarshaller.strict[String, BigDecimal] {
    BigDecimal.exact
  }

}
