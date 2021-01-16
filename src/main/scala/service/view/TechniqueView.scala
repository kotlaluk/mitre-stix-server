package org.example.mitrestixserver
package service.view

import repository.MitreError
import repository.sdo.{SoftwareRepository, TechniqueRepository}
import repository.sro.RelationshipRepository
import utils.SDOUtils

import play.api.libs.json.JsPath


object TechniqueView extends ViewEndpoints {

  override val endpoint: String = "techniques"

  val listView: (String, Seq[ListItem]) = {
    val list = TechniqueRepository.findCurrentWithoutSubtechniques().sortBy(_.mitreId).map(
      sdo => ListItem(sdo.mitreId, sdo.name, s"/${endpoint}/${sdo.mitreId}")
    )
    ("Techniques", list)
  }

  def detailView(id: String): Either[MitreError, DetailItem] = {
    TechniqueRepository.findByMitreId(id) match {
      case Left(error) => Left(error)
      case Right(technique) => {
        val description = technique.description.getOrElse("")
        val detection = technique.getCustomProperty("x_mitre_detection", JsPath.read[String]).getOrElse("")
        val software = RelationshipRepository.findUsedBy(technique).collect(rel =>
          SoftwareRepository.findById(rel.source_ref) match {
            case Right(sw) => ListItem(sw.mitreId, sw.name, s"/software/${sw.mitreId}")
          }
        )
        val subtechniques = RelationshipRepository.findSubtechniques(technique).collect(rel =>
          TechniqueRepository.findById(rel.source_ref) match {
            case Right(subtechnique) => ListItem(subtechnique.mitreId, subtechnique.name, s"/${endpoint}/${subtechnique.mitreId}")
          }
        )
        val stringProps = Map("Description" -> description, "Detection" -> detection)
        val listProps = Map("Subtechniques" -> subtechniques.sortBy(_.mitreId), "Used by software" -> software.sortBy(_.mitreId))
        Right(DetailItem("Technique Detail", id, technique.name, stringProps, listProps))
      }
    }
  }
}
