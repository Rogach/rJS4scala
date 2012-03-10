import AssemblyKeys._

name := "rjs4scala"

version := "0.1"

scalaVersion := "2.9.1"

scalacOptions += "-deprecation"

scalacOptions += "-unchecked"

seq(Revolver.settings: _*)

seq(jotSettings: _*)

seq(assemblySettings: _*)

resolvers += "Scala Tools Snapshots" at "http://scala-tools.org/repo-snapshots/"

libraryDependencies ++= Seq(
  "org.scalaz" %% "scalaz-core" % "6.0.4"
)

//mainClass in assembly := Some("main.class")