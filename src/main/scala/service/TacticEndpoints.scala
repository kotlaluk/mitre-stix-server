package org.example.mitrestixserver
package service

import cats.effect.IO
import com.kodekutters.stix.CustomStix
import io.circe.generic.auto._
import io.circe.syntax._
import org.example.mitrestixserver.repository.sdo.TacticRepository
import org.http4s.HttpRoutes
import org.http4s.circe.CirceEntityCodec.{circeEntityDecoder, circeEntityEncoder}
import org.http4s.dsl.io._


object TacticEndpoints {

  type SDOType = CustomStix

  val endpoints = HttpRoutes.of[IO] {
    case GET -> Root => Ok(TacticRepository.findAllCurrent().asJson)

    case GET -> Root / id => TacticRepository.findByMitreId(id) match {
      case Right(obj) => Ok(obj.asJson)
      case Left(message) => NotFound(message.asJson)
    }

    case req @ POST -> Root =>
      for {
        sdo <- req.as[SDOType]
        response <- TacticRepository.add(sdo) match {
          case Right(obj) => Created(obj.asJson)
          case Left(message) => Conflict(message.asJson)
        }
      } yield response

    case DELETE -> Root / id => TacticRepository.delete(id) match {
      case Right(obj) => Ok(obj.asJson)
      case Left(message) => NotFound(message.asJson)
    }
  }

}
