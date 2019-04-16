package serializationExe

import java.io.{ByteArrayInputStream, ByteArrayOutputStream, ObjectInputStream, ObjectOutputStream}
import java.util.concurrent.atomic.AtomicInteger

object LazySerializationTest extends App {

  def serialize(obj: Any): Array[Byte] = {
    val bytes = new ByteArrayOutputStream()
    val out = new ObjectOutputStream(bytes)
    out.writeObject(obj)
    out.close()
    bytes.toByteArray
  }

  def deSerialise(bytes: Array[Byte]): MyClass = {
    new ObjectInputStream(new ByteArrayInputStream(bytes)).
      readObject().asInstanceOf[MyClass]
  }

  def deSerializeToAny(bytes: Array[Byte]): Any = {
    new ObjectInputStream(new ByteArrayInputStream(bytes)).readObject()
  }


  def test(obj: MyClass): Unit = {
    val bytes = serialize(obj)
    val fromBytes = deSerialise(bytes)

    println(s"Original cnt = ${obj.x.cnt}")
    println(s"De Serialized cnt = ${fromBytes.x.cnt}")
  }

  object X {
    val cnt = new AtomicInteger()
  }

  class X {
    // Not Serializable because of object X has not extended Serializable
    val cnt: Int = X.cnt.incrementAndGet
    println(s"Create instance of X #$cnt")
  }

  class MyClass extends Serializable {
    lazy val x = new X
  }


  object MyTestObject {
    val salary = 1
  }

  val se = serialize(MyTestObject)
  println(deSerializeToAny(se).asInstanceOf[MyTestObject.type].salary)




  // Not initialized
  val mc1 = new MyClass
  test(mc1)

  // Force lazy evaluation
  val mc2 = new MyClass
  mc2.x
  test(mc2) // Failed with NotSerializableException

}