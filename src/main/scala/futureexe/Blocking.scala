package futureexe

import scala.concurrent.{ExecutionContext, ExecutionContextExecutor, Future, blocking}

object Blocking extends App {
  implicit val ec: ExecutionContextExecutor = ExecutionContext.global

  def withoutBlocking(): Unit = {
    for (i <- 1 to 32000) {
      Future {
        println("begin", i)
        Thread.sleep(10000)
        println("Done", i)
      }
    }
  }

  def withBlocking(): Unit = {
    for (i <- 1 to 32000) {
      Future {
        println("begin", i)
        blocking {
          Thread.sleep(10000)
        }
        println("Done", i)
      }
    }
  }

  withBlocking()
//
//  withoutBlocking()
  Thread.sleep(1000000)
}