package inheritage

object ParentChild extends App{

  trait P {
    def fun: Any
  }

  class C extends P{
    def fun: String = "C"
  }
  class A{
    def fun: Any = "parent"
    def fun2: String = "parent2"
  }

  class B extends A{
    override def fun: String = "child"
//    override def fun2: Any = "child2"
  }

  println(new A().fun)
  println(new B().fun)
  println(new C().fun)
//  println(new A().fun2)
//  println(new B().fun2)
}
