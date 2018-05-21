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
              complete(addExp(Request.AddExpense(buy).withUser(user)))
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
                  complete(getStat(Request.WithCategory(period.from, period.to,category).withUser(user)))
                }
            }
          }
        }
      } ~ pathPrefix("byshop") {
        withUser { user: User =>
          withPeriod { (period:Period) =>
            parameter('shop) { shop =>
                get {
                  complete(getStat(Request.WithDateShop(period.from, period.to,shop).withUser(user)))
                }
            }
          }
        }
      }
    } ~ pathPrefix("notifications") {
      withUser { user: User =>
        withPeriod { (period:Period) =>
            get {
              complete(remind(Request.Remind().withUser(user)))
            }
        }
      }
    }
  }
}