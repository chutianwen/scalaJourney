package inheritage
/**
  * When C extends T, T has a class property p, C don't need to have P type, but any type <: P
  */
object Polymorphism extends App{

  class P(val f1: String){
    override def toString: String = f1

    def fun: Any = "1"
  }

  class C(override val f1: String, val f2: String) extends P(f1){
    override def toString: String = s"$f1\t$f2"
  }

  trait T {
    val p: P
    def fun(s: P):Int
  }

  class A extends T {
    // class member p can be C type, unnecessary to be P
    val p: C = new C("1", "2")
    println(p.f2)

    /**
      * fun signature has to be P type, incorrect if s is C
      * @param s
      * @return
      */
    def fun(s: P) = 1
  }

  trait Parent[T]{
    val name: T
    def fun(name: T): Unit
    def build():Any
  }

  class ChildP extends Parent[P]{
    val name: P = new P("William")
    def fun(name: P) = println(name)

    override def build(): Int = 1
  }

  class ChildC extends Parent[C]{
    val name: C = new C("William", "the one")
    def fun(name: C) = println(name)
    override def build(): String = "hi"
  }

  val childP: ChildP = new ChildP
  val childC: ChildC = new ChildC
  val nameP = new P("William")
  val nameC = new C("William", "the one")
  childP.fun(nameP)
  childP.fun(nameC)
  println(childC.build())
}
