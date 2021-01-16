package org.example.mitrestixserver
package service.view

import repository.sdo.{TacticRepository, TechniqueRepository}

import play.api.libs.json.JsPath


case class ListItem(id: String, name: String, link: String)


object ViewGenerator {

  private def createLink(endpoint: String, id: String = "") = {
    s"http://127.0.0.1:8080/$endpoint/$id"
  }

  val indexView: Seq[(String, Seq[ListItem])] = {
    TacticRepository.findAllCurrent().map(tactic => {
      val killChainPhase = TacticRepository.getCustomProperty[String](tactic, "x_mitre_shortname", JsPath.read[String]).getOrElse("Undefined")
      val listItems = TechniqueRepository.findCurrentWithoutSub().filter(_.kill_chain_phases.getOrElse(List()).exists(_.phase_name == killChainPhase)).map(sdo => {
        sdo.external_references.get(0).external_id match {
          case Some(mitreId) => ListItem(mitreId, sdo.name, createLink("techniques", mitreId))
          case None => ListItem("T0000", sdo.name, createLink("techniques"))
        }
      })
      Tuple2(killChainPhase, listItems)
    })
  }

  def listView(): Seq[ListItem] = {
    TechniqueRepository.findCurrentWithoutSub().map(sdo => {
      sdo.external_references.get(0).external_id match {
        case Some(mitreId) => ListItem(mitreId, sdo.name, createLink("techniques", mitreId))
        case None => ListItem("T0000", sdo.name, createLink("techniques"))
      }
    })
  }

  def detailView(): Unit = ???
}
