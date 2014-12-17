name := "spark-sqs-receiver"

version := "1.0"

scalaVersion := "2.10.4"

libraryDependencies += "org.apache.spark" %% "spark-core" % "1.1.1"

libraryDependencies += "org.apache.spark" % "spark-streaming_2.10" % "1.1.1"

libraryDependencies += "com.github.seratch" %% "awscala" % "0.4.+"