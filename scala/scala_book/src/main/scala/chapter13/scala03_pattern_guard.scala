package chapter13

object scala03_pattern_guard extends App {

  trait Expr

  case class Var(name: String) extends Expr

  case class Num(number: Double) extends Expr

  case class UnOp(operator: String, arg: Expr) extends Expr

  case class BinOp(operator: String, left: Expr, right: Expr) extends Expr

  val e = Var("-1")

  /**
   * 13,3 模式守卫
   *
   * 有时候语法级的模式匹配不够精准。举例来说，假设我们要公式化一个简化规则，即用乘以2(即e *2)来替换对两个相同操作元的加法（e+e)。
   * 在表示Expr树的语言中，下面这样的表达式:
   */

  BinOp("+", Var("x"), Var("x"))

  /**
   * 应用该简化规则后将得到:
   */
  BinOp("*", Var("x"), Num(2))

  /**
   * 你可能会像如下这样来定义这个规则:
   *
   * def simplifyAdd(e:Expr) =
   * e match {
   * case BinOp("+",x,x) =>BinOp("*",x,Num(2))
   * case _ => e
   * }
   *
   * @这样做会失败，因为Scala要求模式都是线性（linear）的:同一个模式变量在模式中只能出现一次。
   * 不过，我们可以用一个模式守卫(pattern guard）来重新定义这个匹配逻辑，如示例13.14所示。
   */

  //示例13.14﹑带有模式守卫的match表达式
  def simplifyAdd(e: Expr) =
    e match {
      case BinOp("+", x, y) if x == y =>
        BinOp("*", x, Num(2))
      case _ => e
    }

  /**
   * 下面是其他一些带有模式守卫的示例:
   *
   * //只匹配正整数
   * case n:Int if 0 < n => ...
   *
   * //只匹配以字母'a' 开头的字符串
   * case s:String if s(0)=='a' => ...
   */

}
