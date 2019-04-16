package futureexe

import com.typesafe.scalalogging.StrictLogging

import scala.collection.mutable.ArrayBuffer
import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.util.{Failure, Success, Try}

object ErrorHandling extends App with StrictLogging{

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
    a.wait()
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
    Future{
      Thread.sleep(2000)
      1
    }.flatMap{
      case x if x != 1 => Future.failed(new Exception("Not equal 1"))
      case x => Future.successful(x)
//      case x => Future.fromTry(Try(x/0))
    }
  }
//  val a: Int = Await.result(test3(), Duration("1 s"))

  val b: Future[Int] = Await.ready(test3(), Duration("3 s"))
/*  val res: Int = a match{
    case Success(x) => x
    case Failure(err) => logger.error("Something wrong", err); throw new RuntimeException("bad")
  }
  println(res)*/

  val res: Option[Try[Int]] = b.value
  val resfinal: Option[Int] = res.map(x => x.getOrElse(0))
  resfinal.foreach(println)

  val res1: Future[Int] = Future{
    1
  } andThen[Int] {
    case x => x.get + 4
  }
  Thread.sleep(1000)
  println(res1.value.get)
  Thread.sleep(10000)
}
