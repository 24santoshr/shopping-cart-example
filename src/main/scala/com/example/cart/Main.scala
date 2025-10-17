package com.example.cart

import cats.effect.IOApp
import cats.effect.IO
import com.example.cart.domain.Cart
import com.example.cart.service.{CartService, ProductService}
import org.http4s.ember.client.EmberClientBuilder

object Main extends IOApp.Simple {
  def run: IO[Unit] =  EmberClientBuilder.default[IO].build.use { client =>
    val productService = new ProductService(client)
    val cartService = new CartService(productService)
    val emptyCart = Cart(Nil)

    for {
      cart1 <- cartService.addToCart(emptyCart, "cornflakes", 2)
      cart2 <- cartService.addToCart(cart1, "weetabix", 1)
      _     <- IO.println(s"Cart's subtotal is: ${cart2.cartSubtotal}")
      _     <- IO.println(s"Cart's tax payable is: ${cart2.taxPayable}")
      _     <- IO.println(s"Cart's total payable value is: ${cart2.totalPayable}")
    } yield ()
  }
}
