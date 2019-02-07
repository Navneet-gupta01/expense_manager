package com.navneetgupta.model

import java.util.UUID
import cats.implicits._
import com.navneetgupta.errors.ErrorManagement

final case class EmployeeId(uuid: UUID) extends AnyVal
final case class Employee(id: EmployeeId, name: String, surname: String)

object Employee {
  import implicits._

  val validateId = ErrorManagement.notNull[EmployeeId]("employeeId is null")(_)
  val validateName = ErrorManagement.notEmptyString("employee name is Invalid")(_)
  val validateSurname = ErrorManagement.notEmptyString("employee surname is Invalid")(_)

  def employee(id: EmployeeId, name: String, surname: String): ErrorManagement.Validated[Employee] =
    (validateId(id), validateName(name), validateSurname(surname)).mapN(Employee(_,_,_))

  def employee(name: String, surname: String): ErrorManagement.Validated[Employee] =
    employee(UUID.randomUUID(), name, surname)


  object implicits {
    import scala.language.implicitConversions
    implicit def uuidToEmployeeId(uuid: UUID) : EmployeeId = EmployeeId(uuid)
  }
}

