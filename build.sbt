name := """dungeon-raider-api"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.10.4"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  ws,
  "postgresql" % "postgresql" % "9.1-901.jdbc4",
  "org.slf4j" % "slf4j-nop" % "1.6.4",
  "com.github.t3hnar" % "scala-bcrypt_2.10" % "2.3",
  "com.typesafe.slick" % "slick_2.10" % "2.1.0-M2",
  "com.typesafe.play" % "play-slick_2.10" % "0.6.0.1"
)
