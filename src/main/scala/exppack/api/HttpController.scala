package exppack.api

import akka.http.scaladsl.model.DateTime
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{Directive1, Route}
import exppack.{AddUserController, MaybeUser, UserRepository}
import exppack.api.CustomUnmarshaller._
import exppack.domain.Period
import org.json4s.{DefaultFormats, jackson}
import org.json4s.jackson.Serialization

import scala.concurrent.ExecutionContext

class HttpController {
  implicit val format: DefaultFormats.type = DefaultFormats
  implicit val serialization: Serialization.type = jackson.Serialization
  implicit val userRepository: UserRepository

  val withPeriod = parameters('from.as(toDateTime), 'to.as(toDateTime)).as(Period)
  val withUser: Directive1[MaybeUser] = parameters('login, 'password).as(MaybeUser)

  val routes: Route = pathPrefix("api") {
    pathPrefix("user") {
      pathPrefix("register") {
        withUser { maybeUser: MaybeUser =>
          get {
            complete(???)
          }
        }
      } ~
        pathPrefix("exists") {
          withUser { maybeUser: MaybeUser =>
            get {
              complete(???)
            }
          }
        }
    } ~ pathPrefix("expence") {
      withUser { maybeUser: MaybeUser =>
        parameters('date.as(toDateTime), 'sum.as(toBigDecimal), 'category?, 'shop?, 'nextpayment?) {
          (date:DateTime, sum:BigDecimal, category:Option[String], shop:Option[String], nextpayment:Option[String]) =>
            post {
              complete(???)
            }
        }
      } ~ pathPrefix("similar") {
        withUser { maybeUser: MaybeUser =>
          parameters('date.as(toDateTime),'category?) {
            (date:DateTime, category:Option[String]) =>
              get {
                complete(???)
              }
          }
        }
      }
    } ~ pathPrefix("stat") {
      pathPrefix("bycategory") {
        withUser { maybeUser: MaybeUser =>
          withPeriod { (period: Period) =>
            parameter('category?) { category =>
                get {
                  complete(???)
                }
            }
          }
        }
      } ~ pathPrefix("byshop") {
        withUser { maybeUser: MaybeUser =>
          withPeriod { (period:Period) =>
            parameter('shop?) { shop =>
                get {
                  complete(???)
                }
            }
          }
        }
      }
    } ~ pathPrefix("notifications") {
      withUser { maybeUser: MaybeUser =>
        withPeriod { (period:Period) =>
            get {
              complete(???)
            }
        }
      }
    }
  }
}