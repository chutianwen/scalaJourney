package futureexe
import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.util.{Failure, Success, Try}

object FutureChains extends App{

  case class NonEnoughBalance(message:String = "Balance not enough") extends Exception(message)
  case class PriceTooHigh(message:String = "Non affordable") extends Exception(message)

  var balance: Int = 0

  def depositMoney(amount:Int):Future[Boolean] = {
    Future {
      println(s"Account Before deposit:$balance")

      if(amount > 0){
        balance += amount
        println(s"Account after deposit:$balance")
        true
      }else{
        false
      }
    }
  }

  def withDrawMoney(amount:Int): Future[Int] = {

    val withDraw: Future[Int] = Future{
      amount
    }
    withDraw.flatMap{
      case amt: Int if amt > balance => {
        println(s"WithDraw amount is higher than balance")
        Future.failed(NonEnoughBalance())
      }
      case amt: Int => {
        val withDraw = Try{
          balance -= amt
          println(s"Account after withDraw:$balance")
          amt
        }
        Future.fromTry(withDraw)
      }
    }.recover{
      case _: NonEnoughBalance => {
        println("Failed of withDrawing, so withDraw 0 dollar")
        0
      }
    }
  }

  def buyCar(withDraw:Int, price:Int): Future[Boolean] = {
    Future{
      println(s"WithDraw:$withDraw, price:$price")
      withDraw > price
    }.flatMap{
      case false => Future.failed(PriceTooHigh())
      case true => {
        println(s"Change:${withDraw - price}, Balance:$balance")
        depositMoney(withDraw - price).flatMap{ _ => {Future.successful(true)}}
      }
    }
  }

  def test(depositAmt: Int, checkoutAmt: Int, price:Int):Unit = {
    val deposit: Future[Boolean] = depositMoney(depositAmt)
    val withDraw: Future[Int] = deposit.withFilter(_ == true).flatMap(_ => withDrawMoney(checkoutAmt))
    val purchase: Future[Boolean] = withDraw.flatMap{ check => buyCar(check, price)}
    Await.ready(purchase, Duration("10 s"))
    purchase.onComplete{
      case Success(x) => {
        if(x) println(s"Successful bought the car, and the balance now is:\t$balance\n")
      }
      case Failure(err) => {
        err match{
          case err: NoSuchElementException ⇒ {
            println(s"Possibly the deposit is not positive, err:\t${err.getMessage}")
          }
          case _ => println(s"Not successful of buying car, due to problem: ${err.getMessage}\n")
        }
      }
    }
  }

  val testId = 0
  testId match{
    case 0 =>  test(depositAmt = 100, checkoutAmt = 50, price = 40)
    case 1 =>  test(depositAmt = 100, checkoutAmt = 50, price = 60)
    case 2 =>  test(depositAmt = 100, checkoutAmt = 110, price = 40)
    case 3 =>  test(depositAmt = -100, checkoutAmt = 110, price = 40)
  }

  println("Done")
  // Make sure onComplete can truly complete
  Thread.sleep(500)
}
