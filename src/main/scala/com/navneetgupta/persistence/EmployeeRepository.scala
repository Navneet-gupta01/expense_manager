package com.navneetgupta.persistence

import com.navneetgupta.model.{Employee, EmployeeId, ExpenseSheet}

trait EmployeeRepository[F[_]] {
  def get(id: EmployeeId): F[Option[Employee]]
  def save(employee: Employee): F[Option[Employee]]
  def count(expenseSheet: ExpenseSheet): F[Long]
}
