import sbt._
import Keys._

//import sbtassembly.Plugin._
//import AssemblyKeys._
//
//import com.typesafe.sbt.SbtStartScript

object ScalaTestExtraBuild extends Build {
  def extraResolvers = Seq(
    resolvers ++= Seq(
      "Sonatype OSS Releases" at "http://oss.sonatype.org/content/repositories/releases/",
      "Sonatype OSS Snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/",
      "Local Maven Repository" at Path.userHome.asFile.toURI.toURL + "/.m2/repository",
      Resolver.url("emchristiansen-scalatest-extra", url("https://raw.github.com/emchristiansen/scalatest-extra/master/releases"))(Patterns("[organisation]/[module]/[revision]/[artifact]-[revision].[ext]"))))

  val publishSettings = Seq(
    organization := "emchristiansen",
    publishMavenStyle := false,
    publishTo := Some(Resolver.file("file", new File("./releases"))),
    version := "0.2-SNAPSHOT")

  val scalaVersionString = "2.10.2"

  def extraLibraryDependencies = Seq(
    libraryDependencies ++= Seq(
      "org.scalatest" %% "scalatest" % "2.0.M5b",
      "org.scalacheck" %% "scalacheck" % "1.10.1",
      "junit" % "junit" % "4.11",
      "org.spire-math" %% "spire" % "0.5.0",
      "org.apache.commons" % "commons-io" % "1.3.2",
      "com.typesafe.slick" %% "slick" % "1.0.1",
      "org.slf4j" % "slf4j-nop" % "1.6.4",
      "com.h2database" % "h2" % "1.3.166",
      "org.scala-lang" %% "scala-pickling" % "0.8.0-SNAPSHOT"))

  def updateOnDependencyChange = Seq(
    watchSources <++= (managedClasspath in Test) map { cp => cp.files })

  def scalaSettings = Seq(
    scalaVersion := scalaVersionString,
    scalacOptions ++= Seq(
      "-optimize",
      "-unchecked",
      "-deprecation",
      "-feature",
      "-language:implicitConversions",
      "-language:existentials",
      "-language:reflectiveCalls",
      "-language:postfixOps"))

  def moreSettings =
    Project.defaultSettings ++
      extraResolvers ++
      extraLibraryDependencies ++
      scalaSettings ++
      //      assemblySettings ++
      //      SbtStartScript.startScriptForJarSettings ++
      updateOnDependencyChange ++
      publishSettings

  val projectName = "scalatest-extra"
  lazy val root = {
    val settings = moreSettings ++ Seq(name := projectName, fork := true)
    Project(id = projectName, base = file("."), settings = settings)
  }
}
