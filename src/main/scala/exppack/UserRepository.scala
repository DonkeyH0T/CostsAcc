package exppack

import java.time.LocalDate

import scala.concurrent.{ExecutionContext, Future}
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

import scala.collection.JavaConverters._

class UserRegistrationException(msg: String) extends Exception(msg)
class UserAuthenticationException(msg: String) extends Exception(msg)

trait UserRepositoryUtils {
  def checkAndSave(name: String, pass: String): Future[User]
  def userAuth(name: String, pass: String): Future[User]
}

trait UserService {
  def nextUserId: Int
 }

trait UserServiceImpl extends UserService {
  val idUser: AtomicInteger = new AtomicInteger(0)
  override def nextUserId: Int = idUser.incrementAndGet
}

trait UserRepository extends Repository[Int, User] with UserRepositoryUtils

final class UserRepositoryClass(implicit ec: ExecutionContext) extends UserRepository with UserServiceImpl {

  private[this] val storage = new ConcurrentHashMap[Int, User]()

  override def put(item: User): Future[Unit] = Future {
    storage.put(item.id, item)
  }

  override def all(): Future[Seq[User]] = Future {
    storage.values().asScala.toSeq
  }

  override def checkAndSave(name: String, pass: String): Future[User] = all().map(x => x.filter(_.name == name)).flatMap {
    case Seq() =>
      val newUser = User(nextUserId, name, pass)
      put(newUser)
      Future.successful(newUser)
    case Seq(_) => Future.failed(new UserRegistrationException("user with provided name already exists"))
  }

  override def userAuth(name: String, pass: String): Future[User] = all().map(x => x.filter(y => y.name == name && y.pass == pass)).flatMap {
    case Seq() => Future.failed(new UserAuthenticationException("invalid username or password"))
    case s @ Seq(_) => Future.successful(s.head)
  }
}
