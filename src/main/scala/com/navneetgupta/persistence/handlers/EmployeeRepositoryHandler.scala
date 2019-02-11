package com.navneetgupta.persistence.handlers

import java.util.UUID

import cats.Monad
import com.navneetgupta.model.{Employee, EmployeeId, ExpenseSheet}
import com.navneetgupta.persistence.{EmployeeQueries, EmployeeRepository}
import doobie.util.transactor.Transactor
import doobie.implicits._
import doobie.postgres.implicits._

class EmployeeRepositoryHandler[F[_]: Monad](implicit T: Transactor[F]) extends EmployeeRepository.Handler[F] {
  import EmployeeQueries._
  import com.navneetgupta.persistence.DoobieCustomMapping.implicits._

  override def get(id: EmployeeId): F[Option[Employee]] =
    fetchQuery(id)
      .option
      .transact(T)

  override def save(employee: Employee): F[Option[Employee]] =
    insertQuery(employee)
      .withUniqueGeneratedKeys[UUID]("id")
      .flatMap(x => fetchQuery(EmployeeId(x)).option)
      .transact(T)

  override def count(expenseSheet: ExpenseSheet): F[Long] =
    countEmployeeQuery(expenseSheet)
      .unique
      .transact(T)
}
