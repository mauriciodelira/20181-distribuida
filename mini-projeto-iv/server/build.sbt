name := """enterplay-api"""

version := "2.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.5"

libraryDependencies += guice

libraryDependencies += specs2 % Test

// Backend Dependecies
libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play-slick" %  "3.0.3",
  "org.postgresql" % "postgresql" % "9.4-1200-jdbc41",
  "com.netaporter" %% "scala-uri" % "0.4.16"
)

// Template Dependencies
lazy val templateDependencies = Seq(
)

libraryDependencies ++= templateDependencies // ++ angularDependencies

resolvers += "Typesafe Releases" at "http://repo.typesafe.com/typesafe/maven-releases/"

//LessKeys.compress in Assets := true
//
//pipelineStages := Seq(digest, gzip)
//
//includeFilter in (Assets, LessKeys.less) := "*.less"
//
//routesGenerator := InjectedRoutesGenerator