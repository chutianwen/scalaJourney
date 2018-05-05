package algorithms

import scala.collection.mutable.ArrayBuffer

object combinations extends App{
	def getPermutation(nums:Array[Int]):Array[Array[Int]] = nums match{
		case null | Array() => Array(Array())
		case Array(x) => Array(nums)
		case _ =>
			(for(id <- nums.indices) yield {
				val (left, right) = nums.splitAt(id)
				val subPermutations = getPermutation(left ++ right.tail)
				subPermutations.map(right.head +: _)
			}).flatten.toArray
	}

	def getPermutation(nums:List[Int]): List[List[Int]] = nums match{
			case null | Nil => List(Nil)
			case List(a) => List(nums)
			case _ =>
				val t = for(i <- nums.indices) yield {
					val (before, rest) = nums.splitAt(i)
					val subPermutations = getPermutation(before ++ rest.tail)
					subPermutations.map(rest.head :: _)
				}
				t.flatten.toList
	}
	getPermutation(Array[Int]()).foreach(x => println(x.mkString(",")))
	println(getPermutation(null:List[Int]))

	val a = Array(1,2,3)
}
