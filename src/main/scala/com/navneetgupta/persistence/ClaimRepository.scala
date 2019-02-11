package com.navneetgupta.persistence

import com.navneetgupta.model.Claim
import freestyle.tagless.tagless

@tagless(true)
trait ClaimRepository[F[_]] {
//  def get(id: ClaimId): F[Option[Claim]]
  def save(claim: Claim): F[Option[Claim]]
}
