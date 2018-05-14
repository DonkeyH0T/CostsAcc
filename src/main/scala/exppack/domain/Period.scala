package exppack.domain

import akka.http.scaladsl.model.DateTime

case class Period(from: DateTime, to: DateTime)