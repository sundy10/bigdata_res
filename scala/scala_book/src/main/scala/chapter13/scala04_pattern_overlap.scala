package chapter13

object scala04_pattern_overlap extends App {

  trait Expr

  case class Var(name: String) extends Expr

  case class Num(number: Double) extends Expr

  case class UnOp(operator: String, arg: Expr) extends Expr

  case class BinOp(operator: String, left: Expr, right: Expr) extends Expr

  val e = Var("-1")

  /**
   * 13.4 模式重叠
   *
   * 模式会按照代码中的顺序逐个被尝试。示例13.15中的simplifyAll展示了模式中的case出现顺序的重要性。 (先特殊再一般)
   *
   * 示例13.15中的simplifyAll将会对一个表达式中的各部分都执行简化，不像simplifyTop那样仅仅在顶层做简化。
   * simplifyAll可以从simplifyTop演化出来，只需要再添加两个分别针对一元和二元表达式的case即可(示例13.15中的第四个和第五个case)。
   */

  def simplityAll(expr: Expr): Expr =
    expr match {
      case UnOp("-", UnOp("-", e)) => simplityAll(e) // '-' 是它的自反
      case BinOp("+", e, Num(0)) => simplityAll(e) // '0' 是 '+' 的中立元素
      case BinOp("*", e, Num(1)) => simplityAll(e) // '1' 是 '*' 的中立元素
      case UnOp(op, e) => UnOp(op, simplityAll(e))
      case BinOp(op, l, r) => BinOp(op, simplityAll(l), simplityAll(r))
      case _ => expr
    }

  /**
   * 第四个case的模式是UnOp(op,e)，它匹配所有的一元操作。这个一元操作的操作符和操作元可以是任意的。它们分别被绑定到模式变量op和e上。
   * 这个case对应的可选分支会递归地对操作元e应用simplifyAll，然后用（可能的）简化后的操作元重建这个一元操作。
   * 第五个BinOp的case也同理:它是一个“捕获所有”( catch-all）的对任意二元操作的匹配，并在匹配成功后递归地对它的两个操作元应用简化方法。
   *
   * 在本例中，捕获所有的case出现在更具体的简化规则之后，这是很重要的。如果我们将顺序颠倒过来，则捕获所有的case就会优先于更具体的简化规则执行。
   * 在许多场景下，编译器甚至会拒绝编译。例如，下面这个match表达式就无法通过编译，因为首个case将会匹配所有第二个case能匹配的值:
   */


}
