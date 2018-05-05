package Future
import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.util.{Failure, Success}
object CallBack extends App{

  def test0():Unit = {
    @volatile var totalA = 0

    val text = Future {
      "na" * 16 + "BATMAN!!!"
    }

    text onSuccess {
      case txt => {
        totalA += txt.count(_ == 'a')
        println(totalA)
      }
    }

    //  Thread.sleep(400)

    text onSuccess {
      case txt => {
        totalA += txt.count(_ == 'A')
        println(totalA)
      }
    }
    Thread.sleep(1000)
  }


  def test():Unit = {
    val a = Future{
      Thread.sleep(1000)
      1
    }
    a onComplete{
      case Success(x) => println(x)
      case Failure(x) => println("Fail" + x)
    }
    Await.ready(a, Duration("1 min"))
    Thread.sleep(100)
  }

  def test1():Unit = {
    var a = 0
    val t = Future{
      Thread.sleep(1000)
      1
    }

    while(a == 0){
      t onSuccess{
        case x => {
          a = 1
        }
      }
    }
    println("Done")
  }

  /*def createT2andT4(tt1: Int) = {
    val t2 = Future { tt1 + 4 }
    val t4_1 = t4.flatMap{x => Future{x + 4}}

    t2.zip(t4_1)

  }
  */

  def test2():Unit = {
    // 1
    val t1: Future[Int] = Future{1}

    // 5
    val t2: Future[Int] = t1.flatMap(x => Future{x + 4})

    // 3
    val t3: Future[Int] = Future{3}

/*    // 4
    val t4: Future[Int] = t1.filter(x => x != 0).flatMap(_ => t2.flatMap{x => Future{x + 4}})

    // 8
    val t4_1: Future[Int] = t4.flatMap{x => Future{x + 4}}*/

    val res: Future[Int] = for{
      tt1 <- t1
      tt2 <- t2
      if tt1 != 0
      tt4 <- Future{4}
      tt4_1 <- Future{5}
//      tt4 <- if(tt1 != 0) t3.flatMap(x => Future{x + 4}) else Future.successful(0)
//      tt4_1 <- if(tt1 != 0) Future{tt4 + 4} else Future.successful(0)
    } yield tt1 + tt2 + tt4_1

    println(Await.ready(res, Duration("5 s")))
    res.onComplete {
      case Success(x) => println(x)
      case Failure(x) => println(x.getCause)
    }

    Thread.sleep(1000)
  }
  val t1 = Future{1123}
  var t2 = t1
  t2 = Future{222}
  println(Await.result(t2, Duration("3 s")))
  test2()

}
