Spark SQS Receiver
==================
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
