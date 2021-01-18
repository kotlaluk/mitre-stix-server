package org.example.mitrestixserver
package service.api

import repository.sdo.SoftwareRepository

import cats.effect.IO
import com.kodekutters.stix.Tool
import org.http4s.HttpRoutes
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s.circe.CirceEntityCodec.{circeEntityDecoder, circeEntityEncoder}
import org.http4s.dsl.io._

object SoftwareApi {

  type SDOType = Tool

  val endpoints: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case GET -> Root => Ok(SoftwareRepository.findAllCurrent().asJson)

    case GET -> Root / id => SoftwareRepository.findByMitreId(id) match {
      case Right(obj) => Ok(obj.asJson)
      case Left(message) => NotFound(message.asJson)
    }

    case req @ POST -> Root =>
      for {
        sdo <- req.as[SDOType]
        response <- SoftwareRepository.add(sdo) match {
          case Right(obj) => Created(obj.asJson)
          case Left(message) => Conflict(message.asJson)
        }
      } yield response

    case req @ PUT -> Root / id =>
      for {
        sdo <- req.as[SDOType]
        response <- SoftwareRepository.update(id, sdo) match {
          case Right(obj) => Ok(obj.asJson)
          case Left(message) => BadRequest(message.asJson)
        }
      } yield response

    case DELETE -> Root / id => SoftwareRepository.delete(id) match {
      case Right(obj) => Ok(obj.asJson)
      case Left(message) => NotFound(message.asJson)
    }
  }

}
