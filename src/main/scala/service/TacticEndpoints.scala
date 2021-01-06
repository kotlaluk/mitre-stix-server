package org.example.mitrestixserver
package service

import repository.TacticsRepository

import cats.effect.IO
import com.kodekutters.stix.CustomStix
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s.HttpRoutes
import org.http4s.circe.CirceEntityCodec.{circeEntityDecoder, circeEntityEncoder}
import org.http4s.dsl.io._


object TacticEndpoints {

  type StixType = CustomStix

  val endpoints = HttpRoutes.of[IO] {
    case GET -> Root => Ok(TacticsRepository.findAllCurrent().asJson)

    case GET -> Root / id => TacticsRepository.findByMitreId(id) match {
      case Right(entity) => Ok(entity.asJson)
      case Left(message) => NotFound(message.asJson)
    }

    case req @ POST -> Root =>
      for {
        stixObj <- req.as[StixType]
        response <- TacticsRepository.add(stixObj) match {
          case Right(entity) => Created(entity.asJson)
          case Left(message) => Conflict(message.asJson)
        }
      } yield response

    case DELETE -> Root / id => TacticsRepository.delete(id) match {
      case Right(entity) => Ok(entity.asJson)
      case Left(message) => NotFound(message.asJson)
    }
  }

}
