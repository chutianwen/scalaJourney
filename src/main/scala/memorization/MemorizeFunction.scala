package memorization

import java.util.concurrent.{ExecutorService, Executors}

import scala.concurrent.{ExecutionContext, ExecutionContextExecutor, Future}

object MemorizeFunction extends App{
  implicit val executor: ExecutorService = Executors.newCachedThreadPool()
  protected implicit val executorContext: ExecutionContextExecutor = ExecutionContext.fromExecutor(executor)

  def memoizedIsPrime: Int => Boolean = {
    def isPrime(num: Int): Boolean = {
      2 until num forall (x => num % x != 0)
    }

    val cache = collection.mutable.Map.empty[Int, Boolean]

    num =>
      cache.getOrElse(num, {
        println(s"\n Calling isPrime since input ${num} hasn't seen before and caching the output")
        cache update(num, isPrime(num))
        cache(num)
      })
  }

  val isPrime = memoizedIsPrime
/*
  println(isPrime(10))
  println(isPrime(10))
*/

  Future{
    println(isPrime(10))
  }
  Future{
    println(isPrime(10))
  }
}
