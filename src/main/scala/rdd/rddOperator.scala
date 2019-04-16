package rdd

import Utils.SparkApp
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession

object rddOperator extends SparkApp{
  val data = Seq(1 -> 5, 1 -> 3, 2 -> 4)
  val dataRDD = sparkSession.sparkContext.parallelize(data)
  val dataRDDGroup: RDD[(Int, Iterable[Int])] = dataRDD.groupByKey()
  val dataReduced: RDD[(Int, Int)] = dataRDD.reduceByKey(_ + _)
  val dataRDDGroupMerge = dataRDDGroup.map{case (id, seq) => (id, seq.sum, "check")}
  dataReduced.collect.foreach(println)
  dataRDDGroup.collect.foreach(println)
  dataRDDGroupMerge.collect.foreach(println)

  import org.apache.spark.graphx._
  // Import random graph generation library
  import org.apache.spark.graphx.util.GraphGenerators
  // A graph with edge attributes containing distances
  val graph: Graph[Long, Double] = GraphGenerators.logNormalGraph(sc, numVertices = 100).mapEdges(e => e.attr.toDouble)
  val sourceId: VertexId = 42 // The ultimate source
  // Initialize the graph such that all vertices except the root have distance infinity.
  val initialGraph: Graph[Double, Double] = graph.mapVertices((id, _) => if (id == sourceId) 0.0 else Double.PositiveInfinity)
  val sssp = initialGraph.pregel(Double.PositiveInfinity)(
    vprog = (id, dist, newDist) => math.min(dist, newDist), // Vertex Program
    sendMsg = triplet => {  // Send Message
      if (triplet.srcAttr + triplet.attr < triplet.dstAttr) {
        Iterator((triplet.dstId, triplet.srcAttr + triplet.attr))
      } else {
        Iterator.empty
      }
    },
    mergeMsg = (a,b) => math.min(a,b) // Merge Message
  )
  println(sssp.vertices.collect.mkString("\n"))

  sparkSession.stop()
}
