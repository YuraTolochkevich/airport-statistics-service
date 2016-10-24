organization in ThisBuild := "com.airportstat"

name := "airports-statistics-service"

version := "1.0"

scalaVersion  := "2.11.7"


resolvers += "spray repo" at "http://repo.spray.io"

val sprayVersion = "1.3.3"
val akkaVersion = "2.4.8"

libraryDependencies ++= Seq(
    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "io.spray" %% "spray-caching" % sprayVersion,
    "io.spray" %% "spray-can" % sprayVersion,
    "io.spray" %% "spray-routing" % sprayVersion,
    "org.json4s" %% "json4s-native" % "3.3.0",
     "org.json4s" %% "json4s-jackson" % "3.3.0",
    "org.scalatest" %% "scalatest" % "2.2.5",
    "com.typesafe.akka" %% "akka-testkit" % akkaVersion,
    "io.spray" %% "spray-testkit" % sprayVersion
)

