package com.navneetgupta.model

import java.util.Date

final case class Money(amount: Double) extends AnyVal

sealed trait Expense {
  def cost: Money
  def date: Date
}

final case class TravelExpense(cost: Money, date: Date, from: String, to: String) extends Expense
final case class FoodExpense(cost: Money, date: Date) extends Expense
final case class AccommodationExpense(cost: Money, hotel: String, date: Date) extends Expense
final case class OtherExpense(cost: Money, date: Date, description: String) extends Expense

object Expense {

}