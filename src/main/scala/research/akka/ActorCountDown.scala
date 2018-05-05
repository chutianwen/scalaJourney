package research.akka

import akka.actor.{Actor, ActorRef, ActorSystem, Props}


case class StartCounting(n: Int, other:ActorRef)
case class CountDown(n: Int)

class CountDownActor extends Actor{
	override def receive: Receive = {
		case StartCounting(n, other) =>
			println(n)
			other ! CountDown(n - 1)
		case CountDown(n) =>
			println(self)
			if(n>0){
				println(n)
				sender ! CountDown(n-1)
			}else{
			context.system.terminate()
			}
	}
}




object ActorCountDown extends App{
	val system = ActorSystem("SimpleSystem")
	val actor1 = system.actorOf(Props[CountDownActor], "CounterDown1")
	val actor2 = system.actorOf(Props[CountDownActor], "CounterDown2")
	actor1 ! StartCounting(10, actor2)

	println("Done")
}
