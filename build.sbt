import SonatypeKeys._

name := "spark-sqs-receiver"

sonatypeSettings

organization := "com.github.imapi"

profileName := "com.github.imapi"

version := "1.0.2"

scalaVersion := "2.11.5"

libraryDependencies += "org.apache.spark" %% "spark-core" % "1.2.0"

libraryDependencies += "org.apache.spark" %% "spark-streaming" % "1.2.0"

libraryDependencies += "com.github.seratch" %% "awscala" % "0.4.+"

pomExtra := {
  <url>https://github.com/imapi/spark-sqs-receiver</url>
    <licenses>
      <license>
        <name>Apache 2</name>
        <url>http://www.apache.org/licenses/LICENSE-2.0.txt</url>
      </license>
    </licenses>
    <scm>
      <connection>scm:git:github.com/imapi/spark-sqs-receiver.git</connection>
      <developerConnection>scm:git:git@github.com:imapi/spark-sqs-receiver.git</developerConnection>
      <url>https://github.com/imapi/spark-sqs-receiver</url>
    </scm>
    <developers>
      <developer>
        <id>imapi</id>
        <name>Ivan Bondarenko</name>
        <url>https://github.com/imapi</url>
      </developer>
    </developers>
}