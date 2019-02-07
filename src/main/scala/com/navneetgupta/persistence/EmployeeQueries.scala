package com.navneetgupta.persistence

import com.navneetgupta.model.Employee
import doobie.util.update.Update0
import doobie.implicits._


object EmployeeQueries {
  def insert(employee: Employee): Update0 =
    sql"""INSERT INTO employees (id, name, surname) VALUES (${employee.id}, ${employee.name}, ${employee.surname})""".update

}
