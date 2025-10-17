package com.example.cart.service

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import com.example.cart.domain.{Cart, Product}
import org.http4s._
import org.http4s.dsl.io._
import org.http4s.client.Client
import org.http4s.implicits._
import io.circe.generic.auto._
import munit.CatsEffectSuite
import org.http4s.circe.CirceEntityCodec.circeEntityEncoder


class CartServiceTest extends CatsEffectSuite {

  test("Add multiple items to cart and compute respective values") {

    val dummyProductData: HttpApp[IO] = HttpRoutes.of[IO] {
      case GET -> Root / "cornflakes.json" =>
        Ok(Product("Corn Flakes", 2.52))
      case GET -> Root / "weetabix.json" =>
        Ok(Product("Weetabix", 9.98))
    }.orNotFound

    val localhostUrl = "http://localhost/"
    val mockClient: Client[IO] = Client.fromHttpApp(dummyProductData)
    val productService = new ProductService(mockClient, localhostUrl)
    val cartService = new CartService(productService)
    val emptyCart = Cart(Nil)

    for {
      cart1 <- cartService.addToCart(emptyCart, "cornflakes", 2)
      cart2 <- cartService.addToCart(cart1, "weetabix", 1)
    } yield {
      assertEquals(cart2.items.size, 2)
      assertEquals(cart2.cartSubtotal.setScale(2), BigDecimal(15.02))
      assertEquals(cart2.taxPayable.setScale(2), BigDecimal(1.88))
      assertEquals(cart2.totalPayable.setScale(2), BigDecimal(16.90))
    }
  }

  test("Add single item to the cart and compute the values") {

    val dummyProductData: HttpApp[IO] = HttpRoutes.of[IO] {
      case GET -> Root / "shreddies.json" =>
        Ok(Product("Shreddies", 4.68))
    }.orNotFound

    val localhostUrl = "http://localhost/"
    val mockClient: Client[IO] = Client.fromHttpApp(dummyProductData)
    val productService = new ProductService(mockClient, localhostUrl)
    val cartService = new CartService(productService)
    val emptyCart = Cart(Nil)

    for {
      cart1 <- cartService.addToCart(emptyCart, "shreddies", 2)
    } yield {
      assertEquals(cart1.items.size, 1)
      assertEquals(cart1.cartSubtotal.setScale(2), BigDecimal(9.36))
      assertEquals(cart1.taxPayable.setScale(2), BigDecimal(1.17))
      assertEquals(cart1.totalPayable.setScale(2), BigDecimal(10.53))
    }
  }

  test("Add zero quantity of an item to the cart and check the cart size") {

    val dummyProductData: HttpApp[IO] = HttpRoutes.of[IO] {
      case GET -> Root / "frosties.json" =>
        Ok(Product("Frosties", 4.99))
    }.orNotFound

    val localhostUrl = "http://localhost/"
    val mockClient: Client[IO] = Client.fromHttpApp(dummyProductData)
    val productService = new ProductService(mockClient, localhostUrl)
    val cartService = new CartService(productService)
    val emptyCart = Cart(Nil)

    for {
      cart1 <- cartService.addToCart(emptyCart, "frosties", 0)
    } yield {
      assertEquals(cart1.items.size, 0)
    }
  }
}
