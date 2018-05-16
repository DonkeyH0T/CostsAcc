package exppack.api
/*
import akka.http.scaladsl.model.DateTime
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{Directive1, Route}
import exppack.repository.{DataRepository, UserRepository}
import de.heikoseeberger.akkahttpjson4s.Json4sSupport
import exppack.repository.{DataRepository, UserRepository}
import exppack.api.CustomUnmarshaller._
import exppack.domain.{Buy, MaybeUser, Period, Request}
import org.json4s.{DefaultFormats, jackson}
import org.json4s.jackson.Serialization

import scala.concurrent.ExecutionContext

class HttpController(implicit val userRepository: UserRepository,
                     implicit val dataRepository: DataRepository,
                     implicit val ec: ExecutionContext) extends Json4sSupport{
  implicit val format: DefaultFormats.type = DefaultFormats
  implicit val serialization: Serialization.type = jackson.Serialization

  val withPeriod: Directive1[Period] = parameters('from.as(toDateTime), 'to.as(toDateTime)).as(Period)
  val withUser: Directive1[User] = parameters('login, 'password).as(toUser)
  val withBuy: Directive1[Buy] = parameters(
    'date.as(toDateTime),
    'sum.as(toBigDecimal),
    'category.as(toCategory)?,
    'shop.as(toShop)?,
    'nextpayment.as(toDateTime)?
  ).as(Buy)

  val routes: Route = pathPrefix("api") {
    pathPrefix("user") {
      pathPrefix("register") {
        withUser { user: User =>
          get {
            val uc = new exppack.Controllers.AddUserController
            complete(uc(UserRequest.AddUser(user)))
          }
        }
      } ~
        pathPrefix("exists") {
          withUser { user: User =>
            get {
              val uc = new exppack.Controllers.ExistsUserController
              complete(uc(UserRequest.Exists(user)))
            }
          }
        }
    } ~ pathPrefix("expence") {
      withUser { user: User =>
        withBuy { buy: Buy =>
            post {
              complete(???)
            }
        }
      } ~ pathPrefix("similar") {
        withUser { user: User =>
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
        withUser { user: User =>
          withPeriod { (period: Period) =>
            parameter('category?) { category =>
                get {
                  complete(???)
                }
            }
          }
        }
      } ~ pathPrefix("byshop") {
        withUser { user: User =>
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
      withUser { user: User =>
        withPeriod { (period:Period) =>
            get {
              complete(???)
            }
        }
      }
    }
  }
}*/