package org.example.mitrestixserver
package service.api

import repository.sdo.SDORepository

import cats.effect.IO
import com.kodekutters.stix.SDO
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s.circe.CirceEntityCodec.circeEntityEncoder
import org.http4s.dsl.io._
import org.http4s.{EntityDecoder, HttpRoutes}


class ApiEndpoints[SDOType <: SDO](repo: SDORepository[SDOType])(
  implicit encoder: io.circe.Encoder[SDOType],
  decoder: EntityDecoder[cats.effect.IO, SDOType]) {
  
  val endpoints: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case GET -> Root => Ok(repo.findAllCurrent().asJson)

    case GET -> Root / id => repo.findByMitreId(id) match {
      case Right(obj) => Ok(obj.asJson)
      case Left(message) => NotFound(message.asJson)
    }

    case req @ POST -> Root =>
      for {
        sdo <- req.as[SDOType]
        response <- repo.add(sdo) match {
          case Right(obj) => Created(obj.asJson)
          case Left(message) => Conflict(message.asJson)
        }
      } yield response

    case req @ PUT -> Root / id =>
      for {
        sdo <- req.as[SDOType]
        response <- repo.update(id, sdo) match {
          case Right(obj) => Ok(obj.asJson)
          case Left(message) => BadRequest(message.asJson)
        }
      } yield response

    case DELETE -> Root / id => repo.delete(id) match {
      case Right(obj) => Ok(obj.asJson)
      case Left(message) => NotFound(message.asJson)
    }
  }

}

object ApiEndpoints {
  
  def apply[SDOType <: SDO](implicit repo: SDORepository[SDOType],
                             encoder: io.circe.Encoder[SDOType],
                             decoder: EntityDecoder[cats.effect.IO, SDOType]
  ) = new ApiEndpoints(repo)

}
