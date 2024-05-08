package chapter13

object scala08_complex_case extends App {

  sealed trait Expr

  case class Var(name: String) extends Expr

  case class Num(number: Double) extends Expr

  case class UnOp(operator: String, arg: Expr) extends Expr

  case class BinOp(operator: String, left: Expr, right: Expr) extends Expr

  /**
   * 13.8 一个复杂的例子
   *
   * 在学习了模式的不同形式之后，你可能会对它在相对复杂的例子中是如何应用的感兴趣。
   * 提议的任务是编写一个表达式格式化类,以二维布局来显示一个算术表达式。诸如“x / (x +1)”的除法应该被纵向打印，将被除数放在除数上面，就像这样:
   *
   * x
   * -----
   * x + 1
   *
   * 再看另一个例子，将表达式((a/(b *c)+ 1/n)/ 3)放在二维布局中是这样的:
   *
   * a         1
   * -----  +  -
   * b * c     n
   * -----------
   * 3
   *
   */

  /**
   * 从这些示例来看，要定义的这个类（我们就叫它ExprFormatter吧）需要做大量的布局安排，因此我们有理由使用在第10章开发的布局类库。
   * 另外，我们还会用到本章前面讲到的Expr这组样例类，并将第10章的布局类库和本章的表达式格式化工具放在对应名称的包里。
   * 这个例子的完整代码如示例13.20和示例13.21所示。
   *
   * 第一步，先集中精力做好横向布局。比如，对于下面这个结构化的表达式:
   */
  BinOp("+",
    BinOp("*",
      BinOp("+", Var("x"), Var("y")),
      Var("z")),
    Num(1))

  class ExprFormatter:

    //包含成组的按优先级递进的操作符
    private val opGroups =
      Vector(
        Set("|", "||"),
        Set("&", "&&"),
        Set("^"),
        Set("==", "!="),
        Set("<", "<=", ">", ">="),
        Set("+", "-"),
        Set("*", "%")
      )

    //从操作符到其优先级的映射
    private val precedence = {
      val assocs =
        for
          i <- 0 until opGroups.length
          op <- opGroups(i)
        yield op -> i
      assocs.toMap
    }

    private val unaryPrecedence = opGroups.length
    private val fractionPrecedence = -1

    import chapter10.scala14_height_width.*

    private def format(e: Expr, enclPrec: Int): Element =
      e match
        case Var(name) => elem(name)

        case Num(number) =>
          def stripDot(s: String) =
            if s endsWith ".0" then s.substring(0, s.length - 2)
            else s

          elem(stripDot(number.toString))

        case UnOp(op, arg) =>
          elem(op) beside format(arg, unaryPrecedence)

        case BinOp("/", left, right) =>
          val top = format(left, fractionPrecedence)
          val bot = format(right, fractionPrecedence)
          val line = elem('-', top.width.max(bot.width), 1)
          val frac = top above line above bot
          if enclPrec != fractionPrecedence then frac
          else elem(" ") beside frac beside elem(" ")

        case BinOp(op, left, right) =>
          val opPrec = precedence(op)
          val l = format(left, opPrec)
          val r = format(right, opPrec + 1)
          val oper = l beside elem(" " + op + " ") beside r
          if enclPrec <= opPrec then oper
          else elem("(") beside oper beside elem(")")

      end match

    def format(e: Expr): Element = format(e, 0)

  end ExprFormatter


