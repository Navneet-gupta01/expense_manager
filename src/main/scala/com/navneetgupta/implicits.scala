package com.navneetgupta

import java.util.Properties

import cats.Monad
import cats.effect.{ContextShift, IO}
import com.navneetgupta.persistence.handlers.{ClaimRepositoryHandler, EmployeeRepositoryHandler, ExpenseSheetRepositoryHandler}
import com.zaxxer.hikari.{HikariConfig, HikariDataSource}
import doobie.hikari.HikariTransactor
import doobie.util.transactor.Transactor

import scala.concurrent.ExecutionContext

object implicits extends ExecutionContextImplicit with HikariDBImplicits with RepositoryHandlerImplicits


trait HikariDBImplicits {
  val config = new HikariConfig(new Properties {
    setProperty("driverClassName", "org.postgresql.Driver")
    setProperty("jdbcUrl", "jdbc:postgresql:expense_manager")
    setProperty("username", "postgres")
    setProperty("password", "postgres")
    setProperty("maximumPoolSize", "10")
    setProperty("minimumIdle", "10")
    setProperty("idleTimeout", "600000")
    setProperty("connectionTimeout", "30000")
    setProperty("connectionTestQuery", "SELECT 1")
    setProperty("maxLifetime", "1800000")
    setProperty("autoCommit", "true")
  })
  implicit def xa(implicit CT: ExecutionContext, TC: ExecutionContext, ev: ContextShift[IO]): HikariTransactor[IO] =
    HikariTransactor.apply[IO](new HikariDataSource(config), CT, TC)
}

trait ExecutionContextImplicit {
  import scala.concurrent.ExecutionContext.Implicits.global
  implicit val ec: ExecutionContext = ExecutionContext.Implicits.global
}

trait RepositoryHandlerImplicits {
  implicit def claimRepositoryHandlerImplicit[F[_]: Monad](implicit T: Transactor[F]) = new ClaimRepositoryHandler[F]
  implicit def employeeRepositoryHandlerImplicit[F[_]: Monad](implicit T: Transactor[F]) = new EmployeeRepositoryHandler[F]
  implicit def expenseSheetRepositoryHandlerImplicit[F[_]: Monad](implicit T: Transactor[F]) = new ExpenseSheetRepositoryHandler[F]
}