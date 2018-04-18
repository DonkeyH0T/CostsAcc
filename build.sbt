name := "CostsAcc"

version := "0.1"

scalaVersion := "2.12.5"

libraryDependencies ++= Seq(
  "org.scalikejdbc" %% "scalikejdbc"                  % "3.2.3",
  "org.scalikejdbc" %% "scalikejdbc-config"           % "3.2.3",
  "mysql"           %  "mysql-connector-java"         % "5.1.46",
  "ch.qos.logback"  %  "logback-classic"              % "1.2.3"
)