package futureexe

import akka.actor.ActorSystem

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.util.{Failure, Random, Success, Try}

/**
  * https://stackoverflow.com/questions/20874186/scala-listfuture-to-futurelist-disregarding-failed-futures/20874404#20874404
  */
object FutureSeqException extends App {

  val futures = Future.sequence(Seq(Future{1}, Future{throw new Exception}))
  val r = futures.map(Success(_)).recover{case x => Failure(x)}

  private implicit val actorSystem: ActorSystem = ActorSystem("FutureExe")
  private implicit val ec: ExecutionContext = actorSystem.dispatcher

  val seqFutureExceptions = (0 to 10).map { id =>
    Future {
      Thread.sleep(Random.nextInt(2000))
      id
    }.flatMap { id =>
      if (id % 2 == 0) {
        println(s"Even is good, id:$id")
        Future.successful(id)
      } else {
        Future.failed(new Exception(s"ID: $id throwing exception"))
      }
    }
  }

  val futureSeqException = Future.sequence(seqFutureExceptions).map(_ => "Success" ).recover{
    case err => {
      println(err)
      "Failure"
    }
  }

  /**
    * Even with await until Future.seq finished, however, "even is good" will still be printed out for some IDs.
    */
  println("Res: ", Await.result(futureSeqException, Duration("1 min")))

  actorSystem.terminate()

  println("Done")
}
