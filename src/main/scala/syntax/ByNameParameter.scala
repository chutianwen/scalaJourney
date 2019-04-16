package syntax

object ByNameParameter{

  /**
    * Parameters are by-name, whenTrue and whenFalse won't be executed before passing to caller.
    * @param test
    * @param whenTrue
    * @param whenFalse
    * @tparam A
    * @return
    */
  def whenByName[A](test: Boolean, whenTrue: => A, whenFalse: => A): A =
    if(test) whenTrue else whenFalse

  /**
    * Regular parameters, if whenTrue passed from a block of code, it will executed first
    * @param test
    * @param whenTrue
    * @param whenFalse
    * @tparam A
    * @return
    */
  def when[A](test: Boolean, whenTrue: A, whenFalse: A): A =
    if(test) whenTrue else whenFalse

  def whenTrue: Any = {
    val randInt = util.Random.nextInt(5)
    println("when true", randInt)
    randInt
  }

  def whenFalse: Any = {
    println("False situation")
    "False"
  }

  /**
    * By-name parameter like passing a function, it will be executed every time you call it.
    * It will return a list of random(may different) values
    * @param a
    * @tparam A
    * @return
    */
  def five[A](a: => A): List[A] = List(a, a, a, a, a)

  /**
    * We call a only one time, and using b to store the result of by-name parameter. So it should return
    * a list of same values
    * @param a
    * @tparam A
    * @return
    */
  def fiveLazy[A](a: => A): List[A] = { lazy val b = a; List(b, b, b, b, b) }

  def main(args: Array[String]): Unit = {
    // whenTrue and whenFalse will only be executed inside whenByName, after call
    val resWhenByName = whenByName(test = true, whenTrue, whenFalse)
    println(s"Result of whenByName: $resWhenByName")

    // both whenTrue and whenFalse will be executed before called in when
    val resWhen = when(test = true, whenTrue, whenFalse)
    println(s"Result of when: $resWhen")

    val t = five { println("computing a"); util.Random.nextInt(10) }

    val t2 = fiveLazy { println("computing a"); util.Random.nextInt(10) }

  }
}
