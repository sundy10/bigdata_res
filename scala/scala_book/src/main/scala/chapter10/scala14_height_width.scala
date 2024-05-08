package chapter10

object scala14_height_width extends App {

  def elem(contents: Vector[String]): Element =
    VectorElement(contents)

  def elem(chr: Char, width: Int, height: Int): Element =
    UniformElement(chr, width, height)

  def elem(line: String): Element =
    LineElement(line)


  //val line = elem('-', 1, 1)


  /**
   * 10.14 增高和增宽
   *
   * 我们还需要最后一个增强。示例10.11给出的Element类并不是往够用，因为它不允许使用方将不同宽度的元素叠加在一起，或者将不同高度的元素并排放置。
   * 例如，对如下表达式求值不能正常工作，因为第二行合起来的元素比第一行长:
   * 同理，对第二个表达式求值也不能正常工作，因为第一个VectorElement的高度是2，而第二个VectorElement的高度是1:
   */

  println(elem(Vector("hello")) beside elem(Vector("world!")))
  println(elem(Vector("one", "two")) beside elem(Vector("one","two")))

  /**
   * 示例10.13展示了一个私有的助手方法widen，接收一个宽度参数并返回这个宽度的元素。
   * 结果包含了这个Element元素的内容，且两侧用空格填充，以达到要求的宽度。
   * 示例10.13还展示了另一个类似的方法heighten，用于实现同样的功能，只不过方向变成了纵向的。
   * above方法可以调用widen方法来确保叠加起来的元素拥有相同的宽度。
   * 同样地,beside方法可以调用heighten方法来确保并排放置的元素拥有相同的高度。
   * 做了这些改变之后，我们的这个布局类库就可以用起来了
   */
  abstract class Element {
    def contents: Vector[String]

    def width: Int = if height == 0 then 0 else contents(0).length

    def height: Int = contents.length

    def above(that: Element): Element = {
      val this1 = this.widen(that.width)
      val that1 = this.widen(this.width)
      elem(this1.contents ++ this1.contents)
    }


    def beside(that: Element): Element =
      VectorElement(
        for (line1, line2) <- this.contents.zip(that.contents) yield line1 + line2
      )

    def widen(w: Int): Element =
      if w <= width then this
      else
        val left = elem(' ', (w - width) / 2, height)
        val right = elem(' ', w - width - left.width, height)
        left beside this beside right

    def heighten(h: Int): Element =
      if h <= height then this
      else
        val top = elem(' ', width, (h - height) / 2)
        val bot = elem(' ', width,h-height-top.height)
        top above this above bot

    override def toString: String = contents.mkString("\n")
  }

  class UniformElement(ch: Char, override val width: Int, override val height: Int) extends Element {
    private val line = ch.toString * width

    def contents: Vector[String] = Vector.fill(height)(line)
  }

  class VectorElement(val contents: Vector[String]) extends Element


  class LineElement(s: String) extends Element {
    val contents = Vector(s)

    override def width: Int = s.length

    override def height: Int = 1
  }

}
