package chapter10

object scala11_combine_inherit extends App {

  abstract class Element {
    def contents: Vector[String]

    def height: Int = contents.length

    def width: Int = if height == 0 then 0 else contents(0).length
  }

  /**
   * 10.11 使用组合和继承
   *
   *
   *  组合和继承是两种 用其他已有的类来定义新类的方式。
   *  如果你主要追求的是代码复用，则一般来说应当优先选择组合而不是继承。
   *  只有继承才会受到脆弱基类问题的困扰，会在修改超类时不小心破坏子类的代码。
   */

  /**
   * 关于继承关系，你可以问自己一个问题，那就是要建模的这个关系是否是“is-a”(是一个)的关系。
   * 例如，我们有理由说VectorElement类是一个Element类。
   *
   * 另一个可以问的问题是这些类的使用方是否会把子类的类型当作超类的类型来使用。
   * 以VectorElement类为例，我们确实预期 使用方会将VectorElement类作为Element类来用。
   */

  /**
   * 如果你对图10.3所示的类继承关系发问上述两个问题，有没有哪个关系看上去比较可疑?
   * 具体来说，你是否觉得LineElement类理应是一个VectorElement类呢?你是否认为 使用方需要把LineElement类当作VectorElement类来用呢?
   */

  /**
   * 事实上，我们将LineElement类定义为VectorElement类的子类的主要目的是复用VectorElement类的contents字段定义。
   * 因此，也许更好的做法是将LineElement类定义为Element类的直接子类，就像这样:
   */

  class LineElement(s:String) extends Element{
    val contents = Vector(s)

    override def width: Int = s.length

    override def height: Int = 1
  }

  /**
   * 在前一个版本中，LineElement类有一个与VectorElement类的继承关系，它继承了contents字段。
   * 现在LineElement类有一个与Vector的组合关系:它包含了一个从自己的contents字段指向一个字符串向量的引用。有了这个版本的LineElement实现，Element类的类继承关系如图10.4所示。
   */
}
