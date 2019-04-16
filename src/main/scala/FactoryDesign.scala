object FactoryDesign {
  trait Computer{
    def ram:String
    def hdd:String
    def cpu:String

    override def toString = "RAM= " + ram +", HDD=" + hdd + ", CPU=" + cpu
  }

  private case class PC(ram:String, hdd:String, cpu:String) extends Computer

  private case class Server(ram:String, hdd:String, cpu:String) extends Computer

  object ComputerFactory{
    def apply(compType:String, ram:String, hdd:String, cpu:String) = compType.toUpperCase match {
      case "PC" => PC(ram,hdd,cpu)
      case "SERVER" => Server(ram,hdd,cpu)
    }
  }
}


object A extends App{
  import FactoryDesign._

}