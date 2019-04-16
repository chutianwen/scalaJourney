package iterator

object IteratorTraversable extends App{
  case class Man(id:Int)
  println("Int iterator will start from very beginning")

  val intIterator = (0 to 10).iterator

  intIterator.take(4).foreach(println)
  println("!"*100)
  intIterator.foreach(println)

  println("String iterator won't start from very beginning")
  val stringIterator = (0 to 10).map(_.toString).iterator

  stringIterator.take(4).foreach(println)
  println("!"*100)
  stringIterator.foreach(println)

  val manIterator = (0 to 10).map(Man).iterator

  manIterator.take(4).foreach(println)
  println("!"*100)
  manIterator.foreach(println)
}
