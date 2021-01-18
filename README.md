Spark SQS Receiver
==================


## Build and Deploy

`sbt '+package; +makePom'`

Deployment is currently manual.  Make sure that you have a user for myget with permissions to upload to the transformer-dependencies-lingk feed (https://www.myget.org/feed/Packages/transformer-dependencies-lingk).

It can be also pushed to https://www.myget.org/F/spark-sqs-receiver/maven/ feed.

From the "Packages" page of the feed:
  * Add Package -> Maven Package
  * choose $SQS_RECEIVER_HOME/target/scala-$SCALA_VERSION/spark-sqs-receiver_$SCALA_VERSION-1.1.0-$VERSION.jar for the jar
  * choose $SQS_RECEIVER_HOME/target/scala-$SCALA_VERSION/spark-sqs-receiver_$SCALA_VERSION-1.1.0-$VERSION.pom for the pom

SQS Amazon queue receiver for the Spark, example usage:

```scala
    ssc.receiverStream(new SQSReceiver("sample")
      .credentials(<key>, <secret>)
      .at(Regions.US_EAST_1)
      .withTimeout(2))
```

   or

```scala
    ssc.receiverStream(new SQSReceiver("sample")
      .credentials(<aws properties file with credentials>)
      .at(Regions.US_EAST_1)
      .withTimeout(2))
```

Where:
* name ("sample" in the example above) - name of the queue
* credentials - AWS credentials
* region - region where the queue exists
* timeout - poll timeout for the queue

By default credentials are empty, regions is Regions.DEFAULT_REGION and timeout is 1 second
