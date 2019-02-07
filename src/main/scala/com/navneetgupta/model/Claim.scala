package com.navneetgupta.model

import java.util.UUID

import cats.data.NonEmptyList
import cats.implicits._
import com.navneetgupta.errors.ErrorManagement

final case class ClaimId(uuid: UUID) extends AnyVal
sealed trait Claim {
  def id: ClaimId
  def employee: Employee
  def expenses: NonEmptyList[Expense]
}

final case class PendingClaim(id: ClaimId,
                              employee: Employee,
                              expenses: NonEmptyList[Expense]) extends Claim
object Claim {
  object implicits {
    import scala.language.implicitConversions

    implicit def uuidToClaimId(uuid: UUID) : ClaimId = ClaimId(uuid)
  }
}

object PendingClaim {
  import Claim.implicits._
  def create(employee: Employee, expenses: NonEmptyList[Expense]) : ErrorManagement.Validated[PendingClaim] =
    new PendingClaim(UUID.randomUUID(), employee, expenses).validNel
}