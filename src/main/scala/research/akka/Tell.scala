//package research.akka
//
//import akka.actor.{Actor, ActorRef, Props}
//
//import scala.concurrent.duration._
//import akka.pattern.ask
//
//class TellActor extends Actor {
//
//  val recipient = context.actorOf(Props[ReceiveActor])
//
//  def receive = {
//    case "Start" =>
//      recipient ! "Hello" // equivalent to recipient.tell("hello", self)
//
//    case reply => println(reply)
//  }
//}
//
//class AskActor extends Actor {
//
//  val recipient: ActorRef = context.actorOf(Props[ReceiveActor])
//
//  def receive = {
//    case "Start" =>
//      implicit val timeout = 3 seconds
//      val replyF = recipient ? "Hello" // equivalent to recipient.ask("Hello")
//      replyF.onSuccess{
//        case reply => println(reply)
//      }
//  }
//}
//
//class ReceiveActor extends Actor {
//
//  def receive = {
//    case "Hello" => sender ! "And Hello to you!"
//  }
//}