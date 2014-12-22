package com.github.imapi

import awscala._
import awscala.sqs.{Queue, SQS}
import com.amazonaws.auth.PropertiesCredentials
import com.amazonaws.regions.Regions
import org.apache.spark.Logging
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.receiver.Receiver

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
  * By default credentials are empty, regions is Regions.DEFAULT_REGION and timeout is 1 second
  */
class SQSReceiver(name: String) extends Receiver[String](StorageLevel.MEMORY_AND_DISK_2) with Logging {

  private val credentials: SQSCredentials = new SQSCredentials
  private var region: Regions = Regions.DEFAULT_REGION
  private var timeout: Int = 1000

  def credentials(accessKeyId: String, secretAccessKey: String): SQSReceiver = {
    credentials.key(accessKeyId).secret(secretAccessKey)
    this
  }

  def credentials(filename: String): SQSReceiver = {
    val p = new PropertiesCredentials(new File(filename))
    credentials.key(p.getAWSAccessKeyId).secret(p.getAWSSecretKey)
    this
  }

  def at(region: Regions): SQSReceiver = {
    this.region = region
    this
  }

  def withTimeout(secs: Int) = {
    this.timeout = secs * 1000
    this
  }

  def onStart() {
    new Thread("SQS Receiver") {
      override def run() {
        try {

          implicit val sqs = (if (credentials.notValid)
            SQS.apply(Credentials(credentials.key, credentials.secret))
          else SQS.apply())
            .at(Region.apply(region))

          val queue: Queue = sqs.queue(name) match {
            case Some(q) => q
            case None => throw new IllegalArgumentException(s"No queue with the name $name found")
          }

          @tailrec
          def poll(): Unit = {
            if (!isStopped()) {
              queue.messages.foreach(msg => {
                store(msg.body)
                queue.remove(msg)
              })
              Thread.sleep(timeout)
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
