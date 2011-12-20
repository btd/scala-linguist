name := "scala-linguist"

organization := "com.github.btd"

version := "1.2"

scalaVersion := "2.9.1"

libraryDependencies ++= Seq("org.scala-tools.testing" %% "specs" % "1.6.9" % "test", 
							"com.ibm.icu" % "icu4j" % "4.8.1.1")

publishTo := Some(Resolver.file("Github Pages", Path.userHome / "projects" / "maven2" asFile) (Patterns(true, Resolver.mavenStyleBasePattern)))

publishMavenStyle := true