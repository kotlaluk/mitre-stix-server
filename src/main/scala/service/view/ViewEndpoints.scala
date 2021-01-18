package org.example.mitrestixserver
package service.view

import repository.MitreError

import cats.effect.IO
import org.http4s.HttpRoutes
import org.http4s.dsl.io._
import org.http4s.twirl._


trait ViewEndpoints {

  val endpoint: String

  val listView: (String, Seq[ListItem])

  def detailView(id: String): Either[MitreError, DetailItem]

  val endpoints: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case GET -> Root => Ok(html.list(listView._1, listView._2))
    case GET -> Root / id => detailView(id) match {
      case Right(item) => Ok(html.detail(item))
      case Left(message) => NotFound(message.message)
    }
  }

  def beautify(s: String): String = {
    val patternLink = "\\[([^\\[\\]]+)\\](\\([^\\(\\)]+\\))".r
    val patternCitation = "\\(Citation: [^\\)]+\\)".r
    val s1 = patternLink.replaceAllIn(s, "$1")
    patternCitation.replaceAllIn(s1, "")
  }

}
