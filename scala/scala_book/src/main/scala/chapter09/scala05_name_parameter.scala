package chapter09

object scala05_name_parameter extends App {

  /**
   * 9.5 传名参数
   *
   * (回顾)与前一节的withPrintWriter与语言内建的控制结构（如if和while)不同，花括号中间的代码接收一个入参。
   * 传入withPrintWriter的函数需要一个类型为PrintWiter的入参。这个入参就是下面代码中的"writer =>" :
   */

  /**
   * 不过如果你想要实现那种更像是if或while的控制结构，但没有值需要传入花括号中间的代码，该怎么办呢?
   * 为了帮助我们应对这样的场景，Scala提供了传名参数。
   *
   * 我们来看一个具体的例子，假设你想要实现一个名称为myAssert的断言结构。
   * 这个myAssert将接收一个函数值作为输入，然后通过一个标记来决定如何处理。
   * 如果标记位打开，则myAssert将调用传入的函数，验证这个函数返回了true;而如果标记位关闭，则myAssert将什么也不做。
   */

  var assertionsEnabled = true

  def myAssert(predicate: () => Boolean) =
    if assertionsEnabled && !predicate() then
      throw new AssertionError

  myAssert(() => 5 > 3)

  /**
   * 你大概更希望能够不在函数字面量里写空的圆括号和=>符号，而是直接这样写:
   * myAssert(5<3)
   *
   * 传名参数就是为了解决这个问题产生的。
   * 要让参数成为传名参数，需要给参数一个以=>开头的类型声明，
   * 而不是(=>。例如，可以像这样将myAssert的predicate参数转换成传名参数:把类型“)=>Boolean”改成“=> Boolean”。示例9.5给出了具体的样子:
   */
  def byNameAssert(predicate: => Boolean) =
    if assertionsEnabled && !predicate then
      throw new AssertionError()

  /**
   * 现在已经可以对要做断言的属性去掉空的参数列表了。这样做的结果就是byNameAssert用起来与使用内建的控制结构完全一样:
   * (总结一下如果有参数直接用内建，没有参数的可以用传名参数)
   */
  byNameAssert(5 > 3)

  /**
   * 对传名( by-name)类型而言，空的参数列表，即)，是需要去掉的，
   * 这样的类型只能用于参数声明，并不存在传名变量或传名字段。
   */

  /**
   * 你可能会好奇为什么不能简单地用旧版的Boolean作为其参数的类型声明，就像这样:
   *
   * 这种组织方式当然也是合法的，boolAssert用起来也与之前看上去完全一样:
   */
  def boolAssert(predicate: Boolean) =
    if assertionsEnabled && !predicate then
      throw new AssertionError()

  /**
   * 不过，这两种方式有一个显著的区别需要注意。
   *
   * @由于boolAssert的参数类型为Boolean，在boolAssert(5>3)圆括号中的表达式将“先于”对boolAssert的调用被求值。
   * @但是由于byNameAssert的predicate参数类型是=>Boolean，在byNameAssert(5>3)的圆括号中的表达式在调用byNameAssert之前并不会被求值，
   * 而是会有一个函数值被创建出来。这个函数值的apply方法将会对5>3求值，传入byNameAssert的是这个函数值。
   *
   * 因此，两种方式的区别在于
   * @如果断言被禁用(bug代码被禁用了)，你将能够观察到boolAssert的圆括号中的表达式的副作用，而使用byNameAssert则不会。
   * 例如，如果断言被禁用，那么当我们断言“x / 0 ==O”时，boolAssert会抛出异常:
   */

  val x = 5
  boolAssert(x / 0 == 0)

  byNameAssert(x / 0 == 0) //正常返回

  /**
   * 9.6结语
   *
   * 本章向你展示了如何基于Scala对函数的丰富支持来构建控制抽象。
   * 可以在代码中使用函数来提炼出通用的控制模式，也可以利用Scala类库提供的高阶函数来复用那些对所有程序员代码都适用的公共控制模式。
   * 我们还探讨了如何使用柯里化和传名参数让你的高阶函数用起来语法更加精简。
   */

  /**
   * 在第8章和本章中，你已经了解到关于函数的大量信息。在接下来的几章中，我们将继续对Scala中那些更加面向对象的功能特性做进一步讲解。
   */
}
