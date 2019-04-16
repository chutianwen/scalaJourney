val a = List(1,2,3,4)
a match{
  case List(x,y,z) => println(x + y + z)
}

a match{
  case x::y::z => println(z)
}