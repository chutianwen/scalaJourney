package executor
import java.util.concurrent.{LinkedBlockingQueue, ThreadPoolExecutor, TimeUnit}

import scala.concurrent.{ExecutionContext, Future}

object ExecutorServiceStudy extends App{

  import java.util.concurrent.ExecutorService
  import java.util.concurrent.Executors

  val executor: ExecutorService = Executors.newFixedThreadPool(10)
  val executorService = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue[Runnable]())

  val runnableTask = () => {
    def foo() = {
      try
        TimeUnit.MILLISECONDS.sleep(300)
      catch {
        case e: InterruptedException =>
          e.printStackTrace()
      }
    }

    foo()
  }

  import java.util.concurrent.TimeUnit

  val callableTask = () => {
    def foo() = {
      TimeUnit.MILLISECONDS.sleep(300)
      "Task's execution"
    }

    foo()
  }
  implicit val executorContext = ExecutionContext.fromExecutor(executor)
  Future{
    Thread.sleep(3000)
    println("Future 1 done")
  }
  executor.shutdown()

}
