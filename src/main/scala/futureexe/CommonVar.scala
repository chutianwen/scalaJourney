package futureexe
import java.util.concurrent.{ExecutorService, Executors}


import scala.concurrent.{ExecutionContext, ExecutionContextExecutor, Future}

object CommonVar extends App{

  implicit val executor: ExecutorService = Executors.newFixedThreadPool(8)
  protected implicit val executorContext: ExecutionContextExecutor = ExecutionContext.fromExecutor(executor)

  object tool{
    def takeHome(time: Long) = Thread.sleep(time)
  }

  val f1 = Future{
    println("wait 3 sec")
    tool.takeHome(0)
    println("f1 back home")
  }

  val f2 = Future{
    println("wait 2 sec")
    tool.takeHome(0)
    println("f2 back home")
  }

  f1.flatMap(_ => f2).onSuccess{
    case _ => executor.shutdown()
  }

  f2.flatMap{x =>
    Future{1}
  }
}
