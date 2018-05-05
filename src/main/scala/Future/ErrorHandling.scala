package Future

import scala.collection.mutable.ArrayBuffer
import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.util.{Failure, Success, Try}

object ErrorHandling extends App{

  /**
    * This won't throw exception in original thread.
    */
  def test0():Unit = {
    val a: Future[Nothing] = Future{
      Thread.sleep(500)
      throw new Exception("I am wrong")
    }
    Await.ready(a, Duration("5 s"))
  }

  /**
    * Exception in another future thread will be printed in original thread by onFailure
    */
  def test1():Unit = {
    val a: Future[Nothing] = Future{
//      Thread.sleep(500)
      throw new Exception("I am wrong")
    }
    a.onFailure{
      case t => {
        println(t.getMessage)
      }
    }
    Await.ready(a, Duration("5 s"))
  }

  def test2():Unit = {
    //  Await.result(a, Duration("5 s"))
    val seq = ArrayBuffer[Future[Int]]()
    for(i <- 1 to 3){
      seq.append(Future{
        try{
          1/0
        }catch {
          case e:Exception => throw new Exception("I am done " + i)
        }
      })
    }
    val seqFuture: Future[ArrayBuffer[Int]] = Future.sequence(seq)
    Await.result(seqFuture, Duration("1 min"))
  }


  def test3():Future[Int] = {
    val a = Future{
      1
    }.flatMap{
      case x if x != 1 => Future.failed(new Exception("Not equal 1"))
      case x => Future.fromTry(Try(x/0))
    }
    a
  }
//  test1()
  val res = test3()
  Await.ready(res, Duration("1 s"))
  res onComplete {
    case Success(x) => println("success " + x)
    case Failure(e) => println("Fail " + e)
  }
  println("Done")
  Thread.sleep(1000)

}
