name := "rafi"

version := "0.1"

scalaVersion := "2.11.1"

scalacOptions += "-target:jvm-1.6"

def rafiModule(name: String): Project = Project(name, file(name)).
  settings(
    version := "0.1.1",
    organization := "org.kanly",
    scalaVersion := "2.11.1",
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-actor" % "2.3.6",
      "com.typesafe.akka" %% "akka-slf4j" % "2.3.6",
      "com.typesafe.akka" %% "akka-remote" % "2.3.6",
      "com.github.scala-incubator.io" %% "scala-io-core" % "0.4.3-1",
      "com.github.scala-incubator.io" %% "scala-io-file" % "0.4.3-1",
      "ch.qos.logback" % "logback-classic" % "1.1.2",
      "com.typesafe.akka" %% "akka-testkit" % "2.3.6" % "test",
      "org.scalatest" %% "scalatest" % "2.1.6" % "test"
    ),
    licenses += ("agpl-3.0", url("https://www.gnu.org/licenses/agpl-3.0.html")),
    assemblyJarName in assembly := s"rafi-$name-${version.value}.jar"
  )

lazy val common = rafiModule("common")

lazy val producer = rafiModule("producer")
  .dependsOn(common)
  .settings(
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-camel" % "2.3.6",
      "org.apache.camel" % "camel-stream" % "2.10.3"
    ),
    mainClass in assembly := Some("producer.StreamerApp")
  )

lazy val consumer = rafiModule("consumer")
  .dependsOn(common)
  .settings(
    mainClass in assembly := Some("consumer.ConsumerApp")
  )
  