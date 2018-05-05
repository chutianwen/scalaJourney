package Processor


import java.io.File

import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.s3.AmazonS3Client
import com.typesafe.config.{Config, ConfigFactory}
import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession

/**
  * This is the parent class that all other Fraud Detection Processor classes should inherit from. It initializes Spark
  * environment and configures AWS & Hadoop.
  *
  * How to use:
  * Define other child Processor class such as: GaussianDemo extends Processor.
  */
class Processor extends Serializable {
  org.apache.log4j.BasicConfigurator.configure()
  Logger.getRootLogger.setLevel(Level.ERROR)

  @transient lazy val fraudDetectConfig: Config = ConfigFactory.parseFile(new File("fraud-detection.conf"))

  //AWS:
  val AWS_ACCESS_KEY = fraudDetectConfig.getString("awsKey")
  val AWS_SECRET_KEY = fraudDetectConfig.getString("awsSecret")
  val AWS_REGION = fraudDetectConfig.getString("region")
  @transient lazy val AWSCredentials = new BasicAWSCredentials(AWS_ACCESS_KEY, AWS_SECRET_KEY)
  @transient lazy val amazonS3Client = new AmazonS3Client(AWSCredentials)

  //Local input and output directories:
  val localInput = "input/"
  val localOutput = "output/"

  //Spark:
  val warehouseLocation = "file:${system:user.dir}/spark-warehouse"
  lazy val spark: SparkSession = SparkSession
    .builder()
    .appName("Fraud-Detection")
    .config("spark.master", "local[*]")
    .config("spark.sql.warehouse.dir", warehouseLocation)
    .config("spark.executor.memory", "5g")
    .config("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
    .enableHiveSupport()
    .getOrCreate()

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

  val initTime = System.nanoTime()

}
