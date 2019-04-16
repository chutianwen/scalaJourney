package graphtraversal
import scala.collection.mutable
object DFSWithPriorityQueue extends App{
  val pq = mutable.PriorityQueue((1, 'a'), (2, 'b'), (5,'b'), (3, 'd'), (7, 'e'))
  println(pq.dequeue())

  case class Node(id: String, score: Int)

  val nodeA = Node("<A>", 1)
  val nodeB = Node("<B>", 2)
  val nodeC = Node("<C>", 3)
  val nodeD = Node("<D>", 4)
  val nodeE = Node("<E>", 5)
  val nodeF = Node("<F>", 6)
  val graph = Map(
    nodeA -> Seq(nodeB, nodeC, nodeD),
    nodeB -> Seq(nodeE),
    nodeD -> Seq(nodeF)
  )

  def traverseWithLimit(limit:Int, graph: Map[Node, Seq[Node]]):Unit = {
    println("-----------Test-----------")
    val explored = collection.mutable.Set[Node]()
    var numJumps = 0
    var totalScore = 0

    def dfs(currentNode: Node):Unit = {

      println(s"Currently traversing node $currentNode with numJumps: $numJumps")
      // mark current node as seen
      explored.add(currentNode)
      totalScore += currentNode.score

      val maxHeap = mutable.PriorityQueue(graph.getOrElse(currentNode, Seq()).map(node => (node.score, node)):_*)(Ordering.by {
        case (key:Int, value:Node) => (key, value.id)
      })

      while(maxHeap.nonEmpty){
        val nodeBestNeighbor: (Int, Node) = maxHeap.dequeue()
        if(!explored.contains(nodeBestNeighbor._2) & numJumps < limit){
          numJumps += 1
          dfs(nodeBestNeighbor._2)
        }
      }
    }

    dfs(nodeA)
    println(s"Total score over traversed nodes:$totalScore")
  }

  traverseWithLimit(3, graph)
  traverseWithLimit(5, graph)

}
