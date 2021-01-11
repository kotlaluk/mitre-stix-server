package org.example.mitrestixserver
package service

import repository.TechniqueRepository

import cats.effect.IO
import com.kodekutters.stix.AttackPattern
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s.HttpRoutes
import org.http4s.circe.CirceEntityCodec.{circeEntityDecoder, circeEntityEncoder}
import org.http4s.dsl.io._


object TechniqueEndpoints {

  type StixType = AttackPattern

  val endpoints: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case GET -> Root => Ok(TechniqueRepository.findAllCurrent().asJson)

    case GET -> Root / id => TechniqueRepository.findByMitreId(id) match {
      case Right(obj) => Ok(obj.asJson)
      case Left(message) => NotFound(message.asJson)
    }

    case req @ POST -> Root =>
      for {
        sdo <- req.as[StixType]
        response <- TechniqueRepository.add(sdo) match {
          case Right(obj) => Created(obj.asJson)
          case Left(message) => Conflict(message.asJson)
        }
      } yield response

    case req @ PUT -> Root / id =>
      for {
        sdo <- req.as[StixType]
        response <- TechniqueRepository.update(id, sdo) match {
          case Right(obj) => Ok(obj.asJson)
          case Left(message) => BadRequest(message.asJson)
        }
      } yield response

    case DELETE -> Root / id => TechniqueRepository.delete(id) match {
      case Right(obj) => Ok(obj.asJson)
      case Left(message) => NotFound(message.asJson)
    }
  }

}
