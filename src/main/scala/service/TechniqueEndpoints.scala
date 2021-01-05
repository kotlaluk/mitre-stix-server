package org.example.mitrestixserver
package service

import repository.TechniquesRepository

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
    case GET -> Root => Ok(TechniquesRepository.findAllCurrent().asJson)

    case GET -> Root / id => TechniquesRepository.findByMitreId(id) match {
      case Some(entity) => Ok(entity.asJson)
      case None => NotFound()
    }

    case req @ POST -> Root =>
      for {
        stixObj <- req.as[StixType]
        response <- TechniquesRepository.add(stixObj) match {
          case Some(entity) => Created(entity.asJson)
          case None => Conflict("Object with the same ID or MITRE ID already exists!".asJson)
        }
      } yield response
  }

}
