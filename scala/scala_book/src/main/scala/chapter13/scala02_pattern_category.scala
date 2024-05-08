package chapter13

object scala02_pattern_category extends App {

  trait Expr

  case class Var(name: String) extends Expr

  case class Num(number: Double) extends Expr

  case class UnOp(operator: String, arg: Expr) extends Expr

  case class BinOp(operator: String, left: Expr, right: Expr) extends Expr

  val e = Var("-1")

  /**
   * 13.2 模式的种类
   *
   * 前面的例子快速地展示了几种模式，接下来我们花些时间来详细介绍每一种模式。
   *
   * (选择器 match   case 模式 =>表达式)
   * 模式的语法很容易理解，所以不必太担心。
   * 所有的模式与相应的表达式看上去完全一样。
   * 例如，基于示例13.1的类层次结构，Var(x)这个模式将匹配任何变量表达式，并将x绑定成这个变量的名称。
   * 当作为表达式使用时，Var(x)———完全相同的语法——将重新创建一个等效的对象，当然前提是x已经被绑定成这个变量的名称。
   * 由于模式的语法是透明的，我们只需要关心能使用哪几种模式即可。
   */

  /**
   * @通配模式
   * 通配模式(_)可匹配任何对象。你前面已经看到过，通配模式用于默认、捕获所有的可选路径
   *
   * 通配模式还可以用来忽略某个对象中你并不关心的局部。
   * 例如，前面这个例子实际上并不需要关心二元操作的操作元是什么，它只是检查这个表达式是否是二元操作，仅此而已。
   * 因此，这段代码也完全可以用通配模式来表示BinOp的操作元，参考示例13.4。
   */


  BinOp("*", e, Num(1)) match {
    case BinOp(_, _, _) => println("ok!")
    case _ => println("It't something else")
  }

  /**
   * @常量模式
   *
   * 常量模式仅匹配自己。任何字面量都可以作为常量（模式)使用。
   * 例如，5、true和"hello'都是常量模式。
   * @同时，任何val或单例对象也可以被当作常量（模式）使用。例如，Nil这个单例对象能且仅能匹配空列表。示例给出了常量模式的例子。
   */

  def describe(x: Any) =
    x match {
      case 5 => "five"
      case true => "truth"
      case "hello" => "hi!"
      case Nil => "the empty list"
      case _ => "something else"
    }

  println(describe(5))
  println(describe(true))
  println(describe("hello"))
  println(describe(Nil))
  println(describe(List(1, 2, 3)))


  /**
   * @变量模式
   *
   * 变量模式可匹配任何对象，这一点与通配模式相同。
   * @不过与通配模式不同的是，Scala将对应的变量绑定成匹配上的对象。在绑定之后，就可以用这个变量对对象做进一步的处理。
   * 示例 给出了一个针对零的特例和针对所有其他值的默认处理的模式匹配。
   * 默认的case用到了变量模式，这样就给匹配的值赋予了一个名称，无论这个值是什么。
   */

  var expr = 1
  expr match {
    case 0 => println("zero")
    case somethingElse => println(s"not zero $somethingElse")
  }

  /**
   * @常量还是变量?
   *
   * 常量模式也可以有符号形式的名称。当我们把Nil当作一个模式时，实际上就是在用一个符号名称来引用常量。
   * 这里有一个相关的例子，这个模式匹配涉及常量E(2.71828...）和常量Pi (3.14159...) :
   */

  import math.{E, Pi}

  E match {
    case Pi => println(s"strange math? Pi=$Pi")
    case _ => println(s"OK")
  }

  /**
   * 与我们预期的一样，E并不匹配Pi，因此“strange math”这个case没有被使用。
   *
   * Scala编译器是如何知道Pi是从scala.math包引入的常量，而不是一个代表选择器值本身的变量呢?
   *
   * @Scala采取了一个简单的词法规则来区分:一个以小写字母开头的简单名称会被当作变量（模式)处理;所有其他引用都是常量
   * 要想看到具体的区别，可以给Pi创建一个小写的别名，然后尝试如下代码:
   *
   * 在这里，编译器甚至不允许我们添加一个默认的case。由于pi是变量（模式)，它将会匹配所有输入，因此不可能走到后面的case:
   */
  val pi = math.Pi
  E match {
    case pi => println(s"strange math? Pi=$Pi")
    //case _ => "OK"
  }

  /**
   * 如果有需要，你仍然可以用小写的名称来作为常量（模式)。这里有两个小技巧。
   * 首先，如果常量是某个对象的字段，则可以在字段名前面加上限定词。例如，虽然pi是变量（模式)，但this. pi或obj.pi是常量（模式)，尽管它们以小写字母开头。
   * 如果这样不行（比如，pi可能是一个局部变量)，也可以用反引号将这个名称包起来。例如，`pi`可以再次被编译器解读为一个常量，而不是变量:
   */
  E match {
    case `pi` => println(s"strange math? Pi=$pi")
    case _ => "OK"
  }

