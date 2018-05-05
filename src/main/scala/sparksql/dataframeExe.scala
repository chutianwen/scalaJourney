package sparksql

import com.typesafe.scalalogging.StrictLogging
import org.apache.spark.sql.SparkSession

class A(val a:Int)
object dataframeExe extends App with StrictLogging{

	val spark = SparkSession.builder().master("local[*]").appName("Xmode data").getOrCreate()
	import spark.implicits._

	spark.sparkContext.setLogLevel("WARN")
	val dataXmode = spark.read.csv("data/data0.csv")
	dataXmode.show()
	spark.stop()

	val t = new A(4){
		val weight = 4
	}
	println(t.weight)
	val fun: () => Int = () => 5
	logger.info("Done")
}
