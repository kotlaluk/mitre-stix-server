package org.example.mitrestixserver
package service.view

import repository.sdo.{TacticRepository, TechniqueRepository}
import utils.SDOUtils

import cats.effect.IO
import org.http4s.HttpRoutes
import org.http4s.dsl.io._
import org.http4s.twirl._
import play.api.libs.json.JsPath


class IndexView(tacticRepository: TacticRepository, techniqueRepository: TechniqueRepository) {

  val endpoints: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case GET -> Root => Ok(html.index(view))
  }

  val view: Seq[(String, Seq[ListItem])] = {
    tacticRepository.findAllCurrent().map(tactic => {
      val killChainPhase = tactic.getCustomProperty[String]("x_mitre_shortname").getOrElse("Undefined")
      val listItems = techniqueRepository.findCurrentWithoutSubtechniques().filter(_.kill_chain_phases.getOrElse(List())
        .exists(_.phase_name == killChainPhase)).map(sdo =>
          ListItem(sdo.mitreId, sdo.name, s"/techniques/${sdo.mitreId}")
        )
      (killChainPhase, listItems)
    })
  }

}

object IndexView {
  def apply(implicit tacticRepository: TacticRepository,
            techniqueRepository: TechniqueRepository
           ): IndexView = new IndexView(tacticRepository, techniqueRepository)
}
