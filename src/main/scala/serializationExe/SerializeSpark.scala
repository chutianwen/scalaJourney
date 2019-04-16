package serializationExe

import java.io.{ByteArrayOutputStream, ObjectOutputStream}

import org.apache.spark.sql.SparkSession

/**
  * Serialization is not a mandatory in spark! As long as worker can reach the classpath!
  * Resource:
  * https://www.nicolaferraro.me/2016/02/22/using-non-serializable-objects-in-apache-spark/
  *
  * So non serialize stuff inside static object can solve problem because worker executor can access that  class. If I have object A{val a  = 1}
  * and use A.a inside mapPartition, each executor actually will have its own A.a right?
  * since each executor is a JVM and they find the class A which is a singleton object, and allocate memory of A.a when calling
  */

object NoSerializePerson{
  val name = "Bill"
  val sc = SparkSession.builder.getOrCreate().sparkContext
}

class NoSerializePerson{
  @transient val name = "William"

  def getName = name
}

class SerializedPerson extends Serializable {
  @transient val name = "Willy"

  def getName = name
}


class SerializeSpark{

  def run: Unit ={
    // Spark can serialize it
    val sparkSession: SparkSession = SparkSession.builder
      .master("local[*]")
      .appName("TestSerialize")
      .config("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
      .getOrCreate()

    // Even there's a sc inside, logic still serializable
    println {
      sparkSession.sparkContext.parallelize(1 to 10)
        .map(i => {
          val ds = NoSerializePerson.name
          NoSerializePerson.sc
          println(ds)
          // do something
        })
        .count
    }


    // Will printout null
    val person = new SerializedPerson()
    println{
      sparkSession.sparkContext.parallelize(1 to 10)
        .map{ _ =>
          println(person.name)
          println(person.getName)
        }
        .count
    }

    // create Person inside, and will show william rather than null
    println{
      sparkSession.sparkContext.parallelize(1 to 10)
        .map{ _ =>
          val person = new NoSerializePerson()
          println(person.name)
          println(person.getName)
        }
        .count
    }

    // Will not printout null
    val nonSerializePerson = new NoSerializePerson()
    println{
      sparkSession.sparkContext.parallelize(1 to 10)
        .map{ _ =>
          println(nonSerializePerson.name)
          println(nonSerializePerson.getName)
        }
        .count
    }

    // local sc will throw infamous serialization issue
    val localSc = sparkSession.sparkContext
    println {
      sparkSession.sparkContext.parallelize(1 to 10)
        .map(i => {
          localSc
          // do something
        })
        .count
    }
  }
}

object SerializeSpark extends App{

  def serialize(obj: Any): Array[Byte] = {
    val bytes = new ByteArrayOutputStream()
    val out = new ObjectOutputStream(bytes)
    out.writeObject(obj)
    out.close()
    bytes.toByteArray
  }


  val test = new SerializeSpark()
  test.run

// but NoSerializePerson cannot pass serialize()/like regular java serialization without declaring as Serializable
//  serialize(NoSerializePerson)
}
