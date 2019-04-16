package generics

/*object GenericInheritVersion{
  trait Factory{
    def build():
  }
}*/
object GenericExe extends App{

  /**
    * My thoughts:
    * Generic type on trait or classes serve more like an adjective. Like What type of Factory it is.
    * If is Factory[Car], then we know all behaviors/methods of such Factory is dealing with cars
    * @tparam T
    */
  trait Factory[T]{
    def build():T
    def fix(entry: T): T
  }

  case class Car(Model: String)
  object CarFactory extends Factory[Car] {
    override def build() = Car("Tiguan")

    override def fix(entry: Car): Car = Car(entry.Model + "fixed")
  }

  case class Robot(Model: String)
  object RobotFactory extends Factory[Robot]{
    override def build() = Robot("Tegra")

    override def fix(entry: Robot): Robot = Robot(entry.Model + "fixed")
  }

  val car: Car = CarFactory.build()
  val robot: Robot = RobotFactory.build()

  println(car, robot)

  // ----------------------------------

  /**
    * Since generic types is more like adjective, if we use Meta type to describe our App, it indicates some of the behaviors need to get around with
    * such type. However, we only use generic types here for holding a meta. There's only has-a relationship. Maybe version2 is better .
    */
  trait Meta{
    val config: Seq[String]
    val extra: Int
  }
  /*
    // ------------------Version1--------------------
    case class XMeta(config: Seq[String], disp: Seq[String]) extends Meta
    trait App[T<:Meta] {
      protected val meta: T
    }

    class XApp(protected val meta: XMeta) extends App[XMeta]{
      val config = meta.config
      val disp = meta.disp
    }

    // is-a relationship
    // -------------Version 2:No generic type usage--------------
   trait AppNoGenericType{
      protected val meta: Meta
    }
    case class YMeta(config: Seq[String], disp: Seq[String]) extends Meta
    class YApp(protected val meta: YMeta) extends AppNoGenericType{
      val config = meta.config
      val disp = meta.disp
    }

    val xapp: App[XMeta] = new XApp(XMeta(Seq("A"), Seq("Generic")))
    val yapp: YApp = new YApp(YMeta(Seq("Y"), Seq("NonGeneric")))



    class GenericApp[T<:ParentApp](var app: T){
      def build(): T = {
        app
      }

      def run = app.run
    }

    class GreatApp(var parentApp: ParentApp){
      def run = parentApp.run
    }

    class ParentApp{
      def run = println("run")
    }
    class SillyApp extends ParentApp{}

    class SmartApp extends ParentApp{}

    val gApp: GenericApp[SillyApp] = new GenericApp(new SillyApp())
    gApp.build()
    gApp.run


    val gApp2: GreatApp = new GreatApp(new SillyApp())
    gApp2.run*/
}
