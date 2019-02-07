package com.navneetgupta.services.handlers

import cats.Monad
import com.navneetgupta.services.Services

class ServicesHandler[F[_]: Monad] extends Services[F] {

}
