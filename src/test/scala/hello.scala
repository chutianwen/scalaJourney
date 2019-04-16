import com.typesafe.scalalogging.StrictLogging

import scala.util.Try

object hello extends App with StrictLogging{
	val a = Try(throw new Error("Bad"))
	a.failed.foreach(logger.error("Fail:", _))
	println(1/0)
}
