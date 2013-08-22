name := "scalatest-extra"

version := "0.2-SNAPSHOT"

scalaVersion := "2.10.2"

libraryDependencies += "org.scalatest" %% "scalatest" % "2.0.M5b"

libraryDependencies += "org.scalacheck" %% "scalacheck" % "1.10.1"

libraryDependencies += "junit" % "junit" % "4.11"

libraryDependencies += "org.spire-math" %% "spire" % "0.5.0"

libraryDependencies += "org.apache.commons" % "commons-io" % "1.3.2"

organization := "emchristiansen"

publishMavenStyle := false

publishTo := Some(Resolver.file("file", new File("./releases")))

scalacOptions ++= Seq(
  "-optimize",
  "-unchecked",
  "-deprecation",
  "-feature",
  "-language:implicitConversions",
  // "-language:reflectiveCalls",
  "-language:postfixOps"
)