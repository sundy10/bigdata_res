package chapter13

object scala07_Patterns_are_everywhere extends App {

  sealed trait Expr

  case class Var(name: String) extends Expr

  case class Num(number: Double) extends Expr

  case class UnOp(operator: String, arg: Expr) extends Expr

  case class BinOp(operator: String, left: Expr, right: Expr) extends Expr

  val capitals = Map("France" -> "Paris", "Japan" -> "Tokyo")
  println(capitals.get("France"))     //Some(Paris)
  println(capitals.get("North Pole")) //None

  /**
   * 13.7 到处都是模式
   *
   * 在Scala中，很多地方都允许使用模式，并不仅限于match表达式。我们来看看其他能够使用模式的地方。
   */

  /**
   * @<变量定义中的模式>
   *
   * 每当我们定义一个val或var时，都可以用模式而不是简单的标识符。
   * 例如，可以将一个元组解开并将其中的每个元素分别赋值给不同的变量，参考示例13.17。
   */
  //示例用单个赋值定义多个变量
  val myTuple = (123, "abc")

  val (number, string) = myTuple
  println(number)
  println(string)

  /**
   * @这个语法结构在处理样例类时非常有用。如果你知道要处理的样例类是什么，就可以用—一个模式来析构它。参考下面的例子:
   */
  val exp = BinOp("*", Num(5), Num(1))

  val BinOp(op, left, right) = exp
  println(s"$op $left $right")


  /**
   * @<作为偏函数的case序列>
   *
   * @用花括号括起来的一系列case(即可选分支)可以用在任何允许出现函数字面量的地方。
   * @从本质上讲，case序列就是一个函数字面量，只是更加通用。不像普通函数那样只有一个入口和参数列表，case序列可以有多个入口，且每个入口都有自己的参数列表。
   * @每个case对应该函数的一个入口，而该入口的参数列表用模式来指定。每个入口的逻辑主体是case右侧的部分。
   *
   * 下面是一个简单的例子:
   */
  val withDefault: Option[Int] => Int = {
    case Some(x) => x
    case None => 0
  }

  /**
   * 该函数的函数体有两个case。第一个case匹配Some，返回Some中的值。第二个case匹配None，返回默认值0。下面是这个函数用起来的效果:
   * (Scala中的Partial Function就是一个“残缺”的函数，就像一个严重偏科的学生，只对某些科目感兴趣，而对没有兴趣的内容弃若蔽履。Partial Function做不到以“偏”概全，因而需要将多个偏函数组合，最终才能达到全面覆盖的目的。所以这个Partial Function确实是一个“部分”的函数。)
   */
  println(withDefault(Some(10)))
  println(withDefault(None))

  /**
   * 这套机制对Akka这个actor类库而言十分有用。因为有了这套机制，所以Akka可以用一组case来定义receive方法: 图13.17png
   *
   * 还有另一点值得我们注意:通过case序列得到的是一个偏函数( partial function）。
   * 如果我们将这样一个函数应用到它不支持的值上，则会产生一个运行时异常。例如，这里有一个返回整数列表中第二个元素的偏函数:
   */
  val second:List[Int] =>Int =
    case x::y::_ => y

  /**
   * 在编译时，编译器会正确地发出警告，我们的匹配并不全面: 图13.171
   *
   * 如果传入一个三元素列表，则这个函数会成功执行，不过传入空列表就没那么幸运了:
   */
  println(second(List(5,6,7,8,9,0)))
  //println(second(List()))

  /**
   * @如果你想检查某个偏函数是否对某个入参进行定义，则必须首先告诉编译器你知道要处理的是偏函数。
   * List[Int] => Int这个类型涵盖了所有从整数列表到整数的函数，无论这个函数是偏函数还是全函数。
   * 仅涵盖从整数列表到整数的偏函数的类型写作PartialFunction[List[Int],Int]。我们重新写一遍second函数，这次用偏函数的类型声明:
   */
  val second1:PartialFunction[List[Int],Int] =
    case x::y::_ =>y

  /**
   * @偏函数定义了一个方法isDefinedAt，可以用来检查该函数是否对某个特定的值有定义。在本例中，这个函数对于任何至少有两个元素的列表都有定义:
   */
  println(second1.isDefinedAt(List(5,6,7)))
  println(second1.isDefinedAt(List(1)))

  /**
   * 偏函数的典型用例是模式匹配函数字面量，就像前面这个例子一样。
   * @事实上，这样的表达式会被Scala编译器翻译成偏函数，且这样的翻译发生了两次:一次是实现真正的函数，另一次是测试这个函数是否对指定值有定义。
   *
   * 举例来说，函数字面量{ case x::y::_=>y}将被翻译成如下的偏函数值:
   */
  new PartialFunction[List[Int],Int] :
    override def apply(xs: List[Int]): Int =
      xs match {
        case x::y::_ =>y
      }

    override def isDefinedAt(xs: List[Int]): Boolean =
      xs match {
        case x::y::_ =>true
        case _ =>false
      }

  /**
   * @只要函数字面量声明的类型是PartialFunction，这样的翻译就会生效。
   * 如果声明的类型只是Function1，或者没有声明，那么函数字面量对应的就是一个全函数(complete function)。
   *
   * 一般来说，我们应该尽量用全函数，因为偏函数允许运行时错误出现，而编译器无法帮助我们解决这样的错误。
   * @不过有时候偏函数也特别有用。你也许能确保不会有不能处理的值传入，也可能会用到那种预期偏函数的框架，在调用函数之前，总是会先用isDefinedAt做一次检查。
   * 对于后者的例子，可以参考上面给出的receive方法，我们得到的是一个偏函数，只用于处理那些调用方想处理的消息。
   */


  /**
   * @<for表达式中的模式>
   *
   * 我们还可以在for表达式中使用模式，如示例13.18所示。这里的for表达式从capitals映射中接收键/值对，每个键/值对都与模式(country, city)匹配。这个模式定义了两个变量，即country和city。
   */
  //示例13.18带有元组模式的for表达式
  for (country, city) <- capitals yield
      s"The capital of $country is $city"

  /**
   * 示例13.18给出的对偶（pair）模式很特别，因为这个匹配永远都不会失败。的确，capitals交出一系列的对偶，因此可以确保每个生成的对偶都能够与对偶模式匹配上。
   * 不过某个模式不能匹配某个生成的值的情况也同样存在。示例13.19就是这样一个例子。
   */
  //示例13.19 从列表中选取匹配特定模式的元素
  val results =List(Some("apple"),None,Some("orange"))

  println(for Some(fruit) <- results yield fruit)  //List(apple, orange)


  /**
   * 我们从这个例子中可以看到，生成的值中那些不能匹配给定模式的值会被直接丢弃。
   * 例如，results列表中的第二个元素None就不能匹配给定模式Some(fruit)，因此它也就不会出现在输出中了。
   */




}
