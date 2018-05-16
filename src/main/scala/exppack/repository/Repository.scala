package exppack.repository

import scala.concurrent.Future


trait Repository[ID, T] {

  def put(item: T): Future[T]

  def all(): Future[Seq[T]]

}
//trait NewFunction {
  // def get(key: ID): Future[T]
  // def delete(key: ID): Future[Unit]
//}