import AssemblyKeys._

name := "rjs4scala"

version := "0.1.1"

scalaVersion := "2.9.1"

scalacOptions += "-deprecation"

scalacOptions += "-unchecked"

seq(Revolver.settings: _*)

seq(jotSettings: _*)

seq(assemblySettings: _*)

resolvers += "Scala Tools Snapshots" at "http://scala-tools.org/repo-snapshots/"

crossScalaVersions := Seq("2.9.0", "2.9.0-1", "2.9.1", "2.9.1-1")

unmanagedClasspath in Compile += file("dummy")

//mainClass in assembly := Some("main.class")