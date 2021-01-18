package org.example.mitrestixserver
package service.view

import repository.MitreError
import repository.sdo.{GroupRepository, MitigationRepository, SoftwareRepository, TechniqueRepository}
import repository.sro.RelationshipRepository
import utils.SDOUtils

import play.api.libs.json.JsPath


class TechniqueView(groupRepository: GroupRepository) extends ViewEndpoints {

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
        val groups = RelationshipRepository.findUsedBy(technique).collect(rel =>
          groupRepository.findById(rel.source_ref) match {
            case Right(group) => ListItem(group.mitreId, group.name, s"/groups/${group.mitreId}")
          }
        )
        val subtechniques = RelationshipRepository.findSubtechniques(technique).collect(rel =>
          TechniqueRepository.findById(rel.source_ref) match {
            case Right(subtechnique) => ListItem(subtechnique.mitreId, subtechnique.name, s"/${endpoint}/${subtechnique.mitreId}")
          }
        )
        val subtechniqueOf = RelationshipRepository.findSubtechniqueOf(technique).collect(rel =>
          TechniqueRepository.findById(rel.target_ref) match {
            case Right(technique) => ListItem(technique.mitreId, technique.name, s"/${endpoint}/${technique.mitreId}")
          }
        )
        val mitigatedBy = RelationshipRepository.findMitigatedBy(technique).collect(rel =>
          MitigationRepository.findById(rel.source_ref) match {
            case Right(mitigation) => ListItem(mitigation.mitreId, mitigation.name, s"/mitigations/${mitigation.mitreId}")
          }
        )
        val stringProps = Map("Description" -> beautify(description),
                              "Detection" -> beautify(detection))
        val listProps = Map("Subtechnique of" -> subtechniqueOf.sortBy(_.mitreId),
                            "Subtechniques" -> subtechniques.sortBy(_.mitreId),
                            "Mitigations" -> mitigatedBy.sortBy(_.mitreId),
                            "Used by software" -> software.sortBy(_.mitreId),
                            "Used by groups" -> groups.sortBy(_.mitreId))
        Right(DetailItem("Technique Detail", id, technique.name, stringProps, listProps))
      }
    }
  }
}

object TechniqueView {
  def apply(implicit groupRepository: GroupRepository): TechniqueView = new TechniqueView(groupRepository)
}
