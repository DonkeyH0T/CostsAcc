package exppack.domain

import akka.http.scaladsl.model.DateTime

case class Buy(date:DateTime,
               sum:BigDecimal,
               category:Option[Category],
               shop:Option[Shop],
               nextpayment:Option[DateTime])
