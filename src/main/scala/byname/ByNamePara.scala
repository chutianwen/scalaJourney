package byname

object ByNamePara extends App{
  def whenByName[A](test: Boolean, whenTrue: => A, whenFalse: => A): A =
    if(test) whenTrue else whenFalse

  def when[A](test: Boolean, whenTrue: A, whenFalse: A): A =
    if(test) whenTrue else whenFalse

  when(1 == 1, println("foo"), println("bar"))

  // A by-name parameter acts like a def.
  def five[A](a: => A): List[A] = List(a, a, a, a, a)
  val t = five { println("computing a"); util.Random.nextInt(10) }
  println(t)

  def fiveLazy[A](a: => A): List[A] = { lazy val b = a; List(b, b, b, b, b) }
  val t2 = fiveLazy { println("computing a"); util.Random.nextInt(10) }
  println(t)
}
