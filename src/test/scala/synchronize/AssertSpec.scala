package synchronize

import org.scalatest.{Assertion, FlatSpec}

class AssertSpec extends FlatSpec{
  "left" should "equal to right" in {
    val left = 2
    val right = 1
    assert(left == right)
  }

  "assert result" should "work" in {
    assertResult(Seq){
      Seq(1,2,3).getClass
    }
  }

  "Assert throw" should "work in example " in {
    val s = "1s"
    assertThrows[IndexOutOfBoundsException] { // Result type: Assertion
      s.charAt(-1)
    }

    withClue("this is a clue") {
      assertThrows[IndexOutOfBoundsException] {
        "hi".charAt(-1)
      }
    }
  }

  "Intercept throw" should "work in example " in {
    val s = "hi"
    val caught: IndexOutOfBoundsException =
      intercept[IndexOutOfBoundsException] { // Result type: IndexOutOfBoundsException
        s.charAt(-1)
      }
    assert(caught.getMessage.indexOf("-1") != -1)
  }
}
