package variance

object CovarianceExe {
  abstract class Animal {
    def name: String
  }
  case class Cat(name: String) extends Animal
  case class Dog(name: String) extends Animal

  /**
    * In the following example, the method printAnimalNames will accept a list of animals as an argument and print their names each on a new line.
    * If List[A] were not covariant, the last two method calls would not compile, which would severely limit the usefulness of the printAnimalNames method.
    * @param animals
    */
  def printAnimalNames(animals: List[Animal]): Unit = {
    animals.foreach { animal =>
      println(animal.name)
    }
  }

  val cats: List[Cat] = List(Cat("Whiskers"), Cat("Tom"))
  val dogs: List[Dog] = List(Dog("Fido"), Dog("Rex"))
  printAnimalNames(cats)

}
