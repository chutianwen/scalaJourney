package futureexe
import java.util.concurrent.{ExecutorService, Executors}
import scala.concurrent._

import scala.concurrent.{ExecutionContext, ExecutionContextExecutor, Future}
import scala.util.{Failure, Success}
object TrueComplete extends App{
  val t = new TestExecution()
  t.run()
}


class TestExecution{
  // ?Using newWorkStealingPool will exit early.
  implicit val executor: ExecutorService = Executors.newCachedThreadPool()
  protected implicit val executorContext: ExecutionContextExecutor = ExecutionContext.fromExecutor(executor)
  
  def run(): Unit = {
    Future {
      println("Start")
      Thread.sleep(5000)
    }onComplete{
      case Success(_) => println("Done"); executor.shutdown()
      case Failure(err) => err.printStackTrace(); executor.shutdown()
    }
  }
}

