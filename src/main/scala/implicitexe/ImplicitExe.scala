package implicitexe

object ImplicitExe extends App{
  println("Step 1: How to create a wrapper String class which will extend the String type")
  class DonutString(s: String) {
    def isFavoriteDonut: Boolean = s == "Glazed Donut"
  }

  println("\nStep 2: How to create an implicit function to convert a String to the wrapper String class")
  object DonutConverstions {
    implicit def stringToDonutString(s: String) = new DonutString(s)
  }

  println("\nStep 3: How to import the String conversion so that it is in scope")
  import DonutConverstions._

  println("\nStep 4: How to create String values")
  val glazedDonut: String = "Glazed Donut"
  val vanillaDonut = "Vanilla Donut"

  println("\nStep 5: How to access the custom String function called isFavaoriteDonut")
  println(s"Is Glazed Donut my favorite Donut = ${glazedDonut.isFavoriteDonut}")
  println(s"Is Vanilla Donut my favorite Donut = ${vanillaDonut.isFavoriteDonut}")
}
