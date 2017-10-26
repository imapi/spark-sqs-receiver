
name := "spark-sqs-receiver"

organization := "com.github.imapi"

version := "1.0.3"

scalaVersion := "2.11.7"

libraryDependencies += "org.apache.spark" %% "spark-core" % "2.1.1"

libraryDependencies += "org.apache.spark" %% "spark-streaming" % "2.1.1"

libraryDependencies += "com.amazonaws" % "aws-java-sdk-sqs" % "1.11.220"

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