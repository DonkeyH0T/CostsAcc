package exppack.api

import org.joda.time.DateTime
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{Directive1, Route}
import de.heikoseeberger.akkahttpjson4s.Json4sSupport
import exppack.repository.{DataRepository, UserRepository}
import exppack.api.CustomUnmarshaller._
import exppack.domain._
import exppack.Controllers._
import org.json4s.{DefaultFormats, jackson}
import org.json4s.jackson.Serialization

import scala.concurrent.ExecutionContext

class HttpController(val add: AddUserController,
                     val exists: ExistsUserController,
                     val addExp: AddDataController,
                     val getStat: GetStatController,
                     val remind: RemindController,
                     val userC: UserController,
                     implicit val userRepository: UserRepository,
                     implicit val dataRepository: DataRepository,
                     implicit val ec: ExecutionContext) extends Json4sSupport{
  implicit val format: DefaultFormats.type = DefaultFormats
  implicit val serialization: Serialization.type = jackson.Serialization

  val withPeriod: Directive1[Period] = parameters('from.as(toDateTime), 'to.as(toDateTime)).as(Period)
  val withUser: Directive1[User] = parameters('login, 'password).as(toUser)
  val withData: Directive1[Data] = parameters(
    'date.as(toDateTime),
    'sum.as(toBigDecimal),
    'category.?,
    'shop.?,
    'nextpayment.as(toDateTime).?
  ).as(toData)

  val routes: Route = pathPrefix("api") {
    pathPrefix("user") {
      pathPrefix("register") {
        withUser { user: User =>
          get {
            complete(add(UserRequest.AddUser(user)))
          }
        }
      } ~
        pathPrefix("exists") {
          withUser { user: User =>
            get {
              complete(exists(UserRequest.Exists(user)))
            }
          }
        }
    } ~ pathPrefix("expence") {
      withUser { user: User =>
        withData { buy: Data =>
            post {
              complete(userC(user, Request.AddExpense(buy)).flatMap(addExp))
            }
        }
      } /*~ pathPrefix("similar") {
        withUser { user: User =>
          parameters('date.as(toDateTime),'category?) {
            (date:DateTime, category:Option[String]) =>
              get {
                complete(???)
              }
          }
        }
      }*/
    } ~ pathPrefix("stat") {
      pathPrefix("bycategory") {
        withUser { user: User =>
          withPeriod { (period: Period) =>
            parameter('category) { category =>
                get {
                  complete(userC(user, Request.WithCategory(period.from, period.to,category).flatMap(getStat)))
                }
            }
          }
        }
      } ~ pathPrefix("byshop") {
        withUser { user: User =>
          withPeriod { (period:Period) =>
            parameter('shop) { shop =>
                get {
                  complete(userC(user, Request.WithDateShop(period.from, period.to,shop)).flatMap(getStat))
                }
            }
          }
        }
      }
    } ~ pathPrefix("notifications") {
      withUser { user: User =>
        get {
          complete(userC(user, Request.Remind()).flatMap(remind))
        }
      }
    }
  }
}