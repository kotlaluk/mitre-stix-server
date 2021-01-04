package org.example.mitrestixserver
package service

import repository.TacticsRepository

import cats.effect.IO
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s.HttpRoutes
import org.http4s.circe.CirceEntityCodec.circeEntityEncoder
import org.http4s.dsl.io._


object TacticEndpoints {

  val endpoints = HttpRoutes.of[IO] {
    case GET -> Root => Ok(TacticsRepository.findAllCurrent().asJson)

    case GET -> Root / id => TacticsRepository.findByMitreId(id) match {
      case Some(entity) => Ok(entity.asJson)
      case None => NotFound()
    }
  }

}
