package chapter10

object scala09_polymorphism_dynamic_binding extends App {

//  abstract class Element {
//    def contents: Vector[String]
//
//    def height: Int = contents.length
//
//    def width: Int = if height == 0 then 0 else contents(0).length
//  }
//
//  class VectorElement(val contents: Vector[String]) extends Element
//
//  class LineElement(s: String) extends VectorElement(Vector(s)) { //初始化VectorElement时传入
//    override def width: Int = s.length
//
//    override def height: Int = 1
//  }


  /**
   * 10.9 多态和动态绑定
   *
   * 你在10.4节看到了，类型为Element的变量可以指向一个类型为VectorElement的对象。
   * 这个现象叫作多态(polymorphism)，意思是“多个形状”或“多种形式”。
   * 在我们的这个例子中，Element对象可以有许多不同的展现形式。
   * (这一类多态被称为子类型多态( subtyping polymorphism)。Scala还有其他种类的多态，其中通用多态(universal polymorphism）将在第18章做详细介绍,
   * 而特定目的多态(ad hocpolymorphism)将在第21章和第23章做详细介绍。(译者注:全类型多态通常被称为参数多态，即parametric polymorphism。) )
   */

  /**
   * 到目前为止，你看到过两种形式的Element类: VectorElement和LineElement。可以通过定义新的Element子类来创建更多形式的Element类。
   * 例如，可以定义一个新形式的Element类，有一个指定的宽度和高度，并用指定的字符填充:
   */
  import chapter10.scala07_call_superclass_construction_method.*

  class UniformElement(ch: Char, override val width: Int, override val height: Int) extends Element {
    private val line = ch.toString * width

    def contents: Vector[String] = Vector.fill(height)(line)
  }

  /**
   * Element类现在的类继承关系如图10.3所示。有了这些，Scala将会接收如下所有的赋值，因为用来赋值的表达式满足定义变量的类型要求:
   */
  val e1: Element = VectorElement(Vector("hello", "word"))
  val ve: VectorElement = LineElement("hello")
  val e2: Element = ve
  val e3: Element = UniformElement('x', 2, 3)

  /**
   * 如果你检查这个类继承关系，则会发现对这4个val定义中的每一个而言，等号右边的表达式类型都位于等号左边被初始化的val的类型的下方。
   */

  /**
   * 不过，对变量和表达式的方法调用是动态绑定(dynamic bound)的。
   * 意思是说实际被调用的方法实现是在运行时基于对象的类决定的，而不是基于变量或表达式的类型决定的。
   *
   * 为了展示这个行为，我们将从Element类中临时去掉所有的成员，并向Element类中添加一个名称为demo的方法。
   * 我们将在VectorElement和LineElement类中重写demo方法，但在UniformElement类中不重写这个方法:
   */

}

object demo extends App {
  abstract class Element{
    def demo = "Element's implementation invoked"
  }

  class VectorElement extends Element{
    override def demo: String = "VectorElement's implementation invoked"
  }

  class LineElement extends Element{
    override def demo: String = "LineElement's implementation invoked"
  }

  //UniformElement 类继承了 Element 类的demo方法
  class UniformElement extends Element

  def invokeDemo(e:Element)=e.demo

  /**
   * 如果你传入VectorElement参数给invokeDemo方法，则会看到一条消息，表明VectorElement类的demo实现被调用了，尽管变量e（即接收demo实现调用的那个)的类型是Element:
   *
   * 同理，如果你传入LineElement参数给invokeDemo方法，则会看到一条消息,表明LineElement类的demo实现被调用了:
   *
   * 由于UniformElement类中并没有重写demo方法，而是从其超类Element继承了demo实现。因此，当对象的类为UniformElement时，调用demo方法的正确版本就是来自Element关的demo实现。
   */
  println(invokeDemo(new VectorElement))
  println(invokeDemo(new LineElement))
  println(invokeDemo(new UniformElement))

}
