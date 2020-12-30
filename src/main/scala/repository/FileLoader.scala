package org.example.mitrestixserver
package repository

import com.kodekutters.stix.Bundle
import play.api.libs.json.Json

import scala.io.Source
import scala.util.Using

class FileLoader(source: String) extends Loader {

  override def load(): Option[Bundle] = {
    Using(Source.fromFile(source, "UTF-8")) {jsonData =>
      Json.fromJson[Bundle](Json.parse(jsonData.mkString)).asOpt
    }.get
  }

}
