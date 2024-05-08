package chapter10

object scala07_call_superclass_construction_method extends App {

  abstract class Element {
    def contents: Vector[String]

    def height: Int = contents.length

    def width: Int = if height == 0 then 0 else contents(0).length
  }

  class VectorElement(val contents: Vector[String]) extends Element
  /**
   * 10.7 调用超类的构造方法
   *
   * 现在你已经拥有一个由两个类组成的完整系统:一个抽象类Element，这个类又被另一个具体类VectorElement扩展。
   * 你可能还会看到使用其他方式来表达一个元素。
   * 比如，使用方可能要创建一个由字符串给出的单行组成的布局元素。
   * 面向对象的编程让我们很容易用新的数据变种来扩展一个已有的系统，只需要添加子类即可。
   * 举例来说，示例10.6给出了一个扩展自VectorElement类的LineElement类:
   */
  class LineElement(s:String) extends VectorElement(Vector(s)){//初始化VectorElement时传入
    override def width: Int = s.length

    override def height: Int = 1
  }

  /**
   * 由于LineElement类扩展自VectorElement类，而VectorElement类的构造方法接收一个参数（Vector[String]),
   * LineElement类需要向其超类的主构造方法传入这样一个入参。
   * 要调用超类的构造方法，只需要将你打算传入的入参放在超类名称后的圆括号里即可。
   * 例如，LineElement类就是通过将Vector(s)放在超类VectorElement名称后面的圆括号里来将其传入VectorElement类的主构造方法的:
   */


}
