package com.navneetgupta

import com.navneetgupta.services.handlers.ServicesHandler
import freestyle.tagless.module

@module
trait Services[F[_]] {
  val services: ServicesHandler[F]
}
