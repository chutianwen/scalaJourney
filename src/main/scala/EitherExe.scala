object EitherExe extends App{

  def getOrElse(id: Int): Either[String, Int] = {
    if(id < 0){
      Left("Not positive")
    }else{
      Right(id)
    }
  }
  val t: Either[String, Int] = getOrElse(-1)
  val t2 = getOrElse(2)
  println(t2)
  t.left.foreach(println)
  t2 match {
    case Left(x) => println("left", x)
    case Right(x) => println("right", x)
  }
}
