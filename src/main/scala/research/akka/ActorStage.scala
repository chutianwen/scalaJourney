package research.akka
import akka.actor.{Actor, ActorSystem, Props}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


class SimpleActor extends Actor {
	def receive: PartialFunction[Any, Unit] = {
		case s: String =>
			val t = Future {
				println(self, sender, "String: %s".format(s))
			}
		case i: Int => println("Number: %d".format(i))
	}

	def foo(): Unit = println("Normal Method")

}

object ActorStage extends App {



	println("Begin")
  val system = ActorSystem("SimpleSystem")
  val actor = system.actorOf(Props[SimpleActor], "SimpleActor")
	val actor2 = system.actorOf(Props[SimpleActor], "SimpleActor2")

	actor ! 4
  actor ! "Hi"
	actor ! 'a'
	actor2 ! "Big"
	actor2 ! "culture"


	Thread.sleep(5000)
	system.terminate()
	println("Done")
}