package org.example.mitrestixserver
package service.view

import repository.MitreError
import repository.sdo.{SoftwareRepository, TechniqueRepository}
import repository.sro.RelationshipRepository
import utils.SDOUtils


object SoftwareView extends ViewEndpoints {

  override val endpoint: String = "software"

  val listView: (String, Seq[ListItem]) = {
    val list = SoftwareRepository.findAllCurrent().sortBy(_.mitreId).map(
      sdo => ListItem(sdo.mitreId, sdo.name, s"/${endpoint}/${sdo.mitreId}")
    )
    ("Software", list)
  }

  def detailView(id: String): Either[MitreError, DetailItem] = {
    SoftwareRepository.findByMitreId(id) match {
      case Left(error) => Left(error)
      case Right(sw) => {
        val description = sw.description.getOrElse("")
        val techniques = RelationshipRepository.findUses(sw).collect(rel =>
          TechniqueRepository.findById(rel.target_ref) match {
            case Right(sw) => ListItem(sw.mitreId, sw.name, s"/techniques/${sw.mitreId}")
          }
        )
        val stringProps = Map("Description" -> description)
        val listProps = Map("Used in techniques" -> techniques.sortBy(_.mitreId))
        Right(DetailItem("Software Detail", id, sw.name, stringProps, listProps))
      }
    }
  }
}
