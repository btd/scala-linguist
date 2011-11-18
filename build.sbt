name := "scala-linguist"

organization := "com.github.btd"

version := "1.0"

scalaVersion := "2.9.1"

libraryDependencies += "org.scala-tools.testing" %% "specs" % "1.6.9" % "test"

publishTo := Some(Resolver.file("Github Pages", Path.userHome / "projects" / "maven2" asFile) (Patterns(true, Resolver.mavenStyleBasePattern)))

publishMavenStyle := true