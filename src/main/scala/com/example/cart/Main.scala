package com.example.cart

import cats.effect.IOApp
import cats.effect.IO
import com.example.cart.domain.Cart
import com.example.cart.service.{CartService, ProductService}
import org.http4s.ember.client.EmberClientBuilder

object Main extends IOApp.Simple {
  def run: IO[Unit] =  ???
}
