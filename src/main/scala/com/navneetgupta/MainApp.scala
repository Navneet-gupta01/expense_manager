package com.navneetgupta

import cats.effect._
import cats.syntax.functor._
import doobie.util.transactor.Transactor
import freestyle.tagless.effects.error.implicits._
import freestyle.tagless.loggingJVM.log4s.implicits._
import freestyle.tagless.module
import org.http4s.implicits._
import org.http4s.server.Router
import org.http4s.server.blaze._


object MainApp extends IOApp {

  import implicits._

  override def run(args: List[String]): IO[ExitCode] = bootstrap[IO]

  def bootstrap[F[_]: Effect : ConcurrentEffect](implicit T: Transactor[F], api: AppRoutes[F]): F[ExitCode] = {
    val services = api.endpoints

    BlazeServerBuilder[F]
      .bindHttp(8087, "localhost")
      .withHttpApp(Router("/" -> services).orNotFound)
      .serve.compile.drain.as(ExitCode.Success)
  }
}
