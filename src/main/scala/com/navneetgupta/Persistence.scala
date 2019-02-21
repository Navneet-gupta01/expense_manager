package com.navneetgupta

import com.navneetgupta.persistence.{ClaimRepository, EmployeeRepository, ExpenseSheetRepository}
import freestyle.tagless.module

@module
trait Persistence[F[_]] {
  val calimRepository: ClaimRepository[F]
  val employeeRepository: EmployeeRepository[F]
  val expenseSheetRepository: ExpenseSheetRepository[F]
}