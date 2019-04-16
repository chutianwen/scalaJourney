import Utils.SparkApp
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.DataFrame

object UnionDFExe extends App with SparkApp {
  val taskRDD = sparkSession.sparkContext.parallelize(0 until 3)
  val a = taskRDD.collect{case x if x > 1 => x}.collect()
  a.foreach(println)
  println("S")
  import sparkSession.sqlContext.implicits._
  val task: RDD[Seq[(Int, Int, Int)]] = taskRDD.map(_ => Seq((1,2,3), (4,5,6)))
  task.foreach(_.toDF.show())

  // however, after direct collect, it will throw error
  println("collect and show will fail")
//  task.map(_.toDF).collect.foreach(_.show)

  println("show the union df")
  val unionRes = task.reduce((x, y) => x ++ y).toDF
  unionRes.show()
}