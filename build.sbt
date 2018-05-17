name := "CostsControl"

version := "0.1"

scalaVersion := "2.12.6"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http" % "10.1.1",
  "com.typesafe.akka" %% "akka-stream" % "2.5.12",
  "de.heikoseeberger" %% "akka-http-json4s" % "1.20.1",
  "org.json4s" %% "json4s-jackson" % "3.5.3",
  "com.typesafe.akka" %% "akka-http-testkit" % "10.1.1" % Test,


//Slick
  "com.typesafe.slick" %% "slick" % "3.2.3",
  "org.slf4j" % "slf4j-nop" % "1.6.4",
  "com.typesafe.slick" %% "slick-hikaricp" % "3.2.3",

  "ch.qos.logback" % "logback-classic" % "1.2.3",

  "mysql" %  "mysql-connector-java" % "5.1.6"

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

libraryDependencies += "joda-time" % "joda-time" % "2.9.9"

libraryDependencies += "org.scalamock" %% "scalamock" % "4.1.0" % Test
libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.5"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % "test"
libraryDependencies += "org.scalacheck" %% "scalacheck" % "1.14.0" % "test"