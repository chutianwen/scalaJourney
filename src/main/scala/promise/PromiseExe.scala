package promise
import scala.concurrent.{Future, Promise}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Try

object PromiseExe extends App{

  def produceSomething():Int = {
    Thread.sleep(2000)
    println("Produce Something")
    1
  }

  def continueDoingSomethingUnrelated(): Unit = {
    Thread.sleep(2000)
    println("Continue doing something unrelated")
  }

  def startDoingSomething(): Unit = {
    Thread.sleep(2000)
    println("start doing something")
  }

  def doSomethingWithResult(result: Int): Unit = {
    Thread.sleep(2000)
    println("Do something with result")
    println("result is", result)
  }

  val p = Promise[Int]()
  val f = p.future

  val producer = Future {
    val r = produceSomething()
    p success r
    continueDoingSomethingUnrelated()
  }

  val consumer = Future {
    startDoingSomething()
    f onSuccess {
      case r => doSomethingWithResult(r)
    }
  }

  Future {
    val r = Try(produceSomething())
    p complete r
    continueDoingSomethingUnrelated()
  }

/*  val producer = Future {
    val r = someComputation
    if (isInvalid(r))
      p failure (new IllegalStateException)
    else {
      val q = doSomeMoreComputation(r)
      p success q
    }
  }*/

  def first[T](f: Future[T], g: Future[T]): Future[T] = {
    val p = Promise[T]

    f onSuccess {
      case x => p.trySuccess(x)
    }

    g onSuccess {
      case x => p.trySuccess(x)
    }

    p.future
  }

  Thread.sleep(5000)
}
