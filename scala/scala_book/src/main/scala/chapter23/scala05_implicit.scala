package chapter23

object scala05_implicit extends App {

  /**
   * 23.5 隐式转换
   *
   * @隐式转换是Scala的首个隐式构件。
   * @它的用途是通过移除样板式的类型转换来让代码变得更加清晰和精简。
   * 例如，Scala的标准类库定义了从Int类型到Long类型的隐式转换。
   * 如果你向某个预期为Long值的方法传入Int值，则编译器会自动将Int类型适配成Long类型，而不需要你显式地调用某个类型转换函数，如toLong。
   * 由于任何Int类型都可以被安全地转换成Long类型，而这两种类型都表示二进制补码的整数，因此这个隐式转换可以通过移除样板代码来让代码更易于理解。
   */

  /**
   * 不过，随着时间的推移，隐式转换逐渐不再被推崇，因为除了通过移除样板代码来让代码更清晰，也有可能因为不再显式可见而让代码变得更加难以理解。
   * 同时，其他构件被陆续加入Scala，它们提供了比隐式转换更好的方式，如扩展方法和上下文参数。
   * 就Scala 3而言，隐式转换只适用于少量使用场景。虽然它仍然被支持，但是你必须引入一个特性标志，以在不触发编译器告警的情况下使用它。
   */

  /**
   * @隐式转换的工作机制如下:如果Scala编译器判定某个类型不满足该位置所预期的类型，则它将查找能够解决类型错误的候选隐式转换。
   * 换句话说，每当编译器看到X但它需要Y时，它就会查找能将X转换成Y的隐式转换。举例来说，假设你有一个用来表示某个地址中的街道片段的细微类型，
   * 比如:(我们在17.4节讨论过细微类型。)
   *
   * 而且你有这个细微类型的一个实例，比如:
   */
  case class Street(value: String)

  val street: Street = Street("123 Main St")

  /**
   * 那么，你不能用Street初始化一个字符串类型的变量:
   * val streeStr: String =street
   *
   * 你需要调用street.value并显式地将Street转换或String:
   */
  val streeStr: String = street.value

  /**
   * 这个代码很清晰，不过你可能会觉得对Street调用value来将它转换成String并不会添加多少信息的样板代码。
   * 由于将Street转换成其底层的String总是安全的，因此你可能会决定提供一个从Street到String的隐式转换。
   * 在Scala 3中，你可以定义类型为Conversion[Street,String]的上下文参数实例。
   * (在Scala 3中，还可以用implicit def来定义隐式转换,这是为了与Scala2兼容。这种方式可能会在未来的版本中被废除。)
   * 这是函数类型Street => String的一个子类型，其定义如下:
   *
   * abstract class Conversion[-T, +U] extends (T => U):
        def apply(x:T):U
   */

  /**
   * 由于Conversion特质只有一个抽象方法，因此通常可以用SAM函数字面量来定义它的实例。(我们在8.9节介绍了SAM。)
   * 你可以像这样来定义从Street到String的隐式转换:
   */
  given streetToString:Conversion[Street,String] = _.value

  /**
   * 为了在不触发编译器告警的情况下使用隐式转换，必须启用隐式转换，且要么通过-language: implicitConversions全局开启，要么通过下面的引入局部开启:
   */
  import scala.language.implicitConversions

  /**
   * 在启用隐式转换之后，假设streetToString这个上下文参数实例是以单个标识符的形式存在于当前作用域的，你就可以编写如下代码:
   */
  val streetStr:String = street

  /**
   * 这里发生的是，编译器在一个要求String的上下文中看到了一个Street。
   * 到目前为止，编译器所看到的只是一个普通的类型错误。在它放弃编译之前，还会查找一个从Street到String的隐式转换。
   * 就本例而言，它会找到streetToString。于是编译器会自动地插入一个对streetToString的应用。在幕后，代码就变成了:
   */
  val streetStr1:String = streetToString(street)

  /**
   * 从字面意思上讲，这就是一个隐式的（ implicit）转换。你并没有显式地要求转换，
   * 而是通过将streetToString当作一个上下文参数实例添加到作用域中的方式把streetToString标记为可用的隐式转换。
   * 然后，编译器会在它需要把Street转换成String的时候使用这个隐式转换。
   */

  /**
   * 当你定义隐式转换时，应当确保这个转换永远是合适的。
   * 举例来说，将Double类型隐式地转换成Int类型会引起其他人的不满，因为让某个可能损失精度的事情以不可见的方式发生是很可疑的。
   * 因此，这并不是一个我们真的会推荐的隐式转换。
   * 而另一个方向则更为合理，也就是从某种更受限的类型转换到某种更通用的类型。
   * 例如，Int类型可以在不损失精度的情况下转换成Double类型，因此从Int类型到Double类型的隐式转换是合理的。
   * @事实上，这是实际发生的。在scala.Predef这个会被隐式地引入每个Scala程序的对象中定义了将“较小的”数值类型转换成“较大的”数值类型的隐式转换，
   * 包括从Int类型到Double类型的转换。
   */

  /**
   * 这就是Scala的Int值可以被存放在类型为Double的变量中的原因。类型系统中并没有针对这个的特殊规则，只是应用了一个隐式转换。
   *    (不过，Scala编译器后端会对这个隐式转换特殊对待，并将它翻译成特殊的“i2d”字节码。因此,编译出来的映像与Java是一致的。)
   */

}
