import org.apache.spark.sql.SparkSession

object SparkExecutorException extends App{
  val sparkSession: SparkSession = SparkSession.builder
    .master("local[*]")
    .appName("SparkExecutorException")
    .config("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
    .getOrCreate()

  try{
    sparkSession.sparkContext.parallelize(0 to 10).foreach(_ / 0)
  }catch{
    case e: ArithmeticException => {
      println("Nested exception", e.getCause)
    }
    case e: Exception if e.getCause.isInstanceOf[ArithmeticException] => {
      println("\n")
      e.getCause match{
        case cuz: ArithmeticException => println(s"Showing nested cause ${e.getCause}")
      }
      println(s"Msg:${e.getMessage}")
    }
  }
}
