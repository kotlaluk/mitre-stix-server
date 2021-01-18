package org.example.mitrestixserver
package service

import repository.sdo.Repositories
import service.api._
import service.view._
import storage.StixStorage

import com.kodekutters.stix.IntrusionSet
import io.circe.generic.auto._
import org.http4s.circe.CirceEntityCodec.circeEntityDecoder
import org.http4s.implicits.http4sKleisliResponseSyntaxOptionT
import org.http4s.server.Router

class MitreService(implicit storage: StixStorage) extends Repositories {

  val mainRouter = Router(
    "/" -> IndexView.endpoints,
    "/techniques" -> TechniqueView.apply.endpoints,
    "/software" -> SoftwareView.endpoints,
    "/groups" -> GroupView.apply.endpoints,
    "/mitigations" -> MitigationView.endpoints,
    "/api/tactics" -> TacticApi.endpoints,
    "/api/techniques" -> TechniqueApi.endpoints,
    "/api/software" -> SoftwareApi.endpoints,
    "/api/groups" -> ApiEndpoints[IntrusionSet].endpoints,
    "/api/mitigations" -> MitigationApi.endpoints
  ).orNotFound

}
