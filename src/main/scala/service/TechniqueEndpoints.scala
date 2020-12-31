package org.example.mitrestixserver
package service

import dao.Techniques

import cats.effect.IO
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s.HttpRoutes
import org.http4s.circe.CirceEntityCodec.circeEntityEncoder
import org.http4s.dsl.io._


object TechniqueEndpoints {

  val endpoints: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case GET -> Root => Ok(Techniques.findAll(true).asJson)

    case GET -> Root / id => Techniques.findByMitreId(id) match {
      case Some(entity) => Ok(entity.asJson)
      case None => NotFound()
    }
  }

}