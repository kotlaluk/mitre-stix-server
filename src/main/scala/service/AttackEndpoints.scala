package org.example.mitrestixserver
package service

import repository.StixRepository

import cats.effect.IO
import com.kodekutters.stix.AttackPattern
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s.HttpRoutes
import org.http4s.circe.CirceEntityCodec.circeEntityEncoder
import org.http4s.dsl.io._


object AttackEndpoints {

  val endpoints = HttpRoutes.of[IO] {
    case GET -> Root => {
      val allAttacks = StixRepository.getAllByType(AttackPattern.`type`).asInstanceOf[List[AttackPattern]]
      Ok(allAttacks.asJson)
    }
  }

}
