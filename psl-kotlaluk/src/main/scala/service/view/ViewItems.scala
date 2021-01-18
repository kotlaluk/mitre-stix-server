package org.example.mitrestixserver
package service.view

case class ListItem(mitreId: String, name: String, link: String)

case class DetailItem(title: String, mitreId: String, name: String,
                      stringProps: Map[String, String], listProps: Map[String, Seq[ListItem]])
