package com.navneetgupta

import cats.{Monad, MonadError}
import cats.effect.Effect
import cats.implicits._
import com.navneetgupta.errors.{HttpErrorHandler, RoutesHttpErrorHandler}
import com.navneetgupta.model.{AppError, EntityNotFound, InvalidInputParams}
import com.navneetgupta.services.handlers.ServicesHandler
import org.http4s.{HttpRoutes, Response}
import org.http4s._
import org.http4s.dsl.Http4sDsl
import org.http4s.dsl.io._
import org.http4s.implicits._

class AppRoutesMtl[F[_]: Effect](implicit services: ServicesHandler[F], H: HttpErrorHandler[F, AppError]) extends Http4sDsl[F]  {
  import com.navneetgupta.codecs.Codecs._

  val prefix = "expense"

  private val endpoints = HttpRoutes.of[F] {
    case _ => (for {
      employee <- services.createEmployee("", "")
    } yield Response(Status.Ok))
  }

  val routes: HttpRoutes[F] = H.handle(endpoints)
}

object AppRoutesMtl {
  implicit def instance[F[_]: Effect](implicit
                                      services: ServicesHandler[F],
                                      H: HttpErrorHandler[F, AppError]) = new AppRoutesMtl[F]
}

class AppHttpErrorHandler[F[_]: MonadError[?[_], AppError]](implicit M: Monad[F]) extends HttpErrorHandler[F, AppError] with Http4sDsl[F] {
  private val handler: AppError => F[Response[F]] = {
    case InvalidInputParams(msg) => BadRequest(msg)
    case EntityNotFound(msg) => BadRequest(msg)
  }

  override def handle(routes: HttpRoutes[F]): HttpRoutes[F] =
    RoutesHttpErrorHandler(routes)(handler)
}
//
//object AppHttpErrorHandler {
//  implicit def instance[F[_]: MonadError[?[_], AppError]]= new AppHttpErrorHandler[F]
//}
