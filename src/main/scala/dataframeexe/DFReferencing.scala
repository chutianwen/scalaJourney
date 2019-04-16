package dataframeexe

import org.apache.spark.sql.{Column, SparkSession}

object DFReferencing extends App{
  val sparkSession: SparkSession = SparkSession.builder
    .master("local[*]")
    .appName("SparkJob")
    .config("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
    .getOrCreate()

  import sparkSession.implicits._

  def filterHelper(x:Int, y:Int) = Math.abs(x - y) > 30
  val df1 = Seq (1, 2, 3, 444).toDF("x")
  val df2 = Seq(1, 3, 7, 11).toDF("q")
  val udf = org.apache.spark.sql.functions.udf(filterHelper _)
  val filter: Column = udf(df1("x"), df2("q"))
  val df3 = df1.join(df2, filter, "left_semi")
  val df4 = df1.join(df2, filter)

  df1.show()
  df2.show()

  // df3 is what you want, filtered records
  df3.show()
  df4.show()
  sparkSession.stop()

}
