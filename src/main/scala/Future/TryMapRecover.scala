package Future

import scala.util.{Failure, Random, Success, Try}

/**
  * Keep monad style, using map/flatMap on Try to chain the result.
  * Pattern matching at end of code. Cuz it breaks Monad
  *
  */
object TryMapRecover extends App {
  val RateT = 0.5 // Failure rate of original "Try"
  val RateS = 0.99 // Failure rate in processing "Success"
  val RateF = 0.99 // Failure rate in processing "Failure"

  def failAt[T](rate: Double, msg: Option[String] = None)(t: => T): T =
    if (rate > 0 && Random.nextDouble <= rate) {
      val reason = msg.getOrElse(s"Orignal failure with rate = $rate")
      throw new RuntimeException(reason)
    } else t

  def failAt[T](rate: Double, msg: String)(t: => T): T = failAt(rate, Option(msg))(t)

  def tryInt: Int = failAt(RateT)(Random.nextInt(10))

  def translateResult(n: Int) = failAt(RateS, "translateResult FAILED randomly")(s"Value was $n")

  def translateFailure(cause: Throwable) = failAt(RateF, s"translateFailure FAILED randomly :: ${cause.getMessage}") {
    Failure(new RuntimeException("translateFailure caught bad result", cause))
  }

  def printResult(result: Try[String]): Unit = {
    def exceptionMessage(ex: Throwable): String = Option(ex.getCause).fold(ex.getMessage) { cause => exceptionMessage(cause) }
    val msg = result
      .map { msg => s"SUCCESS: $msg"}
      .recover { case ex => "FAILURE: " + exceptionMessage(ex) }
      .getOrElse("REALLY UNEXPECTED")
    println(msg)
  }

  // stay in monad!
  val result2: Try[String] =
    Try { tryInt }
      .map { translateResult }
      .recoverWith { case ex => translateFailure(ex) }

  printResult(result2)

  // match breaks monad
  val result1: Try[String] =
    Try(tryInt) match {
      case Success(n)  => Success(translateResult(n))
      case Failure(ex) => translateFailure(ex)
    }
  printResult(result1)

  def processString(msg: String): String = Option(msg).fold("") { x =>
    if(x.isEmpty) processString(null) else processString(x.tail)
  }

}