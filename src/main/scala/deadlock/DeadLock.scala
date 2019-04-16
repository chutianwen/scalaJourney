package deadlock

import java.util

object DeadLock extends App{
  class TreeNode{
    val children = new util.ArrayList[TreeNode]
    var parent: TreeNode = _

    def addChild(child: TreeNode): Unit = synchronized{
      if(!this.children.contains(child)){
        this.children.add(child)
        child.setParentOnly(this)
      }
    }

    def addChildOnly(child: TreeNode):Unit = synchronized{
      if(!this.children.contains(child)) this.children.add(child)
    }

    def setParent(parent: TreeNode): Unit = synchronized{
      this.parent = parent
      parent.addChildOnly(this)
    }

    def setParentOnly(parent: TreeNode): Unit = synchronized{
      this.parent = parent
    }
  }

  val parent = new TreeNode()
  val child = new TreeNode()

  val run1 = new Runnable(){
    override def run(): Unit = {
      println("t1 begin")
      parent.addChild(child)
      println("t1 end")
    }
  }
  val run2 = new Runnable(){
    override def run(): Unit = {
      println("t2 begin")
      child.setParent(parent)
      println("t2 end")
    }
  }
  for(_ <- 0 to 100) {
    val t1 = new Thread(run1)
    val t2 = new Thread(run2)
    t1.start()
    t2.start()
    t1.join()
    t2.join()
  }
  println("Done")

}
