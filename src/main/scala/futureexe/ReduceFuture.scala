package futureexe
import java.util.concurrent.{ExecutorService, Executors}

import scala.concurrent.{ExecutionContext, ExecutionContextExecutor, Future}
import scala.util.{Failure, Success}

object ReduceFuture extends App{

  val executor: ExecutorService = Executors.newFixedThreadPool(8)
  implicit val executorContext: ExecutionContextExecutor = ExecutionContext.fromExecutor(executor)

  val id: Seq[Int] = 0 to 10

  def idToFuture(id: Int): Future[Int] = Future{id + 1}

  val taskReduce: Future[Int] = id.foldLeft(Future.successful(0)){
    case (acc: Future[Int], x: Int) => acc.flatMap{ res => println(res); idToFuture(x)}
  }

  taskReduce onComplete{
    case Success(x) => println(s"Final result $x"); executor.shutdown()
    case Failure(err) => err.printStackTrace(); executor.shutdown()
  }
}
