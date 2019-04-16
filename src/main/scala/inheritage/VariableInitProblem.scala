package inheritage

/**
  * Watch out how unimplemented variables behave during inheritance
  */
object VariableInitProblem extends App{

  trait Parent{
    val weight: Int
    val increasedWeight: Int = weight + 10
    def showWeight(): Unit = println(weight)
  }

  class Child extends Parent{
    val weight = 5
  }

  val c = new Child()
  println(s"weight:${c.weight}")

  // !!! This is not showing 15, weight = 0 when init increasedWeight
  println(s"increased weight:${c.increasedWeight}")
  c.showWeight()
}
