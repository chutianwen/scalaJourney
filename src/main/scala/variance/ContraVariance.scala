package variance

import variance.CovarianceExe.{Animal, Cat}

/**
  * A type parameter A of a generic class can be made contravariant by using the annotation -A. This creates a subtyping relationship between the
  * class and its type parameter that is similar, but opposite to what we get with covariance. That is, for some class Writer[-A],
  * making A contravariant implies that for two types A and B where A is a subtype of B, Writer[B] is a subtype of Writer[A].
  */
object ContraVariance extends App{

  abstract class Printer[-A] {
    def print(value: A): Unit
  }

  class AnimalPrinter extends Printer[Animal] {
    def print(animal: Animal): Unit =
      println("The animal's name is: " + animal.name)
  }

  class CatPrinter extends Printer[Cat] {
    def print(cat: Cat): Unit = println("The cat's name is: " + cat.name)
  }

  val myCat: Cat = Cat("Boots")

  def printMyCat(printer: Printer[Cat]): Unit = {
    printer.print(myCat)
  }

  val catPrinter: Printer[Cat] = new CatPrinter
  val animalPrinter: Printer[Animal] = new AnimalPrinter

  printMyCat(catPrinter)
  printMyCat(animalPrinter)


}
