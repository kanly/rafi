name := "rafi"

version := "0.1"

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.3.6",
  "com.typesafe.akka" %% "akka-camel" % "2.3.6",
  "com.typesafe.akka" %% "akka-remote" % "2.3.6",
  "org.apache.camel" % "camel-stream" % "2.10.3",
  "com.github.scala-incubator.io" %% "scala-io-core" % "0.4.3-1",
  "com.github.scala-incubator.io" %% "scala-io-file" % "0.4.3-1",
  //"org.scala-lang" % "jline" % "2.11.0-M3",
  "ch.qos.logback" % "logback-classic" % "1.1.2",
  "com.typesafe.akka" %% "akka-testkit" % "2.3.6" % "test",
  "org.scalatest" %% "scalatest" % "2.1.6" % "test")

scalacOptions += "-target:jvm-1.6"

lazy val common = (
  Project("common", file("common"))
    .settings(
      scalaVersion := "2.11.1",
      libraryDependencies ++= Seq(
        "com.typesafe.akka" %% "akka-actor" % "2.3.6",
        "com.typesafe.akka" %% "akka-remote" % "2.3.6",
        "com.github.scala-incubator.io" %% "scala-io-core" % "0.4.3-1",
        "com.github.scala-incubator.io" %% "scala-io-file" % "0.4.3-1",
        //"org.scala-lang" % "jline" % "2.11.0-M3",
        "ch.qos.logback" % "logback-classic" % "1.1.2",
        "com.typesafe.akka" %% "akka-testkit" % "2.3.6" % "test",
        "org.scalatest" %% "scalatest" % "2.1.6" % "test")
    )
  )

lazy val producer = (
  Project("producer", file("producer"))
    .dependsOn(common)
    .settings(
      scalaVersion := "2.11.1",
      libraryDependencies ++= Seq(
        "com.typesafe.akka" %% "akka-actor" % "2.3.6",
        "com.typesafe.akka" %% "akka-camel" % "2.3.6",
        "com.typesafe.akka" %% "akka-remote" % "2.3.6",
        "org.apache.camel" % "camel-stream" % "2.10.3",
        "com.github.scala-incubator.io" %% "scala-io-core" % "0.4.3-1",
        "com.github.scala-incubator.io" %% "scala-io-file" % "0.4.3-1",
        //"org.scala-lang" % "jline" % "2.11.0-M3",
        "ch.qos.logback" % "logback-classic" % "1.1.2",
        "com.typesafe.akka" %% "akka-testkit" % "2.3.6" % "test",
        "org.scalatest" %% "scalatest" % "2.1.6" % "test")
    )
  )

lazy val consumer = (
  Project("consumer", file("consumer"))
    .dependsOn(common)
    .settings(
      scalaVersion := "2.11.1",
      libraryDependencies ++= Seq(
        "com.typesafe.akka" %% "akka-actor" % "2.3.6",
        "com.typesafe.akka" %% "akka-remote" % "2.3.6",
        "com.github.scala-incubator.io" %% "scala-io-core" % "0.4.3-1",
        "com.github.scala-incubator.io" %% "scala-io-file" % "0.4.3-1",
        //"org.scala-lang" % "jline" % "2.11.0-M3",
        "ch.qos.logback" % "logback-classic" % "1.1.2",
        "com.typesafe.akka" %% "akka-testkit" % "2.3.6" % "test",
        "org.scalatest" %% "scalatest" % "2.1.6" % "test")
    )
  )
  