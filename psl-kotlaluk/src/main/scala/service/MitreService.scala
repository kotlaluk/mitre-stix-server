package org.example.mitrestixserver
package service

import service.api._
import service.view._
import storage.{FileLoader, InMemStixStorage}

import com.kodekutters.stix.IntrusionSet
import org.example.mitrestixserver.repository.sdo.GroupRepository
import org.http4s.implicits.http4sKleisliResponseSyntaxOptionT
import org.http4s.server.Router
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s.circe.CirceEntityCodec.{circeEntityDecoder, circeEntityEncoder}

object MitreService {

  val file = "data/enterprise-attack.json"
  implicit val storage = new InMemStixStorage(new FileLoader(file).load())

  implicit val repo = GroupRepository

  val mainRouter = Router(
    "/" -> IndexView.endpoints,
    "/techniques" -> TechniqueView.endpoints,
    "/software" -> SoftwareView.endpoints,
    "/groups" -> GroupView.endpoints,
    "/mitigations" -> MitigationView.endpoints,
    "/api/tactics" -> TacticApi.endpoints,
    "/api/techniques" -> TechniqueApi.endpoints,
    "/api/software" -> SoftwareApi.endpoints,
    "/api/groups" -> ApiEndpoints[IntrusionSet].endpoints,
    "/api/mitigations" -> MitigationApi.endpoints
  ).orNotFound

}
