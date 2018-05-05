package partialfunction

object ApplyExe extends App{
  val k: PartialFunction[Int,Int] = {
    case x if x > 5 => 1
    case x if x <= 5 => 0
  }

  val a = 0 to 10
  println(a.map(k))

  val f: Function[Int, Int] = {
    case x if x > 5 => 1
    case x if x <= 5 => 0
  }
  println(a.map(f))
}
