package graphtraversal
import java.util

import Utils.SparkApp
import org.apache.spark.graphx._
import org.apache.spark.graphx.lib.ShortestPaths
import org.apache.spark.graphx.lib.ShortestPaths.SPMap
// Import random graph generation library
import org.apache.spark.graphx.util.GraphGenerators


object graphXShortestPath extends SparkApp {

  case class NodeAttr(id: Long)
  case class DistanceFromSrc(dist: Double)
  // A graph with edge attributes containing distances
  val graph: Graph[Long, Double] = GraphGenerators.logNormalGraph(sc, numVertices = 5).mapEdges(e => e.attr.toDouble)
  println(graph.vertices.collect.mkString("\t"))

  val sourceId: VertexId = 3 // The ultimate source

  // Initialize the graph such that all vertices except the root have distance infinity.
  val initialGraph: Graph[(DistanceFromSrc, NodeAttr), Double] = graph.mapVertices((id, vd) =>
    (DistanceFromSrc(if (id == sourceId) 0.0 else Double.PositiveInfinity), NodeAttr(id))
  )

  println(initialGraph.vertices.collect.mkString("\t"))
  println(initialGraph.edges.collect.mkString("\t"))
  println(initialGraph.triplets.collect.mkString("\t"))

  val sssp: Graph[(DistanceFromSrc, NodeAttr), Double] = initialGraph.pregel(DistanceFromSrc(Double.PositiveInfinity))(
    // Vertex Program, and newDist is the message, we need a method of vprog to create a new value of vd by combining this newDist message
    vprog = (id, vd, newDist) => (DistanceFromSrc(math.min(vd._1.dist, newDist.dist)), vd._2),
    sendMsg = triplet => {  // Send Message
      if (triplet.srcAttr._1.dist + triplet.attr < triplet.dstAttr._1.dist) {
        Iterator((triplet.dstId, DistanceFromSrc(triplet.srcAttr._1.dist + triplet.attr)))
      } else {
        Iterator.empty
      }
    },
    mergeMsg = (a,b) => DistanceFromSrc(math.min(a.dist,b.dist)) // Merge Message
  )
  println(sssp.vertices.collect.mkString("\n"))

//  val res: Graph[SPMap, Double] = ShortestPaths.run(initialGraph, graph.vertices.collect.map(_._1))
//  val resWithAttr: Graph[(SPMap, (DistanceFromSrc, NodeAttr)), Double] = ShortestPathsWithAttribute.runAllVerticesAsDst(initialGraph)
//
//  res.vertices.collectAsMap.foreach(println)
//  resWithAttr.vertices.collectAsMap.foreach(println)

//  val bits1 = new util.BitSet(16)
//  bits1.and(bits1)

//  (0,1.0)
//  (1,2.0)
//  (2,1.0)
//  (3,0.0)
//  (4,1.0)
//  (3,Map(0 -> 2, 1 -> 1, 2 -> 1, 3 -> 0, 4 -> 2))
//  (0,2.0)
//  (1,1.0)
//  (2,1.0)
//  (3,0.0)
//  (4,2.0)

}

