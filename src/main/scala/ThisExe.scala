/*
trait A{
  this: ThisExe =>
  println("From trait")
  println(s"From parent trait :${this.a}")
  println(s"From parent trait: ${this.s}")

  this.fun
}

class ThisExe extends A{
  val a = 1
  val s = "1"
  println(s"From child class :${this.a}")
  println(s"From child class: ${this.s}")
  def fun = println("Fun From class")
}

object SelfTyping extends App{
  val t = new ThisExe()
}
*/
