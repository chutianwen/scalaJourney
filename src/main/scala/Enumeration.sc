
case class NameName(v: String)

object AAA{
  val a = NameName("1")
  case class Small(w: Int)
}
object BBB{
  val a = NameName("1")
  case class Small(w: Int)
}

val a: AAA.Small = BBB.a
println(a)