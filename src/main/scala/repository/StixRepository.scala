package org.example.mitrestixserver
package repository

import com.kodekutters.stix.{Bundle, StixObj}


object StixRepository {

  private var stixData = new Bundle()

  def initialize(loader: Loader): Unit = {
    stixData = loader.load().get
  }

  def getAllByType(stixType: String): Seq[StixObj] = {
    stixData.objects.filter(_.`type` == stixType).toList
  }

}



