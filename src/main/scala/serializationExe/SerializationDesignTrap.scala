package serializationExe

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession

class UnrelatedType(val bla: String) extends Serializable

class Parser {
  val unrelatedUnserial = new UnrelatedType("blabla")
  private val unrelated = "unrelated"
  private val suffix = "123"

  def parse(row: String): String = {
    row + "hhha"
  }

  def run(input: RDD[String]):RDD[String] = input.map(row => parse(row))

  def run2(input: RDD[String]): RDD[String] ={
//    val tmp = new String(suffix)
    // why this way also works? Maybe already got the address of parser object, then should be fine, parser object
    // does not need to be serialized
    val tmp = suffix
    input.map(row => row + tmp)
  }

  def run3(input: RDD[String]): RDD[String] ={
    // Needs to work from parser object, so whole parser object need to be serialized before
    input.map(row => row + suffix)
  }
}

object SerializationDesignTrap extends App{
  val sparkSession: SparkSession = SparkSession.builder
    .master("local[*]")
    .appName("TestSerialize")
    .config("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
    .getOrCreate()

  val input: RDD[String] = sparkSession.sparkContext.parallelize((0 to 10).map(_.toString))
  val parser = new Parser()
  println(s"From driver ${parser.unrelatedUnserial.bla}")
//  parser.run(input).collect.foreach(println)
  parser.run2(input).collect.foreach(println)
//  parser.run3(input).collect.foreach(println)

  sparkSession.stop()
}
