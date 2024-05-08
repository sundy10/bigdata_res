package chapter10

object scala12_above_beside_toString extends App {

  /**
   * 10.12 实现above、beside和和toString方法
   *
   * 接下来,我们将实现Element类的above方法。将某个元素放在另一个“上面”意味着将两个元素的值拼接在一起。第一版的above方法可能是这样的:
   */

  abstract class Element {
    def contents: Vector[String]

    def height: Int = contents.length

    def width: Int = if height == 0 then 0 else contents(0).length

    /**
     * 其中，++操作符将两个向量拼接在一起。本章还会讲到向量支持的一些其他方法，然后在第15章还会有更详细的介绍。
     *
     * 前面给出的代码并不是很够用，因为它并不允许你将宽度不同的元素叠加在一起。
     * 不过为了让事情保持简单，我们先不理会这个问题，只是每次都记得传入相同长度的元素给above方法。
     * 在10.14节，我们将对above方法做增强，让 使用方可以用它来拼接不同宽度的元素。
     */
    def above(that: Element): Element =
      VectorElement(this.contents ++ that.contents)

    /**
     * 下一个要实现的方法是beside。要把两个元素并排放在一起，我们将创建一个新的元素。
     * 在这个新元素中，每一行都是由两个元素的对应行拼接起来的。
     * 与之前一样，为了让事情保持简单，我们先假设两个元素有相同的高度。这让我们设计出下面这个beside方法:
     */
    def beside_old(that: Element): Element =
      val newContents = new Array[String](this.contents.length)
      for i <- 0 until this.contents.length do
        newContents(i) = this.contents(i) + that.contents(i)
      VectorElement(newContents.toVector)

    /**
     * 这个beside方法首先分配一个新的数组newContents，
     * 然后用this.contents和that.contents对应的向量元素(即每一行)拼接的字符串数组填充，最后通过调用toVector方法产生一个新的包含新内容的VectorElement类。
     */
    def beside(that: Element): Element =
      VectorElement(
        for (line1, line2) <- this.contents.zip(that.contents) yield line1 + line2
      )

    /**
     * 在这里，我们用zip操作符将this.contents和that.contents这两个向量转换成对偶（即Tuple2)的数组。
     * 这个zip操作符从它的两个操作元中选取对应的元素，组合成一个对偶（pair)的向量。例如，如下表达式:
     */

    /**
     * for表达式有一部分叫作yield，并通i过yield交出结果。这个结果的类型和被遍历的表达式是同一种(向量)。
     * 向量中的每个元素都是将对应的line1和line2拼接起来的结果。
     * 因此这段代码的最终结果与第一版的beside方法一样，不过由于它避免了显式的向量下标，因此获取结果的过程更少出错。
     */

    /**
     * 还需要使用某种方式来显示元素。与往常一样，这是通过定义返回格式化好的字符串的toString方法来完成的。定义如下:
     *
     * “contents.mkString("\n")”这样的表达式将contents向量格式化成一个字符串，且每个向量元素都独占一行。
     */
    override def toString: String = contents.mkString("\n")
  }

  class VectorElement(val contents: Vector[String]) extends Element
  

  class LineElement(s: String) extends Element {
    val contents = Vector(s)

    override def width: Int = s.length

    override def height: Int = 1
  }


  //如果其中一个操作元向量比另一个长，zip操作符将会删除多余的元素。在上面的表达式中，左操作元的第三个元素3并没有进入结果中，因为它在右操作元中并没有对应的元素。
  println(Vector(1,2,3).zip(Vector("a","b")))

}