  /**
   * 你应该看到了，给标识符加上反引号在Scala中有两种用途，可以帮助你从不寻常的代码场景中走出来。
   * 这里你看到的是如何将以小写字母开头的标识符用作模式匹配中的常量。更早的时候，在6.10节，你还看到过使用反引号可以将关键字当作普通的标识符，比如，Thread.`yield` (这段代码将yield当作标识符而不是关键字。)
   */


  /**
   * @构造方法模式
   *
   *
   * 构造方法模式可以真正体现出模式匹配的威力。一个构造方法模式看上去像这样:“BinOp("+"; e,Num(O))”。
   * 它由一个名称（BinOp)和一组圆括号中的模式——"+"、e口Num(O)组或。
   * @假设这里的名称指定的是一个样例类，则这样的一个模式将首先检查被匹配的对象是否是以这个名称命名的样例类的实例，然后检查这个对象的构造方法参数是否匹配这些额外的模式。
   *
   * 这些额外的模式意味着Scala的模式支持深度匹配( deepmatch)。
   * 这样的模式不仅检查给出的对象的顶层，还会进一步检查对象的内容是否匹配额外的模式要求。
   * 由于额外的模式也可能是构造方法模式，用它们检查对象内部时可以到达任意的深度。
   * 例如，示例13.7给出的模式将检查顶层的对象是否为BinOp，并确认它的第三个构造方法参数是一个Num，且这个Num的值字段为0。这是一个长度只有一行但深度有三层的模式。
   */
  val expr1 = BinOp("-", e, Num(0))

  expr1 match {
    case BinOp(a, e, Num(0)) => println("a deep match")
    case _ => println("_")
  }

  /**
   * @序列模式
   *
   * 就像与样例类匹配一样，也可以与序列类型做匹配，如List或Array。使用的语法是相同的，不过现在可以在模式中给出任意数量的元素。示例13.8显示了一个以零开始的三元素列表的模式。
   */

  val xs = List(0, 1, 2, 3)

  //示例13.8 匹配固定长度的序列模式
  xs match {
    case List(0, _, _) => println("found it")
    case _ => println("not found it")
  }

  /**
   * @如果你想匹配一个序列，但又不想给出其长度，则可以用_*作为模式的最后一个元素。
   * 这个看上去有些奇怪的模式能够匹配序列中任意数量的元素，包括零个元素。示例13.9显示了一个能匹配任意长度的、以零开始的列表的模式。
   */
  xs match {
    case List(0, _*) => println("found it")
    case _ => println("not found it")
  }

  /**
   * @元组模式
   *
   * 我们还可以匹配元组(tuple）。形如(a, b, c)这样的模式能匹配任意的三元组。参考示例
   */
  def tupleDemo(obj: Any) =
    obj match {
      case (a, b, c) => println(s"matched $a-$b-$c")
      case _ => println("_")
    }

  tupleDemo(("a", 3, List("haha")))
  tupleDemo(("a", 2, 3, List("haha")))


  /**
   * @带类型的模式
   *
   * 可以用“带类型的模式”(typed pattern)来替代类型测试和类型转换。参考示例13.11。
   */
  def generalSize(x: Any) =
    x match {
      case s: String => println(s.length)
      case m: Map[_, _] => println(m.size)
      case _ => println(-1)
    }

  generalSize("fjsalsda")
  generalSize(Map(1 -> "a", 2 -> "b"))
  generalSize(math.Pi)

  /**
   * generalSize方法返回不同类型的对象的大小或长度。其入参的类型是Any，因此可以是任何值。
   * 如果入参是字符串，则方法将返回这个字符串的长度。模式“s: String”是一个带类型的模式，它将匹配每个（非null的)String实例。
   * 其中的模式变量s将指向这个字符串。
   *
   * 需要注意的是，虽然s和x指向同一个值，但是x的类型是Any,而s的类型是String。因此可以在与模式相对应的可选分支中使用s.length，但不能使用x.length，因为类型Any并没有一个叫作length的成员。
   *
   * 另一个与用带类型的模式匹配等效但更冗长的方式是先做类型测试再做（强制）类型转换。对于类型测试和类型转换，Scala与Java的语法不太一样。例如，要测试某个表达式expr的类型是否为String,需要这样写:
   */
  val expr3 = "12"
  expr3.isInstanceOf[String]
  //要将这个表达式转换成String类型，需要用:
  expr3.asInstanceOf[String]

  /**
   * 通过类型测试和类型转换,我们可以重写示例13.11中的match表达式的第一个Case，如示例13.12所示。
   *
   * if math.Pi.isInstanceOf[String] then
   *    math.Pi.asInstanceOf[String].length
   * else ...
   *
   * isInstanceOf和asInstanceOf两个操作符会被当作Any类的预定义方法处理。这两个方法接收一个用方括号括起来的类型参数。
   * 事实上，x.asInstanceOf[String]是该方法调用的一个特例，它带上了显式的类型参数String。
   */

