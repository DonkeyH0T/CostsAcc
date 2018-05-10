package exppack

import scala.concurrent.Future

trait Controller[Req, Res] extends (Req =>Future[Res])
trait Controller2[ReqA1, ReqA2, Res] extends Function2[ReqA1, ReqA2, Future[Res]]
trait Controller3[ReqA1, ReqA2, ReqA3, Res] extends Function3[ReqA1, ReqA2, ReqA3, Future[Res]]