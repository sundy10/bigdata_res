package chapter10

object scala04_extension_class extends App {

  abstract class Element {
    def contents: Vector[String]

    def height: Int = contents.length

    def width: Int = if height == 0 then 0 else contents(0).length
  }

  /**
   * 10.4 扩展类
   *
   * 我们仍然需要使用某种方式创建新的元素对象。你已经看到"new Element”是不能用的，因为Element类是抽象的。
   * 因此，要实例化一个元素，需要创建一个扩展自Element类的子类，并实现contents这个抽象方法。示例10.3给出了一种可能的做法:
   *
   * VectorElement类被定义为扩展( extend)）自Element类。与Java—样，可以在类名后面用extends子句来表达:
   */

  class VectorElement(conts: Vector[String]) extends Element {
    def contents: Vector[String] = conts
  }

  /**
   * 这样的extends子句有两个作用:第一，它使得VectorElement类从Element类继承(inherit)所有非私有的成员;
   * 第二，它使得VectorElement类型成为Element类型的子类型(subtype)。
   * 反过来讲，Element类是VectorElement类的超类(superclass)。
   * 如果去掉extends子句，则Scala编译器会默认你的类扩展自scala.AnyRef，对应到Java平台，这与java.lang.Object相同。
   * 因此，Element类默认也扩展自AnyRef类。图10.1展示了这些继承关系。
   */

  /**
   * 继承（ inheritance)的意思是超类的所有成员也是子类的成员，但是有两个例外:
   * 一是超类的私有成员并不会被子类继承;
   * 二是如果子类里已经实现了相同名称和参数的成员，则该成员不会被继承。
   *
   * 对于后面这种情况，我们认为子类的成员重写了超类的成员。如果子类的成员是具体的而超类的成员是抽象的，
   * 我们就认为这个具体的成员实现（ implement)了那个抽象的成员。
   */

  /**
   * 例如，VectorElement类中的contents方法重写（或者说实现)了Element类的抽象方法contents。
   * (这个设计有一个缺陷，我们目前并没有确保contents方法的每个String元素都有相同的长度。这个问题可以通过在主构造方法中检查前提条件并在前提条件不满足时抛出异常来解决。)
   * 与此不同的是，VectorElement类从Element类继承了width和height这两个方法。
   * 例如，假设有一个VectorElement ve，可以用ve.width来查询其宽度，就像width方法是定义在VectorElement类中的一样:
   * (6.2节曾经提到过，当实例化接收参数的类(如VectorElement类)时，可以省略new关键字。)
   */
  val ve = VectorElement(Vector("hello", "world"))

  /**
   * 子类型的意思是子类的值可以被用在任何需要超类的值的场合。
   */
  val e: Element = VectorElement(Vector("hello"))

  /**
   * 变量e的类型是Element，因此用于初始化它的值的类型也应该是Element。
   * 事实上，初始值的类型是VectorElement。这是可行的，因为VectorElement类扩展自Element类，也就是说,VectorElement类型是与Element类型兼容的。
   */

  /**
   * 图10.1还展示了VectorElement和Vector[String]之间存在的组合(composition）关系。
   * 这个关系被称为组合，是因为VectorElement是通过使用Vector[String]组合出来的，
   * @Scala编译器会在为VectorElement生成的二进制类文件中放入一个指向传入的conts向量的字段。
   * 我们将在后面的10.11节探讨关于组合和继承的设计考量点。
   */
}