  /**
   * 你现在应该已经注意到了，在Scala中编写类型测试和类型转换会比较啰唆。我们是故意这样做的，因为这并不是一个值得鼓励的做法。
   * 使用带类型的模式通常会更好，尤其是当你需要同时做类型测试和类型转换的时候，因为这两个操作所做的事情会在单个模式匹配中批量完成。
   */

  /**
   * 示例13.11中的match表达式的第二个case包含了带类型的模式"m: Map[_,_]”。这个模式匹配的是任何Map值，不管它的键和值的类型是什么，都会让m指向这个值。
   * 因此,m.size的类型是完备的，返回的是这个映射的大小。类型模式中的下画线就像是其他模式中的通配符。除了用下画线，也可以用（小写的）类型变量。
   * (在m: Map[_,_]这个带类型的模式中，“Map[,_]”部分被称为“类型模式”。)
   */


  /**
   * -----------------
   * @类型归因 (type ascription)
   *
   *  强制类型转换从根本上讲就是不安全的。例如，尽管编译器有足够的信息判定从Int到 String的强制类型转换会在运行时失败，它仍然会通过编译（然后在运行时失败):
   *
   *  3.asInstanceOf[String]
   *  java.lang.ClassCastException: java.lang.Integer cannot be cast to java.lang.String
   */

  /**
   * 另一个总是安全的选择是类型归因(type ascription):在变量或表达式之后放置一个冒号和一个类型（声明)。
   * 类型归因是安全的，因为任何非法的归因，比如，将Int类型归因为String类型，会触发编译错误，而不是运行时的异常:
   *
   * 3:String
   *
   * 类型归因仅在两种情况下通过编译。首先，可以用类型归因将一个类型扩大到它的某一个超类型。
   * 其次，也可以用类型归因隐式地将一个类型转换成另一个类型，比如，隐式地将Int关类型转换或Long类型:
   */
  val res:Long = 3

  /**
   * @类型擦除
   *
   *  除了笼统的映射，我们还能测试特定元素类型的映射吗?这对于测试某个值是否是Int类型到Int类型的映射等场景会很方便。下面我们试试看:
   */
  def isIntIntMap(x:Any)=
    x match {
      case m:Map[Int,Int] =>println(true)
      case _ =>println(false)
    }

  /**
   * Scala采用了擦除式的泛型，就像Java一样。
   * 这意味着在运行时并不会保留类型参数的信息。这样一来，在运行时就无法判断某个给定的Map对象是用两个Int类型参数创建的，还是用其他类型参数创建的。
   * 系统能做的只是判断某个值是否为某种不确定类型参数的Map。可以把isIntIntMap应用到不同的Map类实例来验证这个行为:
   */
  isIntIntMap(Map(1->1))
  isIntIntMap(Map("a"->"b"))

  /**
   * 第一次应用返回true，看上去是正确的，不过第二次应用同样返回true，这可能会让你感到意外。
   * 为了警示这种可能违反直觉的运行时行为，编译器会给出我们在前面看到的那种非受检的警告。
   */

  /**
   * 对于这个擦除规则，唯一的例外是数组，因为Java和Scala都对它做了特殊处理。
   * @数组的元素类型是与数组一起保存的，因此我们可以对它进行模式匹配。例如:
   */
  def isStringArray(x:Any)=
    x match {
      case a:Array[String] =>println("yes")
      case _=>println("no!")
    }

  isStringArray(Array("abc"))
  isStringArray(Array(1,2,3))

  /**
   * @变量绑定
   *
   * 除了独自存在的变量模式，我们还可以对任何其他模式添加变量。只需要写下变量名、一个@符号和模式本身，就可以得到一个变量绑定模式，
   * 这意味着这个模式将像平常一样执行模式匹配。如果匹配成功，就将匹配的对象赋值给这个变量，就像简单的变量模式一样。
   *
   * 示例给出了一个（在表达式中)查找绝对值操作被连续应用两次的模式匹配的例子。这样的表达式可以被简化成只执行一次求绝对值的操作。
   */

  //val expr4 =null
  val expr4 =UnOp("abs", Num(0))
  expr4 match {
    case UnOp("abs",e @ UnOp("abs", _))=> println(e)
    case _=> println("e @")
  }

  /**
   * 示例包括了一个以e为变量，以UnOp("'abs",_)为模式的变量绑定模式。如果整个匹配成功了，则匹配了UnOp("abs",_)的部分就被赋值给变量e。
   * 这个case的结果就是e，这是因为e与expr的值相同，但是少了一次求绝对值的操作。
   */

}
