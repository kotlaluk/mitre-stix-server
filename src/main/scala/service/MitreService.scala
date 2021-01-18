package org.example.mitrestixserver
package service

import service.api.{GroupApi, SoftwareApi, TacticApi, TechniqueApi}
import service.view.{GroupView, IndexView, SoftwareView, TechniqueView}
import storage.{FileLoader, InMemStixStorage}

import com.kodekutters.stix.Bundle
import org.http4s.implicits.http4sKleisliResponseSyntaxOptionT
import org.http4s.server.Router


object MitreService {

  val loader = new FileLoader("enterprise-attack.json")
  implicit val storage = new InMemStixStorage(loader.load().getOrElse(new Bundle()))

  val mainRouter = Router(
    "/" -> IndexView.endpoints,
    "/techniques" -> TechniqueView.endpoints,
    "/software" -> SoftwareView.endpoints,
    "/groups" -> GroupView.endpoints,
    "/api/tactics" -> TacticApi.endpoints,
    "/api/techniques" -> TechniqueApi.endpoints,
    "/api/software" -> SoftwareApi.endpoints,
    "/api/groups" -> GroupApi.endpoints
  ).orNotFound

}
