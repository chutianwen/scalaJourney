package mongoConnect

import com.mongodb.spark.config._
import com.mongodb.spark._
import org.apache.spark.sql.{Row, SparkSession}
import org.bson.Document

object mongoApp extends App{

	val sparkSession = SparkSession.builder()
		.master("local[*]")
		.appName("MongoSparkConnectorIntro")
		.getOrCreate()

	val optParas = Map(
		"uri" -> "mongodb://mongodb.fractal/ueba-db.Dummy"
	)

	val customReadConfig = ReadConfig(optParas)
	println(customReadConfig)
	val df = MongoSpark.load(sparkSession, customReadConfig)
	df.show()
	df.printSchema()

	val rdd2 = df.rdd.map(x => Row(x(0), "fuck", null))
	val df2 = sparkSession.createDataFrame(rdd2, df.schema)
	df2.show()
	val df3 = df.union(df2)
	df3.show()
//
//	val docs = Seq((1, "A"), (2, "C"), (3, "C"))
//	import sparkSession.implicits._
//	val data = sparkSession.sparkContext.parallelize(docs).toDF
//	data.show()
	val customWriteConfig = WriteConfig(Map("uri" -> "mongodb://mongodb.fractal/ueba-db.Dummy", "replaceDocument" -> "true"))
	MongoSpark.save(df3, customWriteConfig)

}
