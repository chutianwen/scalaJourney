import scala.util.Try

object tryexe extends App{
  val f = print _
  (0 to 10).foreach(f)

  val t: Try[Int] = Try{1/9}
  t.foreach(println)

  println(t.map(1/_).recover{case err => 3})
}
