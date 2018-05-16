package exppack.api
/*
import akka.http.scaladsl.model.DateTime
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{Directive1, Route}
import exppack.repository.{DataRepository, UserRepository}
import exppack.api.CustomUnmarshaller._
import exppack.domain.{Buy, MaybeUser, Period, Request}
import org.json4s.{DefaultFormats, jackson}
import org.json4s.jackson.Serialization

import scala.concurrent.ExecutionContext

class HttpController(implicit val userRepository: UserRepository, implicit val dataRepository: DataRepository) {
  implicit val format: DefaultFormats.type = DefaultFormats
  implicit val serialization: Serialization.type = jackson.Serialization

  val withPeriod: Directive1[Period] = parameters('from.as(toDateTime), 'to.as(toDateTime)).as(Period)
  val withUser: Directive1[MaybeUser] = parameters('login, 'password).as(MaybeUser)
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
        withUser { maybeUser: MaybeUser =>
          get {
            // TODO: починить AddUserController!
            complete(AddUserController(userRepository)(Request.AddUser(maybeUser.name, maybeUser.pass, None)))
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
        withBuy { buy: Buy =>
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
}*/