name := "CostsControl"

version := "0.1"

scalaVersion := "2.12.6"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http" % "10.1.1",
  "com.typesafe.akka" %% "akka-stream" % "2.5.12",
  "de.heikoseeberger" %% "akka-http-json4s" % "1.20.1",
  "org.json4s" %% "json4s-jackson" % "3.5.3",
  "com.typesafe.akka" %% "akka-http-testkit" % "10.1.1" % Test
)

scalacOptions := Seq(
  "-encoding",
  "utf8"
  // "-feature",
  // "-unchecked",
  // "-deprecation",
  // "-target:jvm-1.8",
  // "-Ymacro-debug-lite",
  //"-language:_",
  //"-Xexperimental"
)