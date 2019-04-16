package generics

object HasAIsA extends App{

  /***
    * A House is a Building (inheritance);
      A House has a Room (composition);
      A House has an occupant (aggregation).
    */

  trait Building{
    protected val size:String
  }

  case class Room(id: String)
  case class Occupant(id: String)
  case class House(size: String, rooms: Seq[Room], occupants: Seq[Occupant]) extends Building{
  }

}
