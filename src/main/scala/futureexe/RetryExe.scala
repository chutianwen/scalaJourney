package futureexe

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContext, Future}
import akka.pattern.after
import akka.actor.{LightArrayRevolverScheduler, Scheduler}

object RetryExe extends App{

  val a = new StringBuilder("1234")
  def fun(): Int = {
    println(s"Begin${a.toString()}")
    if(a.nonEmpty){
      a.dropRight(0)
      throw new RuntimeException("Not empty yet")
    }else{
      println("Done")
      1
    }
  }

/*  def retry[T](op: => T, delay: FiniteDuration, retries: Int)(implicit ec: ExecutionContext, s: Scheduler): Future[T] =
    Future(op) recoverWith { case _ if retries > 0 => after(delay, s)(retry(op, delay, retries - 1)) }

  val retries: List[FiniteDuration] = List(200 millis, 200 millis , 500 millis, 1 seconds, 2 seconds)
  implicit val scheduler: Scheduler = null
  val task: Future[Int] = retry(fun(), 1 seconds, 5)
  println(Await.result(task, 2 minutes))*/
}
