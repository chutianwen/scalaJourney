package graphXexamples
import com.typesafe.scalalogging.StrictLogging
import org.apache.spark.graphx.{Edge, Graph, VertexId, VertexRDD}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession


object Test1 extends App with StrictLogging {

  val sparkSession: SparkSession = SparkSession.builder
    .master("local[1]")
    .appName("GraphX example")
    .config("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
    .getOrCreate()

  val sc = sparkSession.sparkContext

  trait VertexType

  case class User(name: String, job: String) extends VertexType

  // Create an RDD for the vertices
  val users: RDD[(VertexId, VertexType)] = sc.parallelize(Array(
    (3L, User("rxin", "student")),
    (7L, User("jgonzal", "postdoc")),
    (5L, User("franklin", "prof")),
    (2L, User("istoica", "prof")),
    (4L, User("peter", "student"))
    )
  )

  // Create an RDD for edges
  val relationships: RDD[Edge[String]] = sc.parallelize(Array(
    Edge(3L, 7L, "collab"),
    Edge(5L, 3L, "advisor"),
    Edge(2L, 5L, "colleague"),
    Edge(5L, 7L, "pi"),
    Edge(4L, 0L, "student"),
    Edge(5L, 0L, "colleague")
    )
  )

  // Build the initial Graph
  val defaultUser: VertexType = User("John Doe", "Missing")

  val graph = Graph(users, relationships, defaultUser)
  val vs: VertexRDD[VertexType] = graph.vertices

  def display(vertex: VertexRDD[VertexType] ) = {
    vertex.foreach{case (id, node: User) => println("!" * 10 + node.job)}
  }
  display(vs)
}
