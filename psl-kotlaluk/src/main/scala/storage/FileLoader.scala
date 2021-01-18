package org.example.mitrestixserver
package storage

import com.kodekutters.stix.Bundle
import org.log4s.getLogger
import play.api.libs.json.Json

import scala.io.Source
import scala.util.{Failure, Success, Using}

class FileLoader(source: String) extends Loader {

  override def load(): Bundle = {
      Using(Source.fromFile(source, "UTF-8")) { jsonData =>
        Json.fromJson[Bundle](Json.parse(jsonData.mkString)).asOpt
      } match {
        case Success(data) => data match {
          case Some(bundle) => bundle
          case None => {
            getLogger.warn("Error while reading data source file")
            new Bundle()
          }
        }
        case Failure(_) => {
          getLogger.warn("Error while reading data source file")
          new Bundle()
        }
      }
  }

}
