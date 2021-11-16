name := "first-steps"

version := "0.1"

scalaVersion := "2.13.4"

lazy val AkkaVersion = "2.6.10"
lazy val AkkaHttpVersion = "10.2.1"
lazy val AkkaManagementVersion = "1.0.9"
lazy val AkkaEnhancementsVersion = "1.1.16"
lazy val slf4jVersion = "1.7.30"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion,

  // akka Http
  "com.typesafe.akka" %% "akka-http" % AkkaHttpVersion,
  "com.typesafe.akka" %% "akka-stream" % AkkaVersion,

  // Json
  "com.typesafe.play" %% "play-json" % "2.9.1",
  "com.typesafe.akka" %% "akka-http-spray-json" % AkkaHttpVersion,

  // akka logs
  "com.typesafe.akka" %% "akka-slf4j" % AkkaVersion,

  // Test
  "org.scalactic" %% "scalactic" % "3.2.2",
  "com.typesafe.akka" %% "akka-testkit" % AkkaVersion % Test,
  "org.scalatest" %% "scalatest" % "3.2.2" % "test",

  // akka Cluster
  "com.typesafe.akka" %% "akka-cluster" % AkkaVersion,
  "com.typesafe.akka" %% "akka-cluster-tools" % AkkaVersion,
  "com.typesafe.akka" %% "akka-cluster-sharding"% AkkaVersion,
  "com.typesafe.akka" %% "akka-cluster-typed" % AkkaVersion,

  // akka remote
  "com.typesafe.akka" %% "akka-remote" % AkkaVersion,

  // akka managment This repository contains interfaces to inspect, interact and manage various Parts of Akka, primarily Akka Cluster.
  "com.lightbend.akka.management" %% "akka-management" % AkkaManagementVersion,
  "com.lightbend.akka.management" %% "akka-management-cluster-http" % AkkaManagementVersion,


  "org.scala-lang.modules" %% "scala-parser-combinators" % "1.1.2",
  "ch.qos.logback" % "logback-classic" % "1.1.3" % Runtime
)