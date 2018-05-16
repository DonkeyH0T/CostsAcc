package exppack.Controllers

import scala.concurrent.Future

trait Controller[Req, Res] extends (Req =>Future[Res])
trait Controller2[ReqA1, ReqA2, Res] extends ((ReqA1, ReqA2) => Future[Res])