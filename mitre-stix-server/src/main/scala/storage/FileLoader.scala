package org.example.mitrestixserver
package storage

import com.kodekutters.stix.Bundle
import org.log4s.getLogger
import play.api.libs.json.Json

import scala.io.Source
import scala.util.Using


class FileLoader(source: String) extends Loader {

  override def load(): Bundle = {
      Using(Source.fromFile(source, "UTF-8")) { jsonData =>
        Json.fromJson[Bundle](Json.parse(jsonData.mkString)).asOpt
      }.toOption.flatten match {
        case Some(bundle) => bundle
        case None => {
          getLogger.warn("Error while reading data source file")
          new Bundle()
        }
      }
  }

}
