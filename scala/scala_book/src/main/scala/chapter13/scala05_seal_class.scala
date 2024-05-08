package chapter13

object scala05_seal_class extends App {


  sealed trait Expr

  case class Var(name: String) extends Expr

  case class Num(number: Double) extends Expr

  case class UnOp(operator: String, arg: Expr) extends Expr

  case class BinOp(operator: String, left: Expr, right: Expr) extends Expr
  /**
   * 13.5 密封类
   *
   * 每当我们编写一个模式匹配时，都需要确保完整地覆盖了所有可能的case。有时候可以通过在末尾添加一个默认case来实现，不过这仅限于有合理兜底的场合。
   * 如果没有这样的默认行为，我们如何确保自己覆盖了所有的场景呢?
   *
   * 我们可以寻求Scala编译器的帮助，由它帮助我们检测出match表达式中缺失的模式组合。为了做到这一点，编译器需要分辨出可能的case有哪些。
   * 一般来说，在Scala中这是不可能的，因为新的样例类随时随地都能被定义出来。
   * 例如，没有人会阻止你在现在的4个样例类所在的编译单元之外的另一个编译单元中给Expr的类继承关系添加第五个样例类。
   */

  /**
   * @解决这个问题的方法是将这些样例类的超类标记为密封的(sealed)。
   * @除了在同一个文件中定义的子类，密封类不能添加新的子类。
   * 这一点对模式匹配而言十分有用，因为这样—来我们就只需要关心那些已知的样例类。
   * 不仅如此，我们还因此获得了更好的编译器支持。如果我们对继承自 密封类的样例类做匹配，则编译器会用警告消息标示出缺失的模式组合。
   *
   * 如果你打算将类用于模式匹配，则应该考虑将它做成密封类。只需要在类继承关系的顶部那个类的类名前面加上sealed关键字即可。
   * 这样一来，使用你的这组类的程序员在模式匹配这些类时，就会信心十足。
   * 这也是sealed关键字通常被看作模式匹配的标志的原因。示例13.16给出了Expr被转换成密封类的例子。
   */

  def describe(e:Expr):String =
    (e) match {
      case Num(_) => "a number"
      case Var(_) => "a variable"
    }
 println( describe(Num(1.2)))

  /**
   * 这样的警告 告诉我们这段代码存在产生MatchError异常的风险,因为某些可能出现的模式（UnOp、BinOp）并没有被处理。
   * 这个警告指出了潜在的运行时错误源，因此通常有助于我们编写正确的程序。
   *
   * 不过，有时候你也会遇到编译器过于挑剔的情况。举例来说，你可能从上下文中知道永远只能将describe应用到 Number或Var，因此很清楚不会有MatchError发生。
   * 这时你可以给describe添加一个捕获所有的case，这样就不会有编译器告警了:
   *
   * case _ => throw new RuntimeException
   */

  /**
   * 这样可行，但并不理想。你可能并不会很乐意，因为你被迫添加了永远不会被执行的代码（也可能是你认为不会被执行的代码)，而所有这些只是为了让编译器“闭嘴”。
   * 一个更轻量的做法是给match表达式的选择器部分添加一个@unchecked注解。就像这样:
   */

  def describe1(e: Expr): String =
    (e: @unchecked) match {
      case Num(_) => "a number"
      case Var(_) => "a variable"
    }

  /**
   * 我们会在《Scala高级编程》中介绍注解。一般来说，可以像添加类型声明那样对表达式添加注解:在表达式后面添加一个冒号和注解的名称（以@符号开头)。
   * 例如，在本例中我们给变量e添加了@unchecked注解，即“e: @unchecked”。@unchecked注解对模式匹配而言有特殊的含义。
   * @如果match表达式的选择器带上了这个注解，则编译器对后续模式分支的覆盖完整性检查就会被压制。
   */






}
