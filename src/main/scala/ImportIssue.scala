import akka.actor.ActorSystem

object StorageSystem{
  implicit lazy val system = ActorSystem("client")
  implicit val a = 4

}

object ImportIssue extends App{

  // If only import, system won't be executed, unless be used once.
  import StorageSystem.system

  // system
  println("Done")
}


class Imp(implicit a:Int)

import StorageSystem.a
object Check extends Imp