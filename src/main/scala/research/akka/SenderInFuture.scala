package research.akka
import akka.actor.{Actor, ActorRef, ActorSystem, Props}

import scala.concurrent.{ExecutionContextExecutor, Future}


case class actorWithMessage(other: ActorRef, message:String)
case class Message(content: String)


class MessageActor extends Actor{

	// Put the future threads inside the akka space
	implicit val ec: ExecutionContextExecutor = context.system.dispatcher
	override def receive: Receive = {
		case actorWithMessage(other, message) =>
			other ! Message(message)

		case Message(message) =>
//			implicit val ec = system.dispatcher
			Future{
				Thread.sleep(500)
				// Cannot calling sender() inside another thread, especially this thread will take some time processing
				// sender will change in the future threads
				println("futureexe", self, sender(), message, message)
			}
			Thread.sleep(250)
			println(self, sender(), message)

	}
}

/**
	* Illustrate usage of sender() within the future thread inside an actor
	*/
object SenderInFuture extends App{

	val system = ActorSystem("MessageActor")
	val A = system.actorOf(Props[MessageActor], "A")
	val B = system.actorOf(Props[MessageActor], "B")
	val C = system.actorOf(Props[MessageActor], "C")

	for(_ <- 0 until 2){
		A ! actorWithMessage(B, "Alan")
		C ! actorWithMessage(B, "Cat")
	}

	Thread.sleep(5000)
	system.terminate()

}
