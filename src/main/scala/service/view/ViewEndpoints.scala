package org.example.mitrestixserver
package service.view

import service.view.ViewGenerator.{detailView, indexView, listView}

import cats.effect.IO
import org.http4s.HttpRoutes
import org.http4s.dsl.io._
import org.http4s.twirl._


object ViewEndpoints {

  val endpoints: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case GET -> Root => Ok(html.index(indexView))
    case GET -> Root / "techniques" => Ok(html.list("Techniques", listView()))
    case GET -> Root / "techniques" / id => detailView(id) match {
      case Right(detail) => Ok(html.detail("Technique Detail", detail))
      case Left(message) => NotFound(message.message)
    }
  }

}
