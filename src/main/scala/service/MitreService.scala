package org.example.mitrestixserver
package service

import repository.{FileLoader, StixRepository}

import org.http4s.implicits.http4sKleisliResponseSyntaxOptionT
import org.http4s.server.Router


object MitreService {

  val loader = new FileLoader("enterprise-attack.json")

  StixRepository.initialize(loader)

  val routes = Router(
    "/attacks" -> AttackEndpoints.endpoints
  ).orNotFound

}
