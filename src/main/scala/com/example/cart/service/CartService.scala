package com.example.cart.service

import cats.effect.IO
import com.example.cart.domain.{Cart, CartItem}

class CartService(productService: ProductService) {

  def addToCart(cart: Cart, productName: String, quantity: Int): IO[Cart] = {

    if (quantity <= 0) IO.pure(cart)
    else {
      for {
        product <- productService.getProductData(productName)
        updatedCart = cart.items :+ CartItem(product, quantity)
      } yield Cart(updatedCart)
    }
  }
}
