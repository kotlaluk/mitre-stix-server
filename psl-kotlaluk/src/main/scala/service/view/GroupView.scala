package org.example.mitrestixserver
package service.view

import repository.MitreError
import repository.sdo.{GroupRepository, SoftwareRepository, TechniqueRepository}
import repository.sro.RelationshipRepository
import utils.SDOUtils


object GroupView extends ViewEndpoints {

  override val endpoint: String = "groups"

  val listView: (String, Seq[ListItem]) = {
    val list = GroupRepository.findAllCurrent().sortBy(_.mitreId).map(
      sdo => ListItem(sdo.mitreId, sdo.name, s"/${endpoint}/${sdo.mitreId}")
    )
    ("Groups", list)
  }

  def detailView(id: String): Either[MitreError, DetailItem] = {
    GroupRepository.findByMitreId(id) match {
      case Left(error) => Left(error)
      case Right(group) => {
        val description = group.description.getOrElse("")
        val techniques = RelationshipRepository.findUses(group).collect(rel =>
          TechniqueRepository.findById(rel.target_ref) match {
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
