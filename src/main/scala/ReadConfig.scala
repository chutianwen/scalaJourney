import com.typesafe.config.ConfigFactory

object ReadConfig extends App{

  (0 until 10).foreach { _ =>
    val config = ConfigFactory.load()
    println(config.getConfig("a").root().render)
  }
}
