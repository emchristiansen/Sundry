import sbt._
import Keys._

object SundryBuild extends Build {
  def extraResolvers = Seq(
    resolvers ++= Seq(
      Resolver.sonatypeRepo("releases"),
      Resolver.sonatypeRepo("snapshots")))

  val projectName = "Sundry"
  val mavenName = "sundry"

  val publishSettings = Seq(
    name := mavenName,

    version := "0.1.2-SNAPSHOT",

    organization := "st.sparse",

    publishTo := {
      val nexus = "https://oss.sonatype.org/"
      if (version.value.trim.endsWith("SNAPSHOT"))
        Some("snapshots" at nexus + "content/repositories/snapshots")
      else
        Some("releases" at nexus + "service/local/staging/deploy/maven2")
    },

    publishMavenStyle := true,

    publishArtifact in Test := false,

    pomIncludeRepository := { _ => false },

    licenses := Seq("MIT" -> url("http://opensource.org/licenses/MIT")),

    homepage := Some(url("https://github.com/emchristiansen/Sundry")),

    pomExtra := (
      <scm>
        <url>git@github.com:emchristiansen/Sundry.git</url>
        <connection>scm:git:git@github.com:emchristiansen/Sundry.git</connection>
      </scm>
      <developers>
        <developer>
          <id>emchristiansen</id>
          <name>Eric Christiansen</name>
          <url>http://sparse.st</url>
        </developer>
      </developers>))

  val scalaVersionString = "2.10.3"

  def extraLibraryDependencies = Seq(
    libraryDependencies ++= Seq(
      "org.scala-lang" % "scala-reflect" % scalaVersionString,
      "org.scala-lang" % "scala-compiler" % scalaVersionString,
      "org.scalatest" %% "scalatest" % "2.0.RC1-SNAP6",
      "org.scalacheck" %% "scalacheck" % "1.10.1",
      "junit" % "junit" % "4.11",
      "org.spire-math" %% "spire" % "0.6.0",
      "org.scala-lang" %% "scala-pickling" % "0.8.0-SNAPSHOT",
      "org.apache.commons" % "commons-io" % "1.3.2" //      "com.typesafe.slick" %% "slick" % "1.0.1",
      //      "org.slf4j" % "slf4j-nop" % "1.6.4",
      //      "com.h2database" % "h2" % "1.3.166",
      //      "org.xerial" % "sqlite-jdbc" % "3.7.2",
      //      "org.jumpmind.symmetric.jdbc" % "mariadb-java-client" % "1.1.1",
      //       "joda-time" % "joda-time" % "2.3",
      //       "org.joda" % "joda-convert" % "1.5",
      //      "org.scala-lang" %% "scala-pickling" % "0.8.0-SNAPSHOT"
      ))

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
      //      "-language:reflectiveCalls",
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

  lazy val root = {
    val settings = moreSettings ++ Seq(fork := true)
    Project(id = projectName, base = file("."), settings = settings)
  }
}
