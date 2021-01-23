name := "mitre-stix-server"

version := "0.1"

scalaVersion := "2.13.4"

idePackagePrefix := Some("org.example.mitrestixserver")

lazy val root = (project in file(".")).enablePlugins(SbtTwirl)

val http4sVersion = "0.21.14"

libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.1.3"

libraryDependencies ++= Seq(
  "org.http4s" %% "http4s-dsl" % http4sVersion,
  "org.http4s" %% "http4s-blaze-server" % http4sVersion,
  "org.http4s" %% "http4s-circe" % http4sVersion,
  "org.http4s" %% "http4s-twirl" % http4sVersion
)

libraryDependencies += "io.circe" %% "circe-generic" % "0.13.0"

libraryDependencies += "com.github.workingDog" %% "scalastix" % "1.1"