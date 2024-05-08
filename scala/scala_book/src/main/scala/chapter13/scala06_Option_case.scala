package chapter13

object scala06_Option_case extends App{

  /**
   * 13.6 对Option进行模式匹配
   *
   * 可以用模式匹配来处理Scala的标准类型Option。
   * 正如我们在第3章第12步中提到的，Option的值可以有两种形式: Some(x)，其中x是那个实际的值;None对象，代表没有值。
   *
   * Scala集合类的某些标准操作会返回可选值。比如，Scala的Map有一个get方法，当传入的键有对应的值时，返回Some(value);
   * 而当传入的键在Map中没有定义时，则返回None。我们来看下面这个例子:
   */
  val capitals = Map("France" -> "Paris", "Japan" -> "Tokyo")
  println(capitals.get("France"))     //Some(Paris)
  println(capitals.get("North Pole")) //None

  /**
   * @将可选值解开的最常见的方式是通过模式匹配。例如:
   */
  def show(x:Option[String])=
    x match {
      case Some(s) =>s
      case None => "?"
    }
  println(show(capitals.get("Japan")) )
  println(show(capitals.get("France")) )
  println(show(capitals.get("North Pole")) )

  /**
   * Scala程序经常用到Option类型。可以把它与Java中用null来表示无值做比较。
   * 举例来说，java.util.HashMap的get方法要么返回存放在HashMap中的某个值，要么（在值未找到时)返回null。
   * @这种方式对Java来说是可行的，但很容易出错，因为在实践中想要跟踪某个程序中的哪些变量可以为null是一件很困难的事情。
   *
   * @如果某个变量允许为null，那么你必须记住在每次用到它的时候都要判空(null)。如果忘记了，则运行时就有可能抛出NullPointerException。
   * 由于这样类异常可能并不经常发生，因此在测试过程中很难发现。
   * 对Scala而言，在这种情况下完全不能工作，因为Scala允许在哈希映射中存放值类型的数据，而null并不是值类型的合法元素。
   * 例如，一个HashMap[Int,Int]不可能通过返回null来表示"无值”。
   *
   * @Scala鼓励我们使用Option来表示可选值。这种处理可选值的方式与Java的方式相比有若干优势。
   * 首先，对代码的读者而言，某个类型为Option[String]的变量对应一个可选的String，与某个类型为String的变量是一个可选的String (可能为null）相比，要直观得多。
   * 不过最重要的是，我们之前描述的那种不检查某个变量是否为null就开始使用它的编程错误，在Scala中直接变成了类型错误。
   * 如果某个变量的类型为Option[String]，而我们把它当作String来使用，则这样的Scala程序是无法通过编译的。
   */

}
