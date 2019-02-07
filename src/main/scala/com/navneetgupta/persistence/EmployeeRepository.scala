package com.navneetgupta.persistence

import com.navneetgupta.model.{Employee, EmployeeId}

trait EmployeeRepository[F[_]] {
  def get(id: EmployeeId): F[Option[Employee]]
  def save(employee: Employee): F[Option[Employee]]
}
