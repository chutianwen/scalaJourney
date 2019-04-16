package syntax

/**
  * Difference between concepts of Method and Function. Methods are not values, function is an instance. Method is able to transfer to function.
  *
  * Syntax sugar of function and partial function.
  * A partial function is a function that does not provide an answer for every possible input value it can be given.
  * It provides an answer only for a subset of possible data, and defines the data it can handle. In Scala, a partial function can also be queried to
  * determine if it can handle a particular value.
  */
object MethodAndFunction extends App{

  // define a method.
  def add1(n: Int): Int = n + 1

  // val f = add1 won't work, you can’t declare a variable of type (n: Int)Int. Methods are not values.
  // However, by adding the η-expansion postfix operator (η is pronounced “eta”), we can turn the method into a function value. Note the type of f.
  val f: Int => Int = add1 _

  // The effect of _ is to perform the equivalent of the following: we construct a Function1 instance that delegates to our method.
  val g = new Function1[Int, Int] {
    def apply(n: Int): Int = add1(n)
  }

  // partial function is subType of Function
  val partfun: PartialFunction[Int, Int] = {
    case x if x > 0 => 1
    case x if x < 0 => 0
  }

  val fun: Function[Int, Int] = {
    case x if x >= 0 => 1
    case x if x < 0 => 0
  }

  val fun2: Function[Int, Int] = {
    x => x + 1
  }

  println(partfun(4))
  println(partfun(-1))
  if(partfun.isDefinedAt(0)) println(partfun(0))

  println(fun(4))
  println(fun(-1))
  println(fun(0))

  // A terrific feature of partial functions is that you can chain them together. For instance, one method may only work with even numbers,
  // and another method may only work with odd numbers. Together they can solve all integer problems. In the following example, two functions are
  // defined that can each handle a small number of Int inputs, and convert them to String results:

  // converts 1 to "one", etc., up to 5
  val convert1to5 = new PartialFunction[Int, String] {
    val nums = Array("one", "two", "three", "four", "five")
    def apply(i: Int) = nums(i-1)
    def isDefinedAt(i: Int): Boolean = i > 0 && i < 6
  }

  // converts 6 to "six", etc., up to 10
  val convert6to10 = new PartialFunction[Int, String] {
    val nums = Array("six", "seven", "eight", "nine", "ten")

    def apply(i: Int) = nums(i-6)
    def isDefinedAt(i: Int): Boolean = i > 5 && i < 11
  }

  val handle1to10 = convert1to5 orElse convert6to10
  println(handle1to10(3))
  println(handle1to10(8))
}
