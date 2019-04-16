import org.scalatest.{Matchers, WordSpec}

class Deadlock extends WordSpec with Matchers {

  "Deadlock" when {

    "two threads are waiting on each other" in {

      val lock1 = new Object()
      val lock2 = new Object()

      class ThreadDemo1 extends Thread {
        override def run(): Unit = {
          lock1.synchronized {

            println("Thread 1: Holding lock 1...")

            try {
              Thread.sleep(10)
            }
            catch {
              case _: InterruptedException =>
            }

            println("Thread 1: Waiting for lock 2...")

            lock2.synchronized {
              println("Thread 1: Holding lock 1 & 2....")
            }

          }
        }
      }

      class ThreadDemo2 extends Thread {
        override def run(): Unit = {
          lock2.synchronized {

            println("Thread 2: Holding lock 2...")

            try {
              Thread.sleep(10)
            }
            catch {
              case _: InterruptedException =>
            }

            println("Thread 2: Waiting for lock 1...")

            lock1.synchronized {
              println("Thread 2: Holding lock 1 & 2...")
            }
          }
        }
      }

      val t1 = new ThreadDemo1()
      val t2 = new ThreadDemo2()

      t1.start()
      t2.start()
    }
  }
}