package org.example.mitrestixserver
package service.view

import repository.MitreError
import repository.sdo.{SoftwareRepository, TacticRepository, TechniqueRepository}
import repository.sro.RelationshipRepository
import utils.SDOUtils

import play.api.libs.json.JsPath


case class ListItem(mitreId: String, name: String, link: String)

case class DetailItem(mitreId: String, name: String, subtechniques: Seq[String], description: String, detection: String, relations: Seq[String])


object ViewGenerator {

  val indexView: Seq[(String, Seq[ListItem])] = {
    TacticRepository.findAllCurrent().map(tactic => {
      val killChainPhase = tactic.getCustomProperty[String]("x_mitre_shortname", JsPath.read[String]).getOrElse("Undefined")
      val listItems = TechniqueRepository.findCurrentWithoutSubtechniques().filter(_.kill_chain_phases.getOrElse(List()).exists(_.phase_name == killChainPhase)).map(sdo =>
        ListItem(sdo.mitreId, sdo.name, s"/techniques/${sdo.mitreId}")
      )
      Tuple2(killChainPhase, listItems)
    })
  }

  def listView(): Seq[ListItem] = {
    TechniqueRepository.findCurrentWithoutSubtechniques().map(sdo => ListItem(sdo.mitreId, sdo.name, s"/techniques/${sdo.mitreId}"))
  }

  def detailView(id: String): Either[MitreError, DetailItem] = {
    TechniqueRepository.findByMitreId(id) match {
      case Left(error) => Left(error)
      case Right(technique) => {
        val description = technique.description.getOrElse("")
        val detection = technique.getCustomProperty("x_mitre_detection", JsPath.read[String]).getOrElse("")
        val software = RelationshipRepository.findUsedBy(technique).collect(rel =>
          SoftwareRepository.findById(rel.source_ref) match {
            case Right(sw) => sw.name
          }
        )
        val subtechniques = RelationshipRepository.findSubtechniques(technique).collect(rel =>
          TechniqueRepository.findById(rel.source_ref) match {
            case Right(subtechnique) => subtechnique.name
          }
        )
        Right(DetailItem(id, technique.name, subtechniques, description, detection, software))
      }
    }
  }
}
