package graphtraversal
import scala.reflect.ClassTag

import org.apache.spark.graphx._

/**
  * Computes shortest paths to the given set of landmark vertices, returning a graph where each
  * vertex attribute is a map containing the shortest-path distance to each reachable landmark.
  */
object ShortestPathsWithAttribute {
  /** Stores a map from the vertex id of a landmark to the distance to that landmark. */
  type SPMap = Map[VertexId, Int]

  private def makeMap(x: (VertexId, Int)*) = Map(x: _*)

  private def incrementMap(spmap: SPMap): SPMap = spmap.map { case (v, d) => v -> (d + 1) }

  private def addMaps(spmap1: SPMap, spmap2: SPMap): SPMap =
    (spmap1.keySet ++ spmap2.keySet).map {
      k => k -> math.min(spmap1.getOrElse(k, Int.MaxValue), spmap2.getOrElse(k, Int.MaxValue))
    }.toMap

  /**
    * Computes shortest paths to the given set of landmark vertices.
    *
    * @tparam ED the edge attribute type (not used in the computation)
    *
    * @param graph the graph for which to compute the shortest paths
    * @param landmarks the list of landmark vertex ids. Shortest paths will be computed to each
    * landmark.
    *
    * @return a graph where each vertex attribute is a map containing the shortest-path distance to
    * each reachable landmark vertex.
    */
  def run[VD, ED: ClassTag](graph: Graph[VD, ED], landmarks: Seq[VertexId]): Graph[SPMap, ED] = {
    val spGraph: Graph[SPMap, ED] = graph.mapVertices { (vid, attr) =>
      if (landmarks.contains(vid)) makeMap(vid -> 0) else makeMap()
    }

    val initialMessage = makeMap()

    def vertexProgram(id: VertexId, attr: SPMap, msg: SPMap): SPMap = {
      addMaps(attr, msg)
    }

    def sendMessage(edge: EdgeTriplet[SPMap, _]): Iterator[(VertexId, SPMap)] = {
      val newAttr = incrementMap(edge.dstAttr)
      if (edge.srcAttr != addMaps(newAttr, edge.srcAttr)) Iterator((edge.srcId, newAttr))
      else Iterator.empty
    }

    Pregel(spGraph, initialMessage)(vertexProgram, sendMessage, addMaps)
  }

  def runAllVerticesAsDst[VD, ED: ClassTag](graph: Graph[VD, ED]): Graph[(SPMap, VD), ED] = {
    val spGraph: Graph[(SPMap, VD), ED] = graph.mapVertices{(vid, attr) =>
      (makeMap(vid -> 0), attr)
    }

    val initialMessage = makeMap()

    def vertexProgram(id: VertexId, attr: (SPMap, VD), msg: SPMap): (SPMap, VD) = {
      (addMaps(attr._1, msg), attr._2)
    }

    def sendMessage(edge: EdgeTriplet[(SPMap, VD), _]): Iterator[(VertexId, SPMap)] = {
      val newAttr = incrementMap(edge.dstAttr._1)
      if (edge.srcAttr._1 != addMaps(newAttr, edge.srcAttr._1)) Iterator((edge.srcId, newAttr))
      else Iterator.empty
    }
    Pregel(spGraph, initialMessage)(vertexProgram, sendMessage, addMaps)
  }
}


