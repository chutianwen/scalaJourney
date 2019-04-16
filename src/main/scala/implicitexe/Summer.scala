package implicitexe

object Summer{
   implicit object Helper {
       def add(a: Int, b: Int): Int = a + b
   }
}