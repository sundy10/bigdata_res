package chapter17

object scala04_define_own_value_type extends App {

  /**
   * 17.4 定义自己的值类型
   *
   * 17.1节提到过，你可以定义自己的值类来对内建的值类进行扩充。
   * 与内建的值类一样，你的值类的实例通常也会被编译成那种不使用包装类的Java字节码。在需要包装类的上下文里，如泛型代码，值将被自动装箱和拆箱。
   * (Scala 3还提供了“不透明类型”(opaque type)，虽然其限制更多，但能保证其值永远不会被装箱。我们将在《Scala高级编程》中详细介绍不透明类型。)
   *
   * 只有特定的几个类可以成为值类。要想使某个类成为值类，它必须有且仅有一个参数，并且在内部除def之外不能有任何其他内容。
   * 不仅如此，也不能有其他类扩展自值类，且值类不能重新定义equals或hashCode方法。
   *
   * 要定义值类，需要将它处理成AnyVal类的子类，并在它唯一的参数前加上val。下面是值类的一个例子:
   */
  class Dollars(val amount: Int) extends AnyVal :
    override def toString: String = "$" + amount

  /**
   * 正如10.6节描述的那样，参数前的val让amount参数可以作为字段被外界访问。
   * 例如，如下代码将创建这个值类的一个实例，然后从中获取其金额(amount) :
   */
  val money = new Dollars(1_000_000)
  println(money.amount)
  println(money)

  /**
   * @在本例中，money指向该值类的一个实例。它在Scala源码中的类型为Dollar，但在编译后的Java字节码中将直接使用Int类型。
   *
   * @这个例子定义了toString方法，并且编译器将识别出什么时候使用这个方法。
   * @这就是打印money将给出$1000000，带上了美元符号，而打印money.amount仅会给出1000000的原因。你甚至可以定义多个同样以Int值支撑的值类型。例按:
   */
  class  SwissFrances(val amount:Int) extends AnyVal:
    override def toString: String = s"$amount CHF"

  /**
   * 虽然Dollars和SwissFrancs在运行时都是以整数呈现的，但是它们在编译期是不同的类型。
   */

  /**
   * @避免类型单一化
   *
   * 要想尽可能发挥Scala类继承关系的好处，可以试着对每个领域概念定义一个新的类，即使复用相同的类实现不同的用途也是可行的。
   * 虽然这样的一个类是所谓的“细微类型”(tiny type)，
   * @既没有方法但是也没有字段，定义这样的一个额外的类有助于编译器在更多的地方帮助你。
   */

  /**
   * 例如，假设你正在编写代码生成HTML。在HTML中，样式名是用字符串表示的，锚定标识符也是如此。
   * HTML自身也是一个字符串，所以只要你想，就可以用字符串定义的助手方法来表示所有这些内容，就像这样:
   */
  def title(text:String,anchor:String,style:String):String =
    s"<a id='$anchor'><h1 class='$style'>$text</h1></a>"

  /**
   * 这个类型签名中出现了4个字符串。这类“字符串类型”(stringly typed）的代码从技术上讲是强类型的，
   * 但由于我们能看到的一切都是字符串类型的，因此编译器并不能帮助我们检测到用错的参数情况。
   * 例如，它并不会阻止你写出这样的滑稽代码:
   */
  println(title("chap:vcls","bold","Value Classes"))

  /**
   * 这段HTML代码完全不正确了。本想用来显示的文本“Value Classes”被用成了样式类，而显示出来的文本是“chap:vcls”，
   * 这本来应该是锚定标识。最后，实际的锚定标识为“bold”，这本来应该是样式类。虽然这些错误都很滑稽，但是编译器“一声不吭”。
   *
   * 如果你对每个领域概念都定义一个细微类型，编译器就能对你更有帮助。
   * @比如，可以分别对样式、锚定标识、显示文本和HTML等都定义一个小类。由于这些类只有一个参数，没有其他成员，因此它们可以被定义成值类:
   */
  class Anchor(val value:String) extends AnyVal
  class Style(val value:String) extends AnyVal
  class Text(val value:String) extends AnyVal
  class Html(val value:String) extends AnyVal

  /**
   * 有了这些类以后，就可以编写一个类型签名更丰富的title方法了:
   * 这时如果你再用错误的顺序调用这个版本的方法，编译器就可以探测到这个错误（并提示你)。
   */
  def title1(text: Text,anchor: Anchor,style: Style):Html =
    Html(
      s"<a id='${anchor.value}'><h1 class='${style.value}'>${text.value}</h1></a>"
    )







}
