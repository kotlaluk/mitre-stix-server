package org.example.mitrestixserver
package service.view

import repository.MitreError
import repository.sdo.{MitigationRepository, TechniqueRepository}
import repository.sro.RelationshipRepository
import utils.SDOUtils


class MitigationView(repo: MitigationRepository,
                     techniqueRepository: TechniqueRepository,
                     relationshipRepository: RelationshipRepository) extends ViewEndpoints {

  override val endpoint: String = "mitigations"

  val listView: (String, Seq[ListItem]) = {
    val list = repo.findAllCurrent().sortBy(_.mitreId).map(
      sdo => ListItem(sdo.mitreId, sdo.name, s"/${endpoint}/${sdo.mitreId}")
    )
    ("Mitigations", list)
  }

  def detailView(id: String): Either[MitreError, DetailItem] = {
    repo.findByMitreId(id) match {
      case Left(error) => Left(error)
      case Right(mitigation) => {
        val description = mitigation.description.getOrElse("")
        val techniques = relationshipRepository.findMitigates(mitigation).collect(rel =>
          techniqueRepository.findById(rel.target_ref) match {
            case Right(technique) => ListItem(technique.mitreId, technique.name, s"/techniques/${technique.mitreId}")
          }
        )
        val stringProps = Map("Description" -> beautify(description))
        val listProps = Map("Mitigates techniques" -> techniques.sortBy(_.mitreId))
        Right(DetailItem("Mitigation Detail", id, mitigation.name, stringProps, listProps))
      }
    }
  }
}

object MitigationView {
  def apply(implicit repo: MitigationRepository,
            techniqueRepository: TechniqueRepository,
            relationshipRepository: RelationshipRepository
           ): MitigationView = new MitigationView(repo, techniqueRepository, relationshipRepository)
}