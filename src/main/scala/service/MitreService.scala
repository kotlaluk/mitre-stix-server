package org.example.mitrestixserver
package service

import service.api.{SoftwareEndpoints, TacticEndpoints, TechniqueEndpoints}
import service.view.IndexView
import storage.{FileLoader, InMemStixStorage}

import com.kodekutters.stix.Bundle
import org.http4s.implicits.http4sKleisliResponseSyntaxOptionT
import org.http4s.server.Router


object MitreService {

  val loader = new FileLoader("enterprise-attack.json")
  implicit val storage = new InMemStixStorage(loader.load().getOrElse(new Bundle()))

  val mainRouter = Router(
    "/" -> IndexView.endpoints,
    "/api/techniques" -> TechniqueEndpoints.endpoints,
    "/api/tactics" -> TacticEndpoints.endpoints,
    "/api/software" -> SoftwareEndpoints.endpoints
  ).orNotFound

}
