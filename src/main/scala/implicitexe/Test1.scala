package implicitexe

object Test1 extends App{
  import Summer.Helper

  def sum(a:Int, b:Int)(implicit summer: Helper.type) = summer.add(a,b)

  // after import object, object will auto allocated.
  println(sum(1,2))

}
