package serializationExe

import org.apache.spark.sql.{Row, SparkSession}

import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

object DFwithFuture extends App{
  val sparkSession: SparkSession = SparkSession.builder
    .master("local[*]")
    .appName("DfWithFuture")
    .config("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
    .getOrCreate()

  import sparkSession.implicits._

  val data = (0 to 10).map(x => (x, x+1)).toDF()

  val dataFuture = data.map{case Row(x:Int, y:Int) =>
    val r = Future{x + y}
    Await.result(r, Duration("1 min"))
  }
  dataFuture.show()


}
