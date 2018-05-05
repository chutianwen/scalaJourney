package Future
import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.util.{Failure, Success, Try}

object ExceptionInFutureSequence extends App{

  def test1(): Unit = {
    val listOfFutures: Seq[Future[Int]] = Future.successful(1) :: Future.failed(new Exception("Failure")) ::
      Future.successful(3) :: Nil

    val futureOfList: Future[Seq[Int]] = Future.sequence(listOfFutures)

    futureOfList onComplete {
      case Success(x) => {
        //      Thread.sleep(1000)
        println("Success!!! " + x)
      }
      case Failure(ex) => {
        //      Thread.sleep(1000)
        println("Failed !!! " + ex)
      }
    }
  }

  def test2(): Unit = {

    def futureToFutureTry[T](f: Future[T]): Future[Try[T]] ={
      f.map(x => Success(x)).recover{case e => Failure(e)}
    }

    val listOfFutures: Seq[Future[Int]] = Future.successful{1/2} :: Future.failed(new Exception("Failure")) ::
      Future.successful(3) :: Nil

    val listOfFutureTrys = listOfFutures.map(futureToFutureTry)

    val futureListOfTrys: Future[Seq[Try[Int]]] = Future.sequence(listOfFutureTrys)

    futureListOfTrys onComplete {
      case Success(x) => {
        println("Success!!! " + x)
      }
      case Failure(ex) => {
        println("Failed !!! " + ex)
      }
    }
    val futureListOfSuccesses: Future[Seq[Try[Int]]] = futureListOfTrys.map(x => x.filter(y => y.isSuccess))
    val futureListOfFailures = futureListOfTrys.map(_.filter(_.isFailure))
  }

//  test1()
  test2()
  Thread.sleep(2000)
  println("Done")


}
