package threads

import java.util.concurrent.atomic.AtomicBoolean

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object flagApp extends App{

	@volatile var flag: AtomicBoolean = new AtomicBoolean(false)
	var id: Int  = 0



	val t2 = Future{
		for(x <- 0 until 200){
			if(x % 2 == 0){
				flag.set(true)
			}else{
				flag.set(false)
			}
			id = x
			println("From t2, id:%s, flag:%s".format(id, flag.get))
		}
	}

	val t1 = Future{
		while(true) {
			if (flag.get()) {
				println("From t1, id:%s, flag:%s".format(id, flag.get))
			}
		}
	}
	Thread.sleep(100)
}
