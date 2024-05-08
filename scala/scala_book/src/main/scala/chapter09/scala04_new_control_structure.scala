package chapter09

import java.io.{File, PrintWriter}
import java.util.Date

object scala04_new_control_structure extends App {

  /**
   * 9.4 编写新的控制结构
   *
   * 在拥有一等函数的语言中，可以有效地制作出新的控制接口，尽管语言的语法是固定的。
   *
   * @你需要做的就是创建接收函数作为入参的方法。
   */

  /**
   * 例如，下面这个twice控制结构，它重复某个操作两次，并返回结果:
   */

  def twice(op: Double => Double, x: Double) = op(op(x))

  println(twice(_ + 1,5))

  /**
   * @当你发现某个控制模式在代码中多处出现时，就应该考虑将这个模式实现为新的控制结构。
   * 在本章前面的部分，你看到了filesMatching这个非常特殊的控制模式，
   * 现在来看一个更加常用的编码模式:打开某个资源，对它进行操作，然后关闭这个资源。
   * 可以用类似如下的方法，将这个模式捕获成一个控制抽象:
   */

  def withPrintWriter(file:File,op:PrintWriter=>Unit)=
    val writer = new PrintWriter(file)
    try op(writer)
    finally writer.close()

  withPrintWriter(
    new File("C:\\Users\\yosuke\\Desktop\\代码1\\test.scala"),
    writer =>writer.println(new Date)
  )

  /**
   * 使用这个方法的好处是，确保文件在最后被关闭的是withPrintriter而不是用户代码。
   * 因此，不可能出现使用者忘记关闭文件的情况。
   * @这个技巧被称作贷出模式，因为是某个控制抽象（如withPrintiter）打开某个资源并将这个资源“贷出”给函数的。
   * 例如，前一例中的withPrintWriter将一个PrintWriter“贷出”给op函数。
   * 当函数完成时，它会表明自己不再需要这个“贷入”的资源。
   * 这时这个资源就在finally代码块中被关闭了，这样能确保无论函数是正常返回还是抛出异常，资源都会被正常关闭。
   */

  /**
   * 可以用花括号而不是圆括号来表示参数列表，这样调用方的代码看上去就更像在使用内建的控制结构一样。
   *
   * 在Scala中，只要有那种只传入一个参数的方法调用，就都可以选择使用花括号来将入参括起来，而不是圆括号。
   */
  val s= "Hello, world"
  println(s.charAt(1))
  println(s.charAt{1})

  //起来。不过，这个花括号技巧仅对传入单个入参的场景适用。参考下面这个尝试打破上述规则的例子:
  //println(s.substring{1,2})

  /**
   * @Scala允许用花括号替代圆括号来传入单个入参的目的是让调用方'程序员'在花括号中编写函数字面量。这能让方法用起来更像是控制抽象。
   * 以前面的withPrintWriter为例，在最新的版本中，withPrintWriter接收两个入参，因此不能用花括号。
   * 尽管如此，由于传入withPrintWriter的函数是参数列表中的最后一个，因此可以用柯里化将第一个File参数单独拉到一个参数列表中，这样剩下的函数就独占了第二个参数列表。
   * 示例9.4展示了如何重新定义withPrintWriter。
   */

  def withPrintWriter1(file:File)(op:PrintWriter=>Unit) =
    val writer = new PrintWriter(file)
    try op(writer)
    finally writer.close()

  /**
   * 新版本与旧版本的唯一区别在于，现在有两个各包含一个参数的参数列表，而不是一个包含两个参数的参数列表了。
   * 仔细看两个参数之间的部分，在旧版本的withPrintWiter中 ，
   * 你看到的是...File, op...,，而在新版本中，你看到的是...File)(op...。有了这样的定义，就可以用更舒服的语法来调用这个方法了∶
   */
  val file = new File("C:\\Users\\yosuke\\Desktop\\代码1\\scala.txt")
  withPrintWriter1(file){
    writer=> writer.println(new Date)
  }

  /**
   * 在本例中，第一个参数列表，也就是那个包含了一个File入参的参数列表，用的是圆括号。而第二个参数列表，即包含函数入参的那个，用的是花括号。
   */
}
