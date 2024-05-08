package chapter10

object scala13_factory extends App {


  /**
   * 10.13 定义工厂对象
   *
   * 现在你已经拥有一组用于布局元素的类。这些类的继承关系可以“原样”展现给你的使用方，不过你可能想把继承关系隐藏在一个工厂对象背后。
   * 工厂对象包含创建其他对象的方法。使用方 用这些工厂方法来构建对象，而不是直接用new来构建对象。
   *
   * @这种做法的好处是对象创建逻辑可以被集中起来，而对象是如何用具体的类表示的可以被隐藏来。
   * @这样既可以让你的类库更容易被使用方理解，因为暴露的细节更少，又提供了更多的机会，可以让你在未来不破坏使用方代码的前提下改变类库的实现。
   */

  /**
   * 为布局元素构建工厂对象的第一个任务是选择在哪里放置工厂方法。
   * 工厂方法应该作为某个单例对象的成员，还是类的成员?包含工厂方法的对象或类应该如何命名?可能性有很多。
   * 直接的方案是创建一个Element类的伴生对象，作为布局元素的工厂对象。这样，你只需要暴露Element这组类对象给 使用方，并将VectorElement、LineElement和UniformElement这3个实现类隐藏起来。
   *
   * 示例10.10给出了按这个机制做出的Element对象设计。Element对象包含了3个重载的elem方法，每个方法用于构建不同种类的布局对象。
   */

//  object Element_old {
//
//    def elem(contents: Vector[String]): Element =
//      VectorElement(contents)
//
//    def elem(chr: Char, width: Int, height: Int): Element =
//      UniformElement(chr, width, height)
//
//    def elem(line: String): Element =
//      LineElement(line)
//
//  }

  /**
   * 有了这些工厂方法以后，我们有理由对Element类的实现做一些改变，让它用elem工厂方法，而不是直接显式地创建新的VectorElement。
   * 为了在调用工厂方法时不显式给出Element这个单例对象名称的限定词，我们将在源码文件顶部引入Element.elem。
   * 换句话说，我们在Element类中不再用Element.elem来调用工厂方法，而是引入Element.elem，这样就可以用其简称（即elem)来调用工厂方法了。
   * 示例10.11给出了调整后的Element类。
   *
   * 除此之外，在有了工厂方法后，VectorElement、LineElement和UniformElement这些子类就可以变成私有的，因为它们不再需要被使用方直接访问了。
   *
   * @在Scala中，可以在其他类或单例对象中定义类和单例对象。
   * @对于将Element类的子类变成私有的，方式之一是将其子类放在Element单例对象中，并声明为私有的。
   * 这些类在需要时仍然可以被那3个elem工厂方法访问。示例10.12给出了修改后的样子。
   */

  object Element {

    private class VectorElement(val contents: Vector[String]) extends Element

    private class LineElement(s: String) extends Element :
      val contents = Vector(s)
      override def width: Int = s.length
      override def height: Int = 1

    private class UniformElement(ch: Char, override val width: Int, override val height: Int) extends Element :
      private val line = ch.toString * width
      def contents: Vector[String] = Vector.fill(height)(line)


    def elem(contents: Vector[String]): Element =
      VectorElement(contents)

    def elem(chr: Char, width: Int, height: Int): Element =
      UniformElement(chr, width, height)

    def elem(line: String): Element =
      LineElement(line)

  }






  ///////////////////////////////////////////////////////////////////////////////////////////


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
//    def above(that: Element): Element =
//      VectorElement(this.contents ++ that.contents)

    /**
     * 下一个要实现的方法是beside。要把两个元素并排放在一起，我们将创建一个新的元素。
     * 在这个新元素中，每一行都是由两个元素的对应行拼接起来的。
     * 与之前一样，为了让事情保持简单，我们先假设两个元素有相同的高度。这让我们设计出下面这个beside方法:
     */
//    def beside_old(that: Element): Element =
//      val newContents = new Array[String](this.contents.length)
//      for i <- 0 until this.contents.length do
//        newContents(i) = this.contents(i) + that.contents(i)
//      VectorElement(newContents.toVector)

    /**
     * 这个beside方法首先分配一个新的数组newContents，
     * 然后用this.contents和that.contents对应的向量元素(即每一行)拼接的字符串数组填充，最后通过调用toVector方法产生一个新的包含新内容的VectorElement类。
     */
//    def beside(that: Element): Element =
//      VectorElement(
//        for (line1, line2) <- this.contents.zip(that.contents) yield line1 + line2
//      )

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

}