  /**
   * 应该打印出(x+ y)*z＋1。注意，x+ y外围的这组圆括号是必需的，但(x + y)*z外围的圆括号则不是必需的。
   * 为了保持布局尽可能清晰、易读，我们的目标是去除冗余的圆括号，同时确保所有必需的圆括号被继续保留。
   *
   * 为了知道应该在哪里放置圆括号，代码需要知晓操作符的优先级。接下来,我们先处理好这件事。可以用下面这样的映射字面量来直接表示优先级:
   *
   * 不过，这需要我们自己事先做一些运算。更方便的做法是先按照递增的优先级定义多组操作符，再从中计算每个操作符的优先级。具体代码参考示例13.20。
   *
   * 变量precedence是一个从操作符到优先级的映射，其中优先级从0开始。
   * 它是通过一个带有两个生成器的for表达式计算出来的。第一个生成器产生opGroups数组的每一个下标i。第二个生成器产生opGroups(i)中的每一个操作符op。
   * 对于每一个操作符，for表达式都会交出这个操作符op到下标i的关联。这样一来，数组中操作符的相对位置就被当作它的优先级。
   *
   * 关联关系用中缀的箭头表示，例如op ->i。之前我们只在映射的构造过程中看到过这样的关联，不过其本身也是一种值。事实上，op -> i这样的关联与对偶(op, i)是一回事。
   *
   * 现在我们已经确定了所有除 / 之外的二元操作符的优先级，接下来我们将这个概念进一步泛化，使它也包含一元操作符。
   * 一元操作符的优先级高于所有的二元操作符。因此,我们可以将unaryPrecedence (见示例13.20）设置为opGroups的长度，也就是比*和%操作符的优先级多1。
   *
   * @分数的优先级处理区别于其他操作符，因为分数采用的是纵向布局。不过，稍后我们就会看到，将除法的优先级设置为特殊的-1会很方便，因此将fractionPrecedence设置为-1(见示例13.20)。
   *
   * 完成了这些准备工作之后，就可以着手编写format这个主方法了。该方法接收两个入参:类型为Expr的表达式e，以及直接闭合表达式e的操作符的优先级enclPrec。
   * (如果没有直接闭合的操作符，则enclPrec应被设置为0。）这个方法交出的是一个代表了二维字符数组的布局元素。
   *
   * 示例13.21给出了ExprFormatter类的余下部分，包含3个方法。
   * @第一个方法stripDot是一个助手方法;第二个私有的format方法完成了格式化表达式的主要工作;最后一个同样被命名为format的方法是类库中唯一的公开方法，接收一个要格式化的表达式作为入参。
   *  私有的format方法通过对表达式的种类执行模式匹配来完成工作。这里的match表达式有5个case，我们将逐一介绍每个case。
   *
   *  第一个case是: 如果表达式是一个变量,结果就是由该变量名构成的元素。
   *
   *  第二个case是: 如果表达式是一个数值，结果就是一个由该数值构成的元素。stripDot函数通过去掉":.0"后缀来简化显示浮点数。
   *
   *  第三个case是: 如果表达式是一个一元操作UnOp(op, arg)，结果就是由操作符op和用当前环境中最高优先级格式化入参arg后的结果构成的。
   *  @ 这意味着如果arg是二元操作符（不过不是分数)，则它将总是显示在圆括号中。
   *
   *  第四个case是: 如果表达式是一个分数，中间结果frac就是由格式化后的操作元left和right上下叠加在一起并用横线隔开构成的。
   *  横线的宽度是被格式化的操作元宽度的最大值。这个中间结果也就是最终结果，除非这个分数本身是另一个分数的入参。
   *  对于后面这种情况，在frac的两边都会添加一个空格。要弄清楚为什么需要这样做，可以考虑表达式"(a / b)/ c”。
   *  如果没有这样的加宽处理，则这个表达式在格式化之后的效果会是这样的:
   *
   *  a
   *  -
   * b
   *  -
   * c
   *
   *  这个布局的问题很明显:到底哪一条横线表示分数的第一级是不清楚的。
   *  上述表达式既可以被解读为“(a / b) / c”，也可以被解读为"a / (b/ c)”。
   *  为了清晰地表示出采用哪一种先后次序，需要给内嵌的分数“a / b”在布局的两边加上空格。这样一来，布局就没有歧义了:
   *  a
   *  -
   * b
   * ---
   * c
   *
   *
   *  第五个也就是最后一个case是: 这个case作用于所有其他二元操作，因为它出现在下面这个case之后:
   *
   *  我们知道模式BinOp(op, left,right)中的操作符op不可能是一个除法。要格式化这样一个二元操作，需要首先将其操作元left和right格式化。
   *  格式化左操作元的优先级参数是操作符op的opPrec，而格式化右操作元的优先级比它要多1。这样的机制确保了圆括号能够正确反映结合律。
   *
   *  例如，如下操作:
   */


  BinOp("-", Var("a"), BinOp("-", Var("b"), Var("c"))) //a - (b-c)

  /**
   * 将被正确地加上圆括号: “a - (b - c)”。中间结果oper由格式化后的左操作元和格式化后的右操作元并排放在一起且用操作符隔开构成。
   * 如果当前操作符的优先级比闭合该操作的操作符（即上一层操作符）小，oper就被放在圆括号中;否则直接返回。
   *
   * 这样我们就完成了私有format函数的设计。对于公开的format方法，调用方可以通过该方法格式化一个顶级表达式，而不需要传入优先级入参。示例13.22给出了一个打印格式化表达式的应用程序。
   */
  val f = new ExprFormatter

  val e1: BinOp = BinOp("*", BinOp("/", Num(1), Num(2)), BinOp("+", Var("x"), Num(1))) // 1/2 *  (x+1)

  val e2: BinOp = BinOp("+", BinOp("/", Var("x"), Num(2)), BinOp("/", Num(1.5), Var("x")))

  val e3 = BinOp("/", e1, e2)

  def show(e: Expr) = println(s"${f.format(e)}\n\n")

  for e <- Vector(e1) do show(e)
  //for e <- Vector(e1,e2,e3) do show(e)


  /**
   * 13.9 结语
   *
   * 本章详细地介绍了Scala的样例类和模式匹配。通过它们，我们可以利用一些通常在面向对象编程语言中没有的精简写法。
   * 不过，本章描述的内容并不是Scala的模式匹配的全部。如果你想对你的类做模式匹配，但又不想像样例类那样将你的类开放给其他人访问，
   * 则可以用《Scala高级编程》中介绍的提取器( extractor)。在下一章，我们将注意力转向列表。
   *
   */


}
