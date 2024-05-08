package chapter13

object scala01_caseClass_match extends App {

  /**
   * 第13章 样例类和模式匹配
   *
   * 本章将介绍样例类(case class)和模式匹配( pattern matching)，这组孪生的语法结构为我们编写规则的、未封装的数据结构提供支持。
   *
   * @这两个语法结构对于表达树形的递归数据尤其有用。
   *
   * 如果你之前曾用过函数式语言编程，则也许已经知道什么是模式匹配，不过样例类对你来说应该是新的概念。
   * @样例类是Scala用来对对象进行模式匹配而并不需要大量的样板代码的方式。笼统地说，你要做的就是给那些你希望能做模式匹配的类加上一个case关键字。
   *
   * 本章将从一个简单的样例类和模式匹配的例子开始。然后依次介绍Scala支持的各种模式，探讨密封类( sealed class)，讨论枚举、Option类型，
   * 并展示语言中某些不那么明显地使用模式匹配的地方。最后，还会展示一个更真实的模式匹配的例子。
   */

  /**
   * 13.1 一个简单的例子
   *
   * 在深入探讨模式匹配的所有规则和细节之前，我们有必要先看一个简单的例子，以让我们明白模式匹配大概是做什么的。
   * 假设你需要编写一个操作算术表达式的类库，同时这个类库可能是你正在设计的某个领域特性语言（DSL)的一部分。
   *
   * 解决这个问题的第一步是定义输入数据。为了保持简单，我们将注意力集中在由变量、数，以及一元和二元操作符组成的算术表达式上。
   * 用Scala的类层次结构来表达.
   */
  trait Expr

  case class Var(name: String) extends Expr

  case class Num(number: Double) extends Expr

  case class UnOp(operator: String, arg: Expr) extends Expr

  case class BinOp(operator: String, left: Expr, right: Expr) extends Expr

  /**
   * 这个层次结构包括1个抽象的基类Expr和4个子类，每一个都表示我们要考虑的一种表达式。所有5个类的定义体都是空的。
   *
   * @样例类
   *
   * 在示例13.1中，另一个值得注意的点是每个子类都有一个case修饰符。
   * 带有这样的修饰符的类被称为样例类。
   * @正如4.4节讲到的，使用这个修饰符会让Scala编译器对类添加一些语法上的便利。
   * @首先，它会添加一个与类同名的工厂方法。这意味着我们可以用Var("×")来构造一个Var对象，而不用稍长版本的new Var("x'"):
   *
   *                                                             当你需要嵌套定义时，工厂方法尤为有用。由于代码中不再到处充满new关键字，因此你可以一眼就看明白表达式的结构:
   */
  val v = Var("x")
  val op = BinOp("+", Num(1), v)

  /**
   * @其次，第二个语法上的便利是参数列表中的参数都隐式地获得了—个val前缀，因此它们会被当作字段处理:
   */
  v.name
  op.left

  /**
   * 再次，编译器会帮助我们以“自然”的方式实现toString、hashCode和equals方法。
   * 这些方法分别会打印、哈希，以及比较包含类和所有入参的整棵树。由于Scala的==总是代理给equals方法，这意味着以样例类表示的元素总是以结构化的方式做比较:
   */
  println(op.toString) //BinOp(+,Num(1.0),Var(x))
  println(op.right == Var("x")) //true

  /**
   * 最后，编译器还会添加一个copy方法用于制作修改过的副本。这个方法可以用于制作除一两个属性不同之外其余完全相同的该类的新实例。
   * 这个方法用到了带名称的参数和默认参数（参考8.8节)。我们用带名称的参数给出想要做的修改。对于任何你没有给出的参数,都会用之前对象中的原值。例如，下面这段代码展示了一个与op—样，不过改变了操作符的操作。
   */
  println(op.copy(operator = "-")) //BinOp(-,Num(1.0),Var(x))

  /**
   * 所有这些带来的是大量的便利（代价却很小)。你需要多写一个case修饰符，并且你的类和对象会变得大一些。
   * 之所以更大，是因为生成了额外的方法，并且对构造方法的每个参数都隐式地添加了字段。不过，样例类最大的好处是支持模式匹配。
   * (样例类支持模式匹配的方式是在伴生对象中生成—个名称为unapply的提取器(extractor）方法。更多内容请参考《Scala高级编程》。)
   */

