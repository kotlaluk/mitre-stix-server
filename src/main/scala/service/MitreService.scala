package org.example.mitrestixserver
package service

import storage.{FileLoader, InMemStixStorage}

import com.kodekutters.stix.Bundle
import org.http4s.implicits.http4sKleisliResponseSyntaxOptionT
import org.http4s.server.Router


object MitreService {

  val loader = new FileLoader("enterprise-attack.json")
  implicit val storage = new InMemStixStorage(loader.load().getOrElse(new Bundle()))

  val routes = Router(
    "/techniques" -> TechniqueEndpoints.endpoints,
    "/tactics" -> TacticEndpoints.endpoints,
    "/software" -> SoftwareEndpoints.endpoints
  ).orNotFound

}
