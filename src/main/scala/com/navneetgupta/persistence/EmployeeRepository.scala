package com.navneetgupta.persistence

import com.navneetgupta.model.{Employee, EmployeeId, ExpenseSheet}
import freestyle.tagless.tagless

@tagless(true)
trait EmployeeRepository[F[_]] {
  def get(id: EmployeeId): F[Option[Employee]]
  def save(employee: Employee): F[Option[Employee]]
  def count(expenseSheet: ExpenseSheet): F[Long]
}
