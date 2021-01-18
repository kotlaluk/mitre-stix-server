package org.example.mitrestixserver
package service

import service.api._
import service.view._
import storage.{FileLoader, InMemStixStorage}

import org.http4s.implicits.http4sKleisliResponseSyntaxOptionT
import org.http4s.server.Router


object MitreService {

  val file = "data/enterprise-attack.json"
  implicit val storage = new InMemStixStorage(new FileLoader(file).load())

  val mainRouter = Router(
    "/" -> IndexView.endpoints,
    "/techniques" -> TechniqueView.endpoints,
    "/software" -> SoftwareView.endpoints,
    "/groups" -> GroupView.endpoints,
    "/mitigations" -> MitigationView.endpoints,
    "/api/tactics" -> TacticApi.endpoints,
    "/api/techniques" -> TechniqueApi.endpoints,
    "/api/software" -> SoftwareApi.endpoints,
    "/api/groups" -> GroupApi.endpoints,
    "/api/mitigations" -> MitigationApi.endpoints
  ).orNotFound

}
