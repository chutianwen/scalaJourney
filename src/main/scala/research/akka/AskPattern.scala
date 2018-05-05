package research.akka
import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.pattern._
import akka.util.Timeout

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration._
import scala.util.{Failure, Success}


case object AskName
case class AskNameOf(other: ActorRef)
case class NameResponse(name:String)

class AskActor(val name:String ) extends Actor{
	def receive: PartialFunction[Any, Unit] = {

		case AskName =>
//			println(sender)
			sender ! NameResponse(name)

		case AskNameOf(other) =>
			println("Sender:" + sender())

			implicit val timeout: Timeout = Timeout(1.seconds)
			val f: Future[Any] = other ? AskName
			f.onComplete{
				case Success(NameResponse(n)) =>
					Thread.sleep(1000)
					println("Their name is:" + n)
				case Success(s) =>
					println("No name")
				case Failure(es) =>
					println("Name failed")
			}

			// We need this block code to ensure the sender is the current sender.
			val currentSender = sender
			println(self, currentSender)
			// sender() is not deterministic when inside Future closure
			Future{
				// If we using sender here, and sender is actually a method, this may cause problem of not using the right sender
				// Since this block is wrapped inside Future.
//				sender ! "message"
				currentSender ! "message"
			}
	}
}

/**
	* Get back information after sending message. Using "?" instead of "!"
	*/
object AskPattern extends App{

	println("Begin")
	val system = ActorSystem("HierarchyExample")
	val actor = system.actorOf(Props(new AskActor("Pat")), "AskActor")
	val actor2 = system.actorOf(Props(new AskActor("Val")), "AskActor2")

	implicit val timeout: Timeout = Timeout(1.seconds)

	val answer: Future[Any] = actor ? AskName
	answer.foreach(x => println("Name is " + x))

	actor ? AskNameOf(actor2)

	Thread.sleep(1000)
	system.terminate()
}
