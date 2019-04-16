package algorithms


object combinations extends App {
  val a = Array(1, 2, 3, 4)

  def getPermutation(nums: Array[Int]): Array[Array[Int]] = nums match {
    case null | Array() => Array(Array())
    case Array(x) => Array(nums)
    case _ =>
      (for (id <- nums.indices) yield {
        val (left, right) = nums.splitAt(id)
        val subPermutations = getPermutation(left ++ right.tail)
        subPermutations.map(right.head +: _)
      }).flatten.toArray
  }

  def getPermutation(nums: List[Int]): List[List[Int]] = nums match {
    case null | Nil => List(Nil)
    case List(a) => List(nums)
    case _ =>
      val t = for (i <- nums.indices) yield {
        val (before, rest) = nums.splitAt(i)
        val subPermutations = getPermutation(before ++ rest.tail)
        subPermutations.map(rest.head :: _)
      }
      t.flatten.toList
  }

  getPermutation(Array[Int]()).foreach(x => println(x.mkString(",")))
  println(getPermutation(null: List[Int]))

  def getPermutationSeq(nums: Seq[Int]): Seq[Seq[Int]] = nums match {
    case null | Seq() => Seq(Seq())
    case Seq(x) => Seq(Seq(x))
    case x => x.indices.flatMap { id =>
      val (left, right) = nums.splitAt(id)
      getPermutationSeq(left ++ right.tail).map(right.head +: _)
    }
  }


  val res = getPermutationSeq(a)
  println(res.size)
//  res.foreach(println)

  def matchSeq(input: Seq[Int]) = input match{
    case Seq(x) => println(x)
    case head :: rest => println(head, rest)
  }
  matchSeq(Seq(1))
  matchSeq(Seq(1,23))
}
