package com.navneetgupta.model

import java.util.UUID

import cats.data.NonEmptyList

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

}
