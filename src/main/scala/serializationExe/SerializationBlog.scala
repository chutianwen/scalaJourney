package serializationExe

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession
import serializationExe.SerializationBlog.sparkSession

object SerializationBlog extends App{
  val sparkSession: SparkSession = SparkSession.builder
    .master("local[*]")
    .appName("DfWithFuture")
    .config("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
    .getOrCreate()

  val testRdd = sparkSession.sparkContext.parallelize(0 to 10)
  object SerializationTest1{
    val num = 1
    def myFunc = testRdd.map(_ + num)
  }
  val res1 = SerializationTest1.myFunc
  res1.foreach(println)
}

