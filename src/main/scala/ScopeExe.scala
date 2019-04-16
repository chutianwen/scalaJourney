class ScopeExe {

  class Foo(foo:Foo){
    private val i = 2
    println(this.i + foo.i)
  }

  // if i is private[this], then foo.i cannot be accessed in FooP
/*  class FooP(foo:FooP){
    private[this] val i = 2
    println(this.i + foo.i)
  }*/
}
