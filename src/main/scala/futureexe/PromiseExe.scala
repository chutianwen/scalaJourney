package futureexe
import scala.concurrent.{ Future, Promise }
import scala.concurrent.ExecutionContext.Implicits.global

object PromiseExe extends App{

  def produceSomething(): Int = {
    println("produce something ")
    1
  }

  def continueDoingSomethingUnrelated(): Unit = {
    println("continueDoingSomethingUnrelated")
  }

  def startDoingSomething():Unit = {
    println("startDoingSomething")
  }

  def doSomethingWithResult(input: Int):Unit = {
    println("doSomethingWithResult", input)
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
    f foreach { r =>
      doSomethingWithResult(r)
    }
  }

  Thread.sleep(2000)
}
