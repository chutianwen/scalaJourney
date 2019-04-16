import java.util.concurrent.{ExecutorService, Executors}

import org.scalatest.AsyncFlatSpec

import scala.concurrent.{ExecutionContext, ExecutionContextExecutor, Future}
import scala.util.{Failure, Success}

class TrueCompleteSpec extends AsyncFlatSpec {
  implicit val executor: ExecutorService = Executors.newFixedThreadPool(8)
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

  behavior of "run"

  it should "truly complete" in {
    run()
    println("Done")
    succeed
  }
}
