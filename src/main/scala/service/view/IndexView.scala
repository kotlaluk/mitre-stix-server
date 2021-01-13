package org.example.mitrestixserver
package service.view

import cats.effect.IO
import org.http4s.HttpRoutes
import org.http4s.dsl.io._
import org.http4s.twirl._


object IndexView {

  def createIndexView() = {
    val techniques1 = List("Tech1", "Tech2", "Tech3")
    val techniques2 = List("Tech1", "Tech2", "Tech3", "Tech4")
    val techniques3 = List("Tech1", "Tech2")
    val content = Map("T01" -> techniques1, "T02" -> techniques2, "T03" -> techniques3)
    content
  }

  val endpoints: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case GET -> Root => Ok(html.index(createIndexView()))
  }

}
