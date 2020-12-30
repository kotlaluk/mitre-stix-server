package org.example.mitrestixserver

import service.MitreService

import cats.effect._
import org.http4s.server.blaze._

import scala.concurrent.ExecutionContext.global


object MitreServer extends IOApp {

  def run(args: List[String]): IO[ExitCode] =
    BlazeServerBuilder[IO](global)
      .bindHttp(8080, "localhost")
      .withHttpApp(MitreService.routes)
      .serve
      .compile
      .drain
      .as(ExitCode.Success)
}