package org.example.mitrestixserver
package storage

import com.kodekutters.stix.{Bundle, StixObj}

class InMemStixStorage(stixData: Bundle) extends StixStorage {

  def create(stixObj: StixObj): Unit = stixData.objects.addOne(stixObj)

  def readAll(): Seq[StixObj] = stixData.objects.toSeq

  def read(filter: StixObj => Boolean): Seq[StixObj] = stixData.objects.filter(filter).toSeq

  def update(stixObj: StixObj): Unit = {
    val toDelete = read(_.id == stixObj.id).head
    stixData.objects -= toDelete
    create(stixObj)
  }

  def delete(filter: StixObj => Boolean): Unit = {
    val toDelete = stixData.objects.filter(filter).toSeq
    toDelete.foreach(stixData.objects -= _)
  }

}
