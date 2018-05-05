package Future
import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.util.{Failure, Success}

object ForComprehension extends App{
/*  val rateQuote = Future {
    connection.getCurrentValue(USD)
  }

  val purchase = rateQuote map { quote =>
    if (isProfitable(quote)) connection.buy(amount, quote)
    else throw new Exception("not profitable")
  }

  purchase onSuccess {
    case _ => println("Purchased " + amount + " USD")
  }*/

}
