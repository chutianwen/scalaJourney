import org.scalatest.AsyncFlatSpec

import scala.concurrent.Future

class AddSpec extends AsyncFlatSpec {

  def addSoon(addends: Int*): Future[Int] = Future { addends.sum }

  behavior of "addSoon"

  it should "1 eventually compute a sum of passed Ints" in {
    val futureSum: Future[Int] = addSoon(1, 2)
    // You can map assertions onto a Future, then return
    // the resulting Future[Assertion] to ScalaTest:
    futureSum map { sum => assert(sum == 3) }
  }

  it should "2 eventually compute a sum of passed Ints" in {
    val futureSum: Future[Int] = addSoon(1, 2, 3)
    // You can map assertions onto a Future, then return
    // the resulting Future[Assertion] to ScalaTest:
    futureSum map { sum => assert(sum == 6) }
  }
}

