package com.navneetgupta

import cats.effect._
import cats.syntax.functor._
import com.navneetgupta.errors.HttpErrorHandler
import com.navneetgupta.model.AppError
import doobie.util.transactor.Transactor
import freestyle.tagless.effects.error.implicits._
import freestyle.tagless.loggingJVM.log4s.implicits._
import freestyle.tagless.module
import org.http4s.implicits._
import org.http4s.server.Router
import org.http4s.server.blaze._

@module
trait App1[F[_]] {
  val persistence: Persistence[F]
  val services: Services[F]
}
object MainApp extends IOApp {

  import implicits._
  import AppRoutesMtl._
  import cats.implicits._
  import com.olegpy.meow.hierarchy._

  implicit def appHttpErrorHandler: HttpErrorHandler[IO, AppError] = new AppHttpErrorHandler[IO]

  override def run(args: List[String]): IO[ExitCode] = bootstrap[IO]

  def bootstrap[F[_]: Effect : ConcurrentEffect](implicit T: Transactor[F], api: AppRoutesMtl[F],app: App1[F]): F[ExitCode] = {
    val services = api.routes

    BlazeServerBuilder[F]
      .bindHttp(8087, "localhost")
      .withHttpApp(Router("/" -> services).orNotFound)
      .serve.compile.drain.as(ExitCode.Success)
  }
}
