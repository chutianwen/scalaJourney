package research.akka

import akka.actor.{Actor, ActorSystem, Cancellable, Props}

import scala.concurrent.duration._

case object Count

class ScheduleActor extends Actor{
	var n = 0;
	def receive: PartialFunction[Any, Unit] = {
		case Count =>
			n += 1
			println(n)
	}
}

object Scheduler extends App{
	println("Begin")
	val system = ActorSystem("ScheduleActor")
	val actor = system.actorOf(Props[ScheduleActor], "ScheduleActor1")
	implicit val ec = system.dispatcher

//	actor ! Count
//	system.scheduler.scheduleOnce(1.second)(actor ! Count)
	val can: Cancellable = system.scheduler.schedule(0.seconds, 200.millis, actor, Count)

	Thread.sleep(2000)
	
	can.cancel()
	system.terminate()

}
