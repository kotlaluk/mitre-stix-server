package org.example.mitrestixserver
package service

import repository.SoftwareRepository

import cats.effect.IO
import com.kodekutters.stix.Tool
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s.HttpRoutes
import org.http4s.circe.CirceEntityCodec.{circeEntityDecoder, circeEntityEncoder}
import org.http4s.dsl.io._


object SoftwareEndpoints {

  type StixType = Tool

  val endpoints: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case GET -> Root => Ok(SoftwareRepository.findAllCurrent().asJson)

    case GET -> Root / id => SoftwareRepository.findByMitreId(id) match {
      case Right(entity) => Ok(entity.asJson)
      case Left(message) => NotFound(message.asJson)
    }

    case req @ POST -> Root =>
      for {
        stixObj <- req.as[StixType]
        response <- SoftwareRepository.add(stixObj) match {
          case Right(entity) => Created(entity.asJson)
          case Left(message) => Conflict(message.asJson)
        }
      } yield response

    case req @ PUT -> Root / id =>
      for {
        stixObj <- req.as[StixType]
        response <- SoftwareRepository.update(id, stixObj) match {
          case Right(entity) => Ok(entity.asJson)
          case Left(message) => BadRequest(message.asJson)
        }
      } yield response

    case DELETE -> Root / id => SoftwareRepository.delete(id) match {
      case Right(entity) => Ok(entity.asJson)
      case Left(message) => NotFound(message.asJson)
    }
  }

}
