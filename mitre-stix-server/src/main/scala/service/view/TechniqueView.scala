package org.example.mitrestixserver
package service.view

import repository.MitreError
import repository.sdo.{GroupRepository, MitigationRepository, SoftwareRepository, TechniqueRepository}
import repository.sro.RelationshipRepository
import utils.SDOUtils


class TechniqueView(repo: TechniqueRepository,
                    groupRepository: GroupRepository,
                    mitigationRepository: MitigationRepository,
                    softwareRepository: SoftwareRepository,
                    relationshipRepository: RelationshipRepository) extends ViewEndpoints {

  override val endpoint: String = "techniques"

  val listView: (String, Seq[ListItem]) = {
    val list = repo.findCurrentWithoutSubtechniques().sortBy(_.mitreId).map(
      sdo => ListItem(sdo.mitreId, sdo.name, s"/${endpoint}/${sdo.mitreId}")
    )
    ("Techniques", list)
  }

  def detailView(id: String): Either[MitreError, DetailItem] = {
    repo.findByMitreId(id) match {
      case Left(error) => Left(error)
      case Right(technique) => {
        val description = technique.description.getOrElse("")
        val detection = technique.getCustomProperty[String]("x_mitre_detection").getOrElse("")
        val software = relationshipRepository.findUsedBy(technique).collect(rel =>
          softwareRepository.findById(rel.source_ref) match {
            case Right(sw) => ListItem(sw.mitreId, sw.name, s"/software/${sw.mitreId}")
          }
        )
        val groups = relationshipRepository.findUsedBy(technique).collect(rel =>
          groupRepository.findById(rel.source_ref) match {
            case Right(group) => ListItem(group.mitreId, group.name, s"/groups/${group.mitreId}")
          }
        )
        val subtechniques = relationshipRepository.findSubtechniques(technique).collect(rel =>
          repo.findById(rel.source_ref) match {
            case Right(subtechnique) => ListItem(subtechnique.mitreId, subtechnique.name, s"/${endpoint}/${subtechnique.mitreId}")
          }
        )
        val subtechniqueOf = relationshipRepository.findSubtechniqueOf(technique).collect(rel =>
          repo.findById(rel.target_ref) match {
            case Right(technique) => ListItem(technique.mitreId, technique.name, s"/${endpoint}/${technique.mitreId}")
          }
        )
        val mitigatedBy = relationshipRepository.findMitigatedBy(technique).collect(rel =>
          mitigationRepository.findById(rel.source_ref) match {
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
  def apply(implicit repo: TechniqueRepository,
            groupRepository: GroupRepository,
            mitigationRepository: MitigationRepository,
            softwareRepository: SoftwareRepository,
            relationshipRepository: RelationshipRepository
           ): TechniqueView = new TechniqueView(repo, groupRepository, mitigationRepository, softwareRepository, relationshipRepository)
}
