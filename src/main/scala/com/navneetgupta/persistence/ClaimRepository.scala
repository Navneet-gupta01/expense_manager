package com.navneetgupta.persistence

import com.navneetgupta.model.{Claim, ClaimId}

trait ClaimRepository[F[_]] {
  def get(id: ClaimId): F[Option[Claim]]
  def save(claim: Claim): F[Option[Claim]]
}
