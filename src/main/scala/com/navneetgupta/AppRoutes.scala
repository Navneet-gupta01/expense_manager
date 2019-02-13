package com.navneetgupta

import cats.effect.Effect
import cats.implicits._
import com.navneetgupta.services.handlers.ServicesHandler
import org.http4s.{HttpRoutes, Response}
import org.http4s._
import org.http4s.dsl.Http4sDsl
import org.http4s.dsl.io._
import org.http4s.implicits._

class AppRoutes[F[_]: Effect](implicit services: ServicesHandler[F]) extends Http4sDsl[F] {
  import com.navneetgupta.codecs.Codecs._

  val prefix = "expense"

  val endpoints = HttpRoutes.of[F] {
    case _ => for {
      employee <- services.createEmployee("Navneet ", "Gupta")
    } yield Response(Status.Ok)
  }
}

object AppRoutes {
  implicit def instance[F[_]: Effect](implicit services: ServicesHandler[F]) = new AppRoutes[F]
}