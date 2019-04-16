import org.apache.spark.sql.SparkSession

object graphXBFS extends App{
  import org.apache.spark.graphx._

  /**
    * Returns the shortest directed-edge path from src to dst in the graph. If no path exists, returns
    * the empty list.
    */
  def bfs[VD, ED](graph: Graph[VD, ED], src: VertexId, dst: VertexId): Seq[VertexId] = {
    if (src == dst) return List(src)

    // The attribute of each vertex is (dist from src, id of vertex with dist-1(parent node))
    var g: Graph[(Int, VertexId), ED] =
      graph.mapVertices((id, _) => (if (id == src) 0 else Int.MaxValue, 0L)).cache()

    g.vertices.collect().foreach(println)
    println("*"*100)
    // Traverse forward from src
    var dstAttr = (Int.MaxValue, 0L)

    var idx = 0
    while (dstAttr._1 == Int.MaxValue) {
      g.vertices.collect.foreach(x => println(s"$idx>>>>>>>>>>>>>>>>>>>>${x.toString}"))
      val msgs: VertexRDD[(Int, VertexId)] = g.aggregateMessages[(Int, VertexId)](
        e => if (e.srcAttr._1 != Int.MaxValue && e.srcAttr._1 + 1 < e.dstAttr._1) {
          e.sendToDst((e.srcAttr._1 + 1, e.srcId))
        },
        (a, b) => if (a._1 < b._1) a else b
      ).cache()

      println("==============" + idx + "==================")
      msgs.collect.foreach(x => println(s"$idx-----------------${x.toString}"))

      if (msgs.count == 0) return List.empty

      g = g.ops.joinVertices(msgs) {
        (id, oldAttr, newAttr) =>
          if (newAttr._1 < oldAttr._1) newAttr else oldAttr
      }.cache()

      dstAttr = g.vertices.filter(_._1 == dst).first()._2
      idx += 1
    }
    g.vertices.collect.foreach(x => println(s"$idx>>>>>>>>>>>>>>>>>>>>${x.toString}"))
    // Traverse backward from dst and collect the path
    var path: List[VertexId] = dstAttr._2 :: dst :: Nil
    while (path.head != src) {
      path = g.vertices.filter(_._1 == path.head).first()._2._2 :: path
    }

    path
  }

  val sparkSession: SparkSession = SparkSession.builder
    .master("local[*]")
    .appName("SparkJob")
    .config("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
    .getOrCreate()

  val g = Graph.fromEdgeTuples(
    sparkSession.sparkContext.parallelize(List((1L, 2L), (2L, 3L), (3L, 4L), (2L, 4L), (10L, 11L))),
    defaultValue = 1)

  println("%"*10, bfs(g, 1L, 4L))

//  println("@"*10, bfs(g, 1L, 10L))
//
//  println("!"*10, bfs(g, 4L, 3L))
//
//  println("$"*10, bfs(g, 3L, 3L))

  sparkSession.stop()
}


