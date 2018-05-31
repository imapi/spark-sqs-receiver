package com.github.imapi

import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.sqs.AmazonSQS
import com.amazonaws.services.sqs.AmazonSQSClient
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.auth.PropertiesCredentials
import com.amazonaws.regions.Regions
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.receiver.Receiver

import scala.collection.JavaConversions._

import scala.annotation.tailrec

/** SQS Amazon queue receiver for the Spark, example usage:
  * {{{
  *    ssc.receiverStream(new SQSReceiver("sample")
  *    .credentials(<key>, <secret>)
  *    .at(Regions.US_EAST_1)
  *    .withTimeout(2))
  * }}}
  * or
  * {{{
  *    ssc.receiverStream(new SQSReceiver("sample")
  *    .credentials(<aws properties file with credentials>)
  *    .at(Regions.US_EAST_1)
  *    .withTimeout(2))
  * }}}
  *
  * Where:
  * <ul>
  *   <li>name ("sample" in the example above) - name of the queue</li>
  *   <li>credentials - AWS credentials</li>
  *   <li>region - region where the queue exists</li>
  *   <li>timeout - poll timeout for the queue</li>
  * </ul>
  *
  * By default credentials are empty, regions is Regions.DEFAULT_REGION, timeout is 20 seconds and maxMessages is 10
  */
class SQSReceiver(name: String) extends Receiver[String](StorageLevel.MEMORY_AND_DISK_2) {
  private var awsKey: String = ""
  private var awsSecret: String = ""
  private var region: Regions = Regions.DEFAULT_REGION
  private var timeout: Int = 20
  private var maxMessages: Int = 10

  def credentials(accessKeyId: String, secretAccessKey: String): SQSReceiver = {
    awsKey = accessKeyId
    awsSecret = secretAccessKey
    this
  }

  def at(region: Regions): SQSReceiver = {
    this.region = region
    this
  }

  def withTimeout(secs: Int) = {
    this.timeout = secs
    this
  }
  
  def withMaxMessages(m: Int) = {
    this.maxMessages = m
    this
  }

  def onStart() {
    new Thread("SQS Receiver") {
      override def run() {
        try {
          val credentials = new BasicAWSCredentials(awsKey, awsSecret)
          val sqs = new AmazonSQSClient(credentials)

          val receiveMessageRequest = new ReceiveMessageRequest(name).withMaxNumberOfMessages(maxMessages).withWaitTimeSeconds(timeout);
          
          @tailrec
          def poll(): Unit = {
            if (!isStopped()) {
              sqs.receiveMessage(receiveMessageRequest).getMessages().foreach(msg => {
                store(msg.getBody)
                sqs.deleteMessage(new DeleteMessageRequest(name, msg.getReceiptHandle()))
              })
              poll()
            }
          }
          poll()

        } catch {
          case e: IllegalArgumentException => restart(e.getMessage, e, 5000)
          case t: Throwable => restart("Connection error", t)
        }
      }
    }.start()
  }


  def onStop() {
    // There is nothing much to do as the thread calling poll()
    // is designed to stop by itself isStopped() returns false
  }


  private class SQSCredentials extends Serializable {
    private var _key = ""
    private var _secret = ""
    def key = _key
    def secret = _secret
    def key(v: String) = {
      _key = v
      this
    }
    def secret(v: String) = {
      _secret = v
      this
    }
    def notValid = _key.isEmpty || _secret.isEmpty
  }

}
