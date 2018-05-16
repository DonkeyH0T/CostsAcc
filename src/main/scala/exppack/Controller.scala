package exppack

import scala.concurrent.Future

trait Controller[Req, Res] extends (Req =>Future[Res])
trait Controller3[ReqA1, ReqA2, ReqA3, Res] extends ((ReqA1, ReqA2, ReqA3) => Future[Res])