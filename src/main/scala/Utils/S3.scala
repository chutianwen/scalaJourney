package Utils

import java.io.File
import java.nio.file.{Files, Paths, StandardCopyOption}

import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.{ObjectListing, S3ObjectSummary}
import com.typesafe.config.{Config, ConfigFactory}
import org.apache.spark.sql.SparkSession

import scala.collection.JavaConversions.{collectionAsScalaIterable => asScala}
import scala.util.Try

class S3(AWSConfigFile: String, spark: SparkSession) extends Serializable {
  lazy val config: Config = ConfigFactory.parseFile(new File(AWSConfigFile))

  //AWS:
  val AWS_ACCESS_KEY = config.getString("awsKey")
  val AWS_SECRET_KEY = config.getString("awsSecret")
  val AWS_REGION = config.getString("region")
  @transient lazy val AWSCredentials = new BasicAWSCredentials(AWS_ACCESS_KEY, AWS_SECRET_KEY)
  @transient lazy val amazonS3Client = new AmazonS3Client(AWSCredentials)

  //Set AWS credentials:
  @transient lazy val hadoopConf = spark.sparkContext.hadoopConfiguration

  hadoopConf.set("fs.s3a.impl", "org.apache.hadoop.fs.s3a.S3AFileSystem")
  hadoopConf.set("fs.s3a.access.key", AWS_ACCESS_KEY)
  hadoopConf.set("fs.s3a.secret.key", AWS_SECRET_KEY)

  hadoopConf.set("fs.s3n.impl", "org.apache.hadoop.fs.s3native.NativeS3FileSystem")
  hadoopConf.set("fs.s3n.awsAccessKeyId", AWS_ACCESS_KEY)
  hadoopConf.set("fs.s3n.awsSecretAccessKey", AWS_SECRET_KEY)

  hadoopConf.set("fs.s3.impl", "org.apache.hadoop.fs.s3native.NativeS3FileSystem")
  hadoopConf.set("fs.s3.awsAccessKeyId", AWS_ACCESS_KEY)
  hadoopConf.set("fs.s3.awsSecretAccessKey", AWS_SECRET_KEY)

  hadoopConf.set("mapreduce.input.fileinputformat.input.dir.recursive", "true")


  /**
    * this function is a curried function, it takes AmazonS3Clint object , bucket name, path of the bucket as first set
    * and a map function on SeObjectSummary object as second set
    *
    * Java AWS connector documentation :
    * http://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/index.html
    */
  def mapS3Object[T](s3: AmazonS3Client, bucket: String, prefix: String)(f: (S3ObjectSummary) => T) = {
    def scan(acc: List[T], listing: ObjectListing): List[T] = {
      val summaries = asScala[S3ObjectSummary](listing.getObjectSummaries())
      val mapped = (for (summary <- summaries) yield f(summary)).toList
      if (!listing.isTruncated) (acc ::: mapped)
      else scan(acc ::: mapped, s3.listNextBatchOfObjects(listing))
    }

    scan(List(), s3.listObjects(bucket, prefix))
  }

  def mapS3Object[T](bucket: String, prefix: String)(f: (S3ObjectSummary) => T) = {
    def scan(acc: List[T], listing: ObjectListing): List[T] = {
      val summaries = asScala[S3ObjectSummary](listing.getObjectSummaries())
      val mapped = (for (summary <- summaries) yield f(summary)).toList
      if (!listing.isTruncated) (acc ::: mapped)
      else scan(acc ::: mapped, amazonS3Client.listNextBatchOfObjects(listing))
    }

    scan(List(), amazonS3Client.listObjects(bucket, prefix))
  }

  def listFilesInS3(S3Path: String) {
    Try {
      spark.sparkContext.binaryFiles(S3Path).map(file => file._1).foreach(println)
    }
  }

  def getS3FileList(S3Path: String): List[String] = {
    val AWSBucket = S3Path.split("://").last
    val bucketName = AWSBucket.split("/").head
    val urlPrefix = AWSBucket.stripPrefix(bucketName).stripPrefix("/")

    mapS3Object(amazonS3Client, bucketName, urlPrefix)(s => s.getKey)
  }

  def downloadFromS3(S3Path: String, localPath: String): Unit = {
    Try {
      spark.sparkContext.binaryFiles(S3Path).foreach { f =>
        Files.copy(f._2.open, Paths.get(localPath + "/" + f._1.split('/').last), StandardCopyOption.REPLACE_EXISTING)
      }
    }
  }

  def uploadToS3(localPath: String, S3Path: String): Unit = {
    val AWSBucket = S3Path.split("://").last
    val fileList = new java.io.File(localPath).listFiles
    Try {
      fileList.foreach(f => amazonS3Client.putObject(AWSBucket, f.getName, f))
    }
  }

  def downloadS3File(fileName: String, S3Path: String, localPath: String): Unit = {
    val AWSBucket = S3Path.split("://").last
    Try {
      val inStream = amazonS3Client.getObject(AWSBucket, fileName).getObjectContent
      Files.copy(inStream, Paths.get(localPath + "/" + fileName), StandardCopyOption.REPLACE_EXISTING)
    }
  }

  def verifyFileExist(bucketName: String, fileName: String): Boolean = {
    amazonS3Client.doesObjectExist(bucketName, fileName)
  }

}

