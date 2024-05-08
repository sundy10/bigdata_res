package chapter23

object scala04_Cross_boundary_equality extends App {

  /**
   * 23.4 跨界相等性
   *
   * @Scala2实现了通用相等性（universal-equality)，允许对任意两个对象用==和!=方法比较相等性。
   * 这个方案对使用者而言很容易理解，与Java的equals方法也能很好地搭配，因为equals方法允许任何Object与任何其他Object做比较。
   * @这个方案也让Scala2支持协作相等性(cooperative-equality)，即不同的类型可以与其他协作的类型比较相等性。
   * 举例来说，协作相等性允许Scala 2继续像Java那样执行Int和Long之间的相等性比较，而不需要显式地从Int转换成Long。
   */

  /**
   * 尽管如此，通用相等性有一个明显的弊端:它会掩盖bug。例如，在Scala 2中，你可以对一个字符串和一个可选值做比较。这里有一个例子:
   * "hello" ==Option("hello")
   */

  /**
   * 虽然Scala 2在运行期给出的答案是正确的，字符串"hello'的确不等于Option("hello")，但是不可能有任何字符串等于任何可选值。
   * 这类比较的结果将永远是false。因此任何对字符串和可选值的比较都极有可能意味着存在Scala 2编译器没有捕获到的bug。
   *
   * @这类bug很容易通过重构引入:当你把一个变量的类型从String改成Option[String]时，你并没有注意到在其他地方:实际上已经对String和Option[String]做比较了。
   *
   * 与此不同的是，在Scala 3中尝试同样的比较时会在编译期报错:
   */
  //"hello" == Option("hello")

  /**
   * Scala 3通过一个被称为跨界相等性(multiversal equality)的新特性完成了这个安全性方面的改进。
   *
   * @跨界相等性是Scala3针对Scala2的编译器为==和!=方法所做的特殊处理的增强。
   * 示例23.10给出的==和!=方法定义在Scala 3和Scala 2中是一样的。
   * 在Scala 3中，只有编译器对==和!=方法的处理有所改变，即从通用相等性转换成了跨界相等性。
   *
   * 示例23.10 在Scala 2和Scala 3中都有的=和!=方法
   * //对 Any类:
   * final def ==(that :Any): Boolean
   * final def !=(that :Any): Boolean
   */

  /**
   * 为了理解Scala 3的跨界相等性，我们需要先弄明白Scala 2实现通用相等性的细节。
   * 在JVM平台上是这样工作的:当Scala 2编译器遇到对==或!=方法的调用时，它首先会检查参与比较的操作元类型是否为基本类型。
   * 如果参与比较的操作元类型都是基本类型，则编译器会生成特殊的Java字节码来实现高效的对这些基本类型的相等性比较。
   * 而如果参与比较的操作元类型中一方是基本类型而另一方不是，则编译器会对基本类型的那个值做装箱处理。这样一来，参与比较的两个操作元类型就都是引用类型了。
   *
   * 接下来，编译器生成首先检查左操作元是否为null的代码。如果左操作元为null，则编译器生成的代码会检查右操作元是否为null以给出Boolean结果，
   * 从而确保对==和!=方法的调用永远不会抛出NullPointerException。如果右操作元不是null，则生成的代码会在左操作元(现已知为非null)上调用equals方法，并传入右操作元。
   *
   * Scala 3编译器会执行与Scala 2一模一样的步骤，但是 是在通过查找 一个名称为CanEqual的类型族上下文参数实例 来确定这个比较操作是被允许的 之后。CanEqual的定义如下:
   * sealed trait CamEqual[-L,-R]
   */

  /**
   * 这个CanEqual特质接收两个类型参数，即L和R，
   * (虽然“类型族”通常指的是一组存在可用的接收“单个”类型参数的特质 的上下文参数实例的类型，但是你可以把CanEqual特质当作定义了一组由“成对的”类型构成的类型。
   * 举例来说，String和Option[String]之间的相等性比较之所以在Scala 3中不
   * 能通过编译，是因为(String,Option[String])这个(成对的）类型并不在组成CanEqual类型族的集)
   *
   * L是相等性比较的左操作元的类型;而R是右操作元的类型。CanEqual并不提供任何实际上对L和R类型的对象进行相等性比较的方法，
   * 因为这些比较在Scala 3中仍然是通过==和!=方法来完成的。
   * 简言之，CanEqual并不会像你通常预期一个类型族那样提供针对L和R类型的相等性比较服务，
   * 它只是允许(give permission)L和R类型使用==和!=方法来进行相等性比较。
   */

  /**
   * 如18.6节所述，类型参数旁边的减号意味着CanEqual同时对L和R类型逆变。
   * 由于这个逆变的存在，CanEqual[Any, Any]类型是任何CanEqual[L，R]类型的子类型，无论L和R类型是什么。
   * 如此一来，CanEqual[Any, Any]的实例可以被用来允许对任意两个类型做相等性比较。
   * 举例来说，当我们需要一个CanEqual[Int, Int]来允许两个Int类型进行相等性比较时，只需要一个CanEqual[Any,Any]的上下文参数实例就可以满足了，
   * 因为CanEqual[Any,Any]是CanEqual[Int, Int]的子类型。正因为如此，CanEqual被定义为只有一个实例的密封特质，
   * 这个实例拥有通用的类型签名CanEqual[Any, Any]。这个对象被命名为derived，被声明在CanEqual伴生对象中:
   *
   * object CanEqual:
   * object derived extends CanEqual[Any, Any]
   */

