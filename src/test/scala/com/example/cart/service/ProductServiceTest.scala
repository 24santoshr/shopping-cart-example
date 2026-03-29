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

  val CORNFLAKES_JSON = "cornflakes.json"
  val WEETABIX_JSON = "weetabix.json"
  val SHREDDIES_JSON = "shreddies.json"

  val PRODUCT_CORNFLAKES = "Corn Flakes"
  val PRODUCT_WEETABIX = "Weetabix"
  val PRODUCT_SHREDDIES = "Shreddies"
  val PRODUCT_CHEERIOS = "cheerios"

  val localhostUrl = "http://localhost/"

  test("get product details based on product name") {

    val dummyProductData: HttpApp[IO] = HttpRoutes.of[IO] {
      case GET -> Root / CORNFLAKES_JSON => Ok(Product(PRODUCT_CORNFLAKES, 2.52))
      case GET -> Root / WEETABIX_JSON => Ok(Product(PRODUCT_WEETABIX, 9.98))
    }.orNotFound

    val mockClient: Client[IO] = Client.fromHttpApp(dummyProductData)
    val productService = new ProductService(mockClient, localhostUrl)

    for {
      productData1 <- productService.getProductData("cornflakes")
      productData2 <- productService.getProductData(PRODUCT_WEETABIX)
    } yield {
      assertEquals(productData1.title,PRODUCT_CORNFLAKES)
      assertEquals(productData1.price, BigDecimal(2.52))
      assertEquals(productData2.title, PRODUCT_WEETABIX)
      assertEquals(productData2.price, BigDecimal(9.98))
    }
  }

  test("getProductData should throw error for non-existent product") {
    val dummyProductData: HttpApp[IO] = HttpRoutes.of[IO] {
      case GET -> Root /SHREDDIES_JSON => Ok(Product(PRODUCT_SHREDDIES, 4.68))
    }.orNotFound

    val mockClient: Client[IO] = Client.fromHttpApp(dummyProductData)
    val productService = new ProductService(mockClient, localhostUrl)

    productService.getProductData(PRODUCT_CHEERIOS).attempt.map { result =>
      assert(result.isLeft)
    }
  }

  test("getUrl should construct correct URL") {
    val productService = new ProductService(null.asInstanceOf[Client[IO]], localhostUrl)

    assertEquals(productService.getUrl("cornflakes"), Uri.unsafeFromString("http://localhost/cornflakes.json"))
    assertEquals(productService.getUrl("Weetabix"), Uri.unsafeFromString("http://localhost/weetabix.json"))
  }

  test("getUrl should handle empty product name") {
    val productService = new ProductService(null.asInstanceOf[Client[IO]], localhostUrl)
    assertEquals(productService.getUrl(""), Uri.unsafeFromString("http://localhost/.json"))
  }
}
