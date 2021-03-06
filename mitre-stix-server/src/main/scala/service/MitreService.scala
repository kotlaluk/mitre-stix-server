package org.example.mitrestixserver
package service

import repository.Repositories
import service.api._
import service.view._
import storage.StixStorage

import cats.effect.IO
import com.kodekutters.stix._
import io.circe.generic.auto._
import org.http4s.HttpApp
import org.http4s.circe.CirceEntityCodec.circeEntityDecoder
import org.http4s.implicits.http4sKleisliResponseSyntaxOptionT
import org.http4s.server.Router


class MitreService(implicit storage: StixStorage) extends Repositories {

  val mainRouter: HttpApp[IO] = Router(
    "/" -> IndexView.apply.endpoints,
    "/techniques" -> TechniqueView.apply.endpoints,
    "/software" -> SoftwareView.apply.endpoints,
    "/groups" -> GroupView.apply.endpoints,
    "/mitigations" -> MitigationView.apply.endpoints,
    "/api/tactics" -> ApiEndpoints[CustomStix].endpoints,
    "/api/techniques" -> ApiEndpoints[AttackPattern].endpoints,
    "/api/software" -> ApiEndpoints[Tool].endpoints,
    "/api/groups" -> ApiEndpoints[IntrusionSet].endpoints,
    "/api/mitigations" -> ApiEndpoints[CourseOfAction].endpoints
  ).orNotFound

}
