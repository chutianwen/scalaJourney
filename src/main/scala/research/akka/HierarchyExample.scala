package research.akka

import akka.actor.SupervisorStrategy.Resume
import akka.actor.{Actor, ActorRef, ActorSystem, OneForOneStrategy, Props, SupervisorStrategy}

case object CreateChild
case object SignalChildren
case object PrintSignal
case class DivideNumbers(n:Int, d:Int)


class ParentActor extends Actor {

	private var number = 0
	private var children = collection.mutable.Buffer[ActorRef]()

	def receive: PartialFunction[Any, Unit] = {
		case CreateChild =>
			children += context.actorOf(Props[ChildActor], "child" + number)
			number += 1
		case SignalChildren =>
			children.foreach( _ ! PrintSignal)
	}

	override def supervisorStrategy: SupervisorStrategy = OneForOneStrategy(){
		case ae: ArithmeticException => Resume
	}

	def foo(): Unit = println("Normal Method")

}

class ChildActor extends Actor{
	println("Child created")

	def receive: PartialFunction[Any, Unit] = {
		case PrintSignal => println(self)
		case DivideNumbers(n,d) => println(n/d)
	}

	override def preStart(): Unit = {
		super.preStart()
		println("preStart")
	}

	override def postStop(): Unit = {
		super.postStop()
		println("postStop")
	}

	override def preRestart(reason: Throwable, message: Option[Any]): Unit = {
		super.preRestart(reason, message)
		println("preRestart")
	}

	override def postRestart(reason: Throwable): Unit = {
		super.postRestart(reason)
		print("postRestart")
	}
}


object HierarchyExample extends App{
	println("Begin")
	val system = ActorSystem("HierarchyExample")
	val actor = system.actorOf(Props[ParentActor], "Parent1")

	actor ! CreateChild
	actor ! CreateChild

	actor ! SignalChildren
//	actor ! CreateChild
//	actor ! SignalChildren

	val child0 = system.actorSelection("/user/Parent1/child0")
	child0 ! DivideNumbers(4, 0)

	Thread.sleep(5000)
	system.terminate()
	println("Done")
}
