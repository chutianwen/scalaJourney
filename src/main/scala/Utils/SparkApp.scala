package Utils

import org.apache.spark.sql.SparkSession

trait SparkApp extends App{
  val sparkSession: SparkSession = SparkSession.builder
    .master("local[*]")
    .appName("SparkJob")
    .config("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
    .getOrCreate()

  val sc = sparkSession.sparkContext
}
