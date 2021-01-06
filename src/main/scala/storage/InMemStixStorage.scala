package org.example.mitrestixserver
package storage

import com.kodekutters.stix.{Bundle, StixObj}

class InMemStixStorage(stixData: Bundle) extends StixStorage {

  def create(stixObj: StixObj): Option[StixObj] = {
    stixData.objects.find(_.id == stixObj.id) match {
      case Some(_) => None
      case None => {
        stixData.objects.addOne(stixObj)
        Some(stixObj)
      }
    }
  }

  def readAll(): Seq[StixObj] = stixData.objects.toSeq

  def read(filter: StixObj => Boolean): Seq[StixObj] = stixData.objects.filter(filter).toSeq

  def update(stixObj: StixObj): Option[StixObj] = {
    stixData.objects.find(_.id == stixObj.id) match {
      case Some(obj) => {
        stixData.objects -= obj
        create(stixObj)
      }
      case None => None
    }
  }

  def delete(filter: StixObj => Boolean): Boolean = {
    val toDelete = stixData.objects.filter(filter).toSeq
    toDelete.foreach(stixData.objects -= _)
    toDelete.nonEmpty
  }

}
