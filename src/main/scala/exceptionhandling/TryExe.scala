package exceptionhandling

import scala.util.{Failure, Success, Try}

object TryExe extends App{

  def forComprehension():Unit = {
    val sumTry = for {
      int1 <- Try(Integer.parseInt("1"))
      int2 <- Try(Integer.parseInt("2"))
    } yield {
      int1 + int2
    }

    sumTry match {
      case Failure(thrown) => {
        Console.println("Failure: " + thrown)
      }
      case Success(s) => {
        Console.println(s)
      }
    }
    if (sumTry.isFailure) {
      val thrown = sumTry.failed.get
    }
  }

  def recoverTry():Unit = {
    val sum: Try[Int] = for {
      int1 <- Try(Integer.parseInt("one"))
      int2 <- Try(Integer.parseInt("two"))
    } yield {
      int1 + int2
    }
    val sumRecover: Try[Int] = sum.recover{
      case e => 0
    }
    val sumRecoverWith: Try[Int] = sum.recoverWith{
      case e: NumberFormatException => Failure[Int](new IllegalArgumentException("Try 1 next time"))
      case _ => Try(0)
    }
  }

}
