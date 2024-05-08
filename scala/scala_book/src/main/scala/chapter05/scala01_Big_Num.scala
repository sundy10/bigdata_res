package chapter05

object scala01_Big_Num extends App {

  /**
   * Scala 3包含了一个实验属性的功能特性，可以消除数值字面量的大小限制，用来初始化任意一种(数值)类型。
   * 你可以通如下的引入语句来开启这个特性:
   */
  val invoice: BigInt = 1_000_000_000
  val pi: BigDecimal = 3.1415926535897932384626433833

  //字符字面量
  val a = 'A'

  //除了显式地给出原字符，也可以用字符的Unicode码来表示。具体写法是u加上Unicode码对应的4位的十六进制数字，例如
  val b = '\u0042'
  val d = '\u0044'
  println(b + " " + d)

  //事实上，这样的Unicode字符可以出现在Scala程序的任何位置。比如，可以像这样命名一个标识符(变量):
  //val B\u0041 =1

  /**
   * 为了处理这个常见的情兄，可以对字符串调用stripMargin方法。
   * 具体做法是在每—行开始加一个管道符(|)，然后对整个字符串调用stripMargin方法:
   */

  println(
    """|Welcome to Ultamix 3000
      |Type "HELP" for help.""".stripMargin)


}
