import sbt._

object RafiBuild extends Build {
  lazy val root = Project(id = "rafi", base = file(".")) aggregate(producer, consumer)

  lazy val producer = Project(id = "producer", base = file("producer")) dependsOn common

  lazy val consumer = Project(id = "consumer", base = file("consumer")) dependsOn common

  lazy val common = Project(id = "common", base = file("common"))
}