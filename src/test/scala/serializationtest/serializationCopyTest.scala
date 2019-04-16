package serializationtest

import org.apache.spark.sql.SparkSession

class SparkSerializationObject {

  val sparkSession: SparkSession = SparkSession.builder
    .master("local[*]")
    .appName("DfWithFuture")
    .config("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
    .getOrCreate()

  @transient val sc = sparkSession.sparkContext

  object Example extends Serializable {

    // if lazy here, failure, work without lazy here?
    val testRdd = sc.parallelize(List(1, 2, 3))
    val num = 1
    def myFunc = testRdd.map(_ + num).collect.toList equals List(2, 3, 4)
  }

  println(Example.myFunc)
  println("Done")
}

object SubTest extends App{
  val fun1 = new SparkSerializationObject()

}