  /**
   * 因此，要提供CanEqual[L，R]的上下文参数实例，无论L和R类型具体是什么，都必须使用CanEqual有且仅有的实例CanEqual.derived.
   *
   * 由于向后兼容的需要，即使不存在满足要求的CanEqual上下文参数实例，
   * Scala 3也会默认允许某些相等性比较。对于任何两个L和R类型之间的相等性比较，如果编译器没有找到类型为CanEqual[L,R]的上下文参数实例，
   * 则当如下条件中的任意一条为真时，编译器仍然会允许这个比较操作:
   * 1.L和R是相同的类型。
   * 2.在被抬升(lift)之后，L是R的子类型，或者R是L的子类型。 --(如果要抬升一个类型，则编译器会把类型协变点上的抽象类型替换成其上界，把类型逆变点上的改良类型替换成其父类型)
   * 3.不存在针对L或R类型的自反（reflexive) CanEqual实例。所谓自反实例，指的是允许与自己比较类型的实例，如CanEqual[L, L].
   *
   * 第三条规则可以确保，一旦你提供了一个允许与自己比较类型的自反的CanEqual上下文参数实例，这个类型就不能与其他类型比较，除非存在一个允许该比较的CanEqual上下文参数实例。
   * 从本质上讲，Scala 3为了向后兼容，对那些没有定义自反的CanEqual实例的类型做比较时，会默认回归到通用相等性的逻辑。
   */

  /**
   * Scala 3为若干个标准类库的类型提供了上下文参数实例，包括对字符串的自反实例。
   * 这就是对String和Option[String]的比较默认不被允许的原因。
   * 这个由标准类库提供的CanEqual[String, String]实例足够让Scala 3编译器默认不允许对String和Option[String]进行相等性比较。
   */

  /**
   * 这个默认行为让我们可以从Scala 2平稳升级到Scala 3，因为那些从Scala 2移植的用户代码不会有针对它们的类型的CanEqual实例。
   * 举例来说，假设你的Scala 2工程包含一个Apple类，定义如下:
   */
  case class Apple(size: Int) derives CanEqual

  /**
   * 而在代码的某处有对两个苹果的比较，就像这样:
   */
  val appleTwo =Apple(2)
  val appleTwoToo =Apple(2)
  println(appleTwo == appleTwoToo)

  /**
   * 这个比较操作在Scala 3中默认会继续编译、继续正常工作，
   * 因为左右两侧是相同的类型。然而，由于Scala 3仍然会默认允许那些不存在自反CanEqual上下文参数实例的类型做比较，因此下面这样不太好的相等性比较仍然会通过编译:
   */
  case class Orange(size:Int)

  val orangeTwo: Orange = Orange(2)
  //println(appleTwo == orangeTwo) true

  /**
   * 这个比较很可能是一个bug，因为对任何Apple和Orange的比较总会返回false。
   * 为了得到Scala 3对相等性比较的完整的健全检查，即使对于涉及的类型没有定义自反实例，也可以启用“严格相等性”。
   * 你可以对编译器给出命令行参数-language:strictEquality，或者在代码源文件中包含下列引入∶
   *
   * 启用严格相等性之后，你就会在比较苹果和橙子时得到“想要的”编译器报错:
   */
  import scala.language.strictEquality
  //println(appleTwo == orangeTwo)
  /**
   * 不幸的是，现在对苹果和苹果做比较时，也会得到这样的编译器报错,这大概是你“不想要的”:
   *
   * 要在严格相等性的情况下允许这个比较，可以提供一个CanEqual实例来允许苹果和苹果进行相等性比较。
   * 你可以在Apple伴生对象中提供一个显式的上下文参数实例，如示例23.11所示，尽管这不是Scala的惯用写法。
   */
  //显式的上下文参数实例（不推荐这样写)
//  object Apple:
//    given canEq:CanEqual[Apple,Apple] = CanEqual.derived

  /**
   * 更好的方式是标记一个你想要的针对你的Apple实例的派生的(derived)CanEqual实例，如示例23.12所示。
   * case class Apple(size: Int) derives CanEqual //符合Scala惯用写法
   */

  /**
   * @这个符合Scala惯用写法的方案利用了类型族派生(typeclassderivation)，
   * @这是一个允许将类型族实例的定义交给该类型族的伴生对象中名称为derived的成员来代为实现的特性。
   * @这个派生子句会让编译器在示例23.11所示的Apple伴生对象中插入一个上下文参数的提供者。
   */

  /**
   * 关于derives还有很多可以讲的内容，
   * @因为大多数派生的方法都使用编译期的元编程来生成类型族实例。这些更为通用的类型族派生技巧将在《Scala高级编程》中详细探讨。
   *
   * 既然你已经通过derives子句定义了CanEqual[Apple，Apple]上下文参数实例，那么编译器将允许你在严格相等性的情况下对苹果进行比较:
   */
  println(appleTwo == appleTwoToo)

   
}
