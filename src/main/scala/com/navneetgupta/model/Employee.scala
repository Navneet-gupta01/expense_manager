package com.navneetgupta.model

import java.util.UUID

final case class EmployeeId(uuid: UUID) extends AnyVal
final case class Employee(id: EmployeeId, name: String, surname: String)

object Employee {

}

