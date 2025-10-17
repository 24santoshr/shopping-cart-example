package com.example.cart.service

import cats.effect._
import com.example.cart.domain.Product
import io.circe.generic.auto._
import org.http4s._
import org.http4s.circe.CirceEntityCodec._
import org.http4s.client._


class ProductService(client: Client[IO],
                     baseUrl: String = "https://raw.githubusercontent.com/mattjanks16/shopping-cart-test-data/main/") {

  private val JSON_EXTENSION = "json"

  def getProductData(productName: String): IO[Product] = {
    val fullProductUrl = Uri.unsafeFromString(s"$baseUrl${productName.toLowerCase}.$JSON_EXTENSION")
    client.expect[Product](fullProductUrl)
  }
}
