package com.example.cart.domain

import scala.math.BigDecimal.RoundingMode

final case class Cart(items: List[CartItem]) {
  lazy val cartSubtotal: BigDecimal = (items.map(i => i.product.price * i.quantity).sum).setScale(2,RoundingMode.HALF_UP)
  lazy val taxPayable: BigDecimal = (cartSubtotal * 0.125).setScale(2,RoundingMode.HALF_UP)
  lazy val totalPayable: BigDecimal = (cartSubtotal + taxPayable).setScale(2,RoundingMode.HALF_UP)

}

