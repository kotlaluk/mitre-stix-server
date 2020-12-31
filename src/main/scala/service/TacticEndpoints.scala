package org.example.mitrestixserver
package service

import dao.Tactics

import cats.effect.IO
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s.HttpRoutes
import org.http4s.circe.CirceEntityCodec.circeEntityEncoder
import org.http4s.dsl.io._

object TacticEndpoints {

  val endpoints = HttpRoutes.of[IO] {
    case GET -> Root => Ok(Tactics.findAll(false).asJson)
  }

}
