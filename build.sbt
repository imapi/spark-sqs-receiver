
name := "spark-sqs-receiver"

organization := "io.lingk"

version := "2.0.0"

scalaVersion := "2.12.13"

crossScalaVersions := Seq("2.12.13")

libraryDependencies += "org.apache.spark" %% "spark-core" % "3.2.0" % Provided

libraryDependencies += "org.apache.spark" %% "spark-streaming" % "3.2.0" % Provided

libraryDependencies += "com.amazonaws" % "aws-java-sdk-sqs" % "1.11.937"

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
