package com.example.cart.service

import cats.effect.IO
import com.example.cart.domain.Product
import io.circe.generic.auto._
import munit.CatsEffectSuite
import org.http4s._
import org.http4s.circe.CirceEntityCodec.circeEntityEncoder
import org.http4s.client.Client
import org.http4s.dsl.io._
import org.http4s.implicits._

class ProductServiceTest extends CatsEffectSuite {

  test("get product details based on product name") {

    val dummyProductData: HttpApp[IO] = HttpRoutes.of[IO] {
      case GET -> Root / "cornflakes.json" => Ok(Product("Corn Flakes", 2.52))
      case GET -> Root / "weetabix.json" => Ok(Product("Weetabix", 9.98))
    }.orNotFound

    val localhostUrl = "http://localhost/"
    val mockClient: Client[IO] = Client.fromHttpApp(dummyProductData)
    val productService = new ProductService(mockClient, localhostUrl)

    for {
      productData1 <- productService.getProductData("cornflakes")
      productData2 <- productService.getProductData("Weetabix")
    } yield {
      assertEquals(productData1.title, "Corn Flakes")
      assertEquals(productData1.price, BigDecimal(2.52))
      assertEquals(productData2.title, "Weetabix")
      assertEquals(productData2.price, BigDecimal(9.98))
    }
  }

  test("getProductData should throw error for non-existent product") {
    val dummyApp: HttpApp[IO] = HttpRoutes.of[IO] {
      case GET -> Root / "cornflakes.json" => Ok(Product("Corn Flakes", 2.52))
    }.orNotFound

    val localhostUrl = "http://localhost/"
    val mockClient: Client[IO] = Client.fromHttpApp(dummyApp)
    val productService = new ProductService(mockClient, localhostUrl)

    productService.getProductData("cheerios").attempt.map { result =>
      assert(result.isLeft)
    }
  }
}
