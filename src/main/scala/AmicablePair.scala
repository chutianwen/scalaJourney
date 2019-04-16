import scala.collection.mutable

object AmicablePair extends App {

  val numToFactorSum: mutable.Map[Long, Long] = collection.mutable.Map[Long, Long]()
  val res = amicablePair(100000000)

  def amicablePair(k: Long): Seq[(Long, Long)] = {
    val initTime = System.nanoTime

    val result = collection.mutable.ArrayBuffer[(Long, Long)]()
    var number = 4
    while (number <= k) {
      val factorSum = calculateFactorSum(number)
      if (factorSum != -1) {
        numToFactorSum.put(number, factorSum)
      }
      if (factorSum < number && numToFactorSum.getOrElse(factorSum, -1) == number) {
        result.append((factorSum, number))
      }
      number += 1
    }
    val endTime = System.nanoTime
    println("** Elapsed time: " + (endTime - initTime) / 1e9 + " s")
    result
  }

  println(calculateFactorSum(2))

  def calculateFactorSum(number: Long): Long = {
    var factor = 2L
    val hi = math.floor(math.sqrt(number)).toLong
    var factorSum = 1L
    var isPrime = true
    while (factor <= hi) {
      if (number % factor == 0) {
        isPrime = false
        factorSum += factor + number / factor
      }
      factor += 1
    }
    if (hi * hi == number) {
      factorSum -= hi
    }
    if (isPrime) -1 else factorSum
  }
  print(res)
}
