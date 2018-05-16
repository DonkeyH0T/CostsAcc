package exppack.repository

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

import exppack.domain._

import scala.collection.JavaConverters._
import scala.concurrent.{ExecutionContext, Future}

class UserRegistrationException(msg: String) extends Exception(msg)
class UserAuthenticationException(msg: String) extends Exception(msg)

trait UserRepositoryUtils {
  this: Repository[Int, User] =>

  def userAuth(user: User): Future[User]
}

trait UserService {
  def nextUserId: Int
 }

trait UserServiceImpl extends UserService {
  val idUser: AtomicInteger = new AtomicInteger(0)
  override def nextUserId: Int = idUser.incrementAndGet
}

trait UserRepository extends Repository[Int, User] with UserRepositoryUtils

final class MemoryUserRepository(implicit ec: ExecutionContext) extends UserRepository with UserServiceImpl {

  private[this] val storage = new ConcurrentHashMap[Int, User]()

  override def all(): Future[Seq[User]] = Future {
    storage.values().asScala.toSeq
  }

  override def put(item: User): Future[User] = all().map(x => x.filter(_.name == item.name)).flatMap {
    case Seq() =>
      val id = nextUserId
      val newUser = item.copy(id = Some(id))
      storage.put(id, newUser)
      Future.successful(newUser)
    case Seq(_) => Future.failed(new UserRegistrationException("user with provided name already exists"))
  }

  override def userAuth(user: User): Future[User] = all().map(x => x.filter(y => y.name == user.name && y.pass == user.pass)).flatMap {
    case Seq() => Future.failed(new UserAuthenticationException("invalid username or password"))
    case s @ Seq(_) => Future.successful(s.head)
  }
}
