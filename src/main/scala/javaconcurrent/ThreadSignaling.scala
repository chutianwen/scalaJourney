package javaconcurrent

import java.util.concurrent.TimeUnit

object ThreadSignaling extends App{

  class MonitorObject{}

  class MyWaitNotify(myMonitorObject: MonitorObject){

    // Using extra variable to store the signal, to prevent missing signals
    var wasSignaled = false

    def doWait():Unit = {
      // do wait has to be in synchronized block of object
      myMonitorObject.synchronized{
        // Using while as spin lock to guard against spurious wake-ups
        while(!wasSignaled){
          try{
            // wait() cannot exit until re-obtain the monitor object after doNotify finished.
            myMonitorObject.wait()
          }catch{
            case e: InterruptedException => e.printStackTrace
          }
        }
//        wasSignaled = false
      }
    }

    def doNotify():Unit = {
      myMonitorObject.synchronized{
        wasSignaled = true
        myMonitorObject.notifyAll()
      }
    }
  }

  class Receiver(myWaitNotify: MyWaitNotify) extends Runnable{
    override def run(): Unit = {
      println(s"${Thread.currentThread}: I am sleeping before being awakened")
      myWaitNotify.doWait()
      println(s"${Thread.currentThread}: I am start working after being notified")
    }
  }

  class Sender(myWaitNotify: MyWaitNotify) extends Runnable{
    override def run(): Unit = {
      println(s"${Thread.currentThread}: I am sending message after 2 sec to awake receiver")
      TimeUnit.SECONDS.sleep(2)
      myWaitNotify.doNotify()
    }
  }

  val myMonitorObject = new MonitorObject()
  val myWaitNotify = new MyWaitNotify(myMonitorObject)
  val receiver = new Receiver(myWaitNotify)
  val sender = new Sender(myWaitNotify)
  val t1 = new Thread(receiver)
  val t1_1 = new Thread(receiver)
  val t2 = new Thread(sender)
  t1.start()
  t1_1.start()
  t2.start()
  t1.join()
  t1_1.join()
  t2.join()
  println("Done")
}
