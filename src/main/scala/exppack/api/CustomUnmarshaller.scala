package exppack.api

import akka.http.scaladsl.model.DateTime
import akka.http.scaladsl.server.util.ConstructFromTuple
import akka.http.scaladsl.unmarshalling.Unmarshaller
import exppack.domain.{Category, Shop, User}

import scala.concurrent.Future
/*
object CustomUnmarshaller {
  val toDateTime: Unmarshaller[String, DateTime] = DateTime.fromIsoDateTimeString(_: String) match {
    case Some(a) => Future.successful(a)
    // TODO: обработка ошибок парсинга дат
    case None => Future.failed(???)
  }

  val toBigDecimal: Unmarshaller[String, BigDecimal] = Unmarshaller.strict[String, BigDecimal] {
    BigDecimal.exact
  }
  val toCategory: Unmarshaller[String, Category] = Unmarshaller.strict[String, Category] {
    Category
  }
  val toShop: Unmarshaller[String, Shop] = Unmarshaller.strict[String, Shop] {
    Shop
  }
  val toUser: ConstructFromTuple[(String, String), User] = (logpass: (String, String)) => User(logpass._1, logpass._2, None)

}
*/