  /**
   * @模式匹配
   *
   * 假设我们想简化前面展示的算术表达式。可能的简化规则非常多，下面只是一些示例:
   */
  val e =Var("-1")
  println(UnOp("-",UnOp("-",e))) //UnOp(-,UnOp(-,Var(-1))) //双重取反
  println(BinOp("+",e,Num(0)))  //加0
  println(BinOp("*",e,Num(1)))  //乘1

  /**
   * 如果使用模式匹配，则这些规则可以被看作一个 Scala编写的简化函数的核心逻辑，如示例13.2所示。可以这样使用这个simplifyTop函数:
   *
   * 使用模式匹配的simplifyTop函数:
   */
  def simplifyTop(expr: Expr):Expr =
    expr match {
      case UnOp("-",UnOp("-",e)) => e
      case BinOp("+",e,Num(0)) => e
      case BinOp("*",e,Num(1)) => e
      case _=> expr
    }
  println(simplifyTop(UnOp("-",UnOp("-",e))) )
  println(simplifyTop(UnOp("-!",UnOp("-",e))) )

  /**
   * simplifyTop函数的右边由一个match表达式组成。match表达式对应Java的switch语句，不过match表达式出现在选择器表达式后面。换句话说，写成:
   *
   * 选择器 match{可选分支}
   *
   * 而不是
   * switch (选择器) {可选分支}
   */

  /**
   * @模式匹配包含一系列以case关键字开头的可选分支。每一个可选分支都包括一个模式，以及一个或多个表达式，
   * @如果模式匹配了，这些表达式就会被求值。箭头符号=>用于将模式和表达式分开。
   *
   * 一个match表达式的求值过程是按照模式给出的顺序逐一尝试。第一个匹配上的模式会被选中，同时跟在这个模式后面的表达式会被执行。
   *
   *
   * 类似"+'和1这样的常量模式( constant pattern)，可以匹配那些按照==的要求与它们相等的值。
   * 而像e这样的变量模式（variablepattern）可以匹配任何值。匹配后，在右侧的表达式中，这个变量将指向这个匹配的值。
   * 在本例中，注意前3个可选分支的求值结果都为e，一个在对应的模式中绑定的变量。
   * 通配模式（wildcard pattern,即_）可匹配任何值，不过它并不会引入一个变量名来指向这个值。
   * 在示例13.2中，需要注意的是，match表达式是以一个默认什么都不做的case结尾的，这个默认的case直接返回用于匹配的表达式expr。
   */

  /**
   * 构造方法模式（constructor pattern)看上去就像UnOp("-", e)。这个模式匹配所有类型为UnOp且首个入参匹配"-"而第二个入参匹配e的值。
   * 注意，构造方法的入参本身也是模式。这允许我们用精简的表示法来编写有深度的模式。例如:
   * UnOp("-",UnOp("-",e))
   *
   * 想象一下，如果用访问者模式来实现相同的功能要怎么做。再想象一下，如果用一长串if语句、类型测试和类型转换来实现相同的功能，几乎会同样笨拙。
   */

  /**
   * 对比match表达式和switch语句
   *
   * @match表达式可以被看作Java风格的switch语句的广义化。
   * Java风格的switch语句可以很自然地用match表达式表达，其中每个模式都是常量且最后一个模式可以是一个通配模式（代表switch语句中的默认case)。
   * @不过，我们需要记住3个区别:
   *  @第一，Scala的match是一个表达式（也就是说，它总是能得到一个值);
   *  @第二，Scala的可选分支不会贯穿(fall through）到下一个case;
   *  @第三，如果没有一个模式匹配上，则会抛出名称为MatchError的异常。这意味着你需要确保所有的case被覆盖到，哪怕这意味着你需要添加一个什么都不做的默认case。
   */
  def simplifyMatch(expr: Expr):Unit =
    expr match {
      case BinOp(op,left,right) =>
        println(s"$expr is a binary operation")
      case _=>
    }

  simplifyMatch(BinOp("*",e,Num(1)))

  /**
   * 参考示例13.3。第二个case是必要的，因为如果没有它，则match表达式对于任何非BinOp的expr入参都会抛出MatchError。
   * 在本例中，对于第二个case，我们并没有给出任何代码，因此如果这个case被运行，则什么都不会发生。
   * 两个case的结果都是Unit值，即'0'，这也是整个match表达式的结果。
   */

}
