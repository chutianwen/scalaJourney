package Future
import java.util.concurrent.{ExecutorService, Executors}


import scala.concurrent.{ExecutionContext, ExecutionContextExecutor, Future}

object TrueComplete extends App{

  val executor: ExecutorService = Executors.newFixedThreadPool(10)
  implicit val executorContext: ExecutionContextExecutor = ExecutionContext.fromExecutor(executor)

  val f1 = Future{
    Thread.sleep(2000)
    println("F1 done")
  }
}
