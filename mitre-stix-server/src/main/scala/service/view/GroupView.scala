package org.example.mitrestixserver
package service.view

import repository.MitreError
import repository.sdo.{GroupRepository, TechniqueRepository}
import repository.sro.RelationshipRepository
import utils.SDOUtils


class GroupView(repo: GroupRepository,
                techniqueRepository: TechniqueRepository,
                relationshipRepository: RelationshipRepository) extends ViewEndpoints {

  override val endpoint: String = "groups"

  val listView: (String, Seq[ListItem]) = {
    val list = repo.findAllCurrent().sortBy(_.mitreId).map(
      sdo => ListItem(sdo.mitreId, sdo.name, s"/${endpoint}/${sdo.mitreId}")
    )
    ("Groups", list)
  }

  def detailView(id: String): Either[MitreError, DetailItem] = {
    repo.findByMitreId(id) match {
      case Left(error) => Left(error)
      case Right(group) => {
        val description = group.description.getOrElse("")
        val techniques = relationshipRepository.findUses(group).collect(rel =>
          techniqueRepository.findById(rel.target_ref) match {
            case Right(technique) => ListItem(technique.mitreId, technique.name, s"/techniques/${technique.mitreId}")
          }
        )
        val stringProps = Map("Description" -> beautify(description))
        val listProps = Map("Uses techniques" -> techniques.sortBy(_.mitreId))
        Right(DetailItem("Group Detail", id, group.name, stringProps, listProps))
      }
    }
  }
}

object GroupView {
  def apply(implicit repo: GroupRepository,
            techniqueRepository: TechniqueRepository,
            relationshipRepository: RelationshipRepository
           ): GroupView = new GroupView(repo, techniqueRepository, relationshipRepository)
}
