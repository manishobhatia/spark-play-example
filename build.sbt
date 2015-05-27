name := """ito-tools-service"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.5"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  ws,
  "net.sf.opencsv" % "opencsv" % "2.3",
  "org.scalaz" %% "scalaz-core" % "7.1.1",
  "org.apache.spark" %% "spark-core" % "1.3.0"
)

javaOptions ++= Seq("-Xms1024M", "-Xmx4096M", "-XX:MaxPermSize=256M")
