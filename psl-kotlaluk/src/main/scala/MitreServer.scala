package org.example.mitrestixserver

import service.MitreService
import storage.{FileLoader, InMemStixStorage}

import cats.effect._
import org.http4s.server.blaze._

import scala.concurrent.ExecutionContext.global


object MitreServer extends IOApp {

  val file = "data/enterprise-attack.json"
  implicit val storage = new InMemStixStorage(new FileLoader(file).load())

  def run(args: List[String]): IO[ExitCode] =
    BlazeServerBuilder[IO](global)
      .bindHttp(8080, "localhost")
      .withHttpApp(new MitreService().mainRouter)
      .serve
      .compile
      .drain
      .as(ExitCode.Success)
}