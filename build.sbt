name := "mitre-stix-server"

version := "0.1"

scalaVersion := "2.13.4"

idePackagePrefix := Some("org.example.mitrestixserver")

libraryDependencies ++= Seq(
  "org.http4s" %% "http4s-dsl" % "0.21.14",
  "org.http4s" %% "http4s-blaze-server" % "0.21.14",
  "org.http4s" %% "http4s-blaze-client" % "0.21.14",
  "org.http4s" %% "http4s-circe" % "0.21.14"
)

libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.1.3"