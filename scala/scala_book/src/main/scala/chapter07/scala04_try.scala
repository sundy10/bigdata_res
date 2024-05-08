package chapter07

import java.io.{FileNotFoundException, FileReader, IOException}
import java.net.{MalformedURLException, URL}

object scala04_try extends App {

  /**
   * Scala的异常处理与其他语言类似。除了正常地返回某个值，方法也可以通过抛出异常来终止执行。
   * 方法的调用方要么捕获并处理这个异常，要么自我终止，让异常传播给更上层的调用方。
   * 异常通过这种方式传播，逐个展开调用栈，直到某个方法处理了该异常或者没有更多方法了为止。
   *
   * 但是在Scala中,throw是一个有结果类型的表达式。下面是一个带有结果类型的示例:
   */

  def half(n: Int) =
    if n % 2 == 0 then
      n / 2
    else
      throw new RuntimeException("n must be even")


  //half(1)


  /**
   * 从技术上讲，抛出异常这个表达式的类型是Nothing。
   * 即使表达式从不实际被求值，也可以用throw。这个技术细节听上去有些奇怪，不过在这样的场景下，还是很常见且很有用的。
   * if表达式的一个分支用于计算出某个值，而另一个分支用于抛出异常并计算出Nothing。
   * 整个if表达式的类型就是那个计算出某个值的分支的类型。我们将在17.3节对Nothing做进一步的介绍。
   */

  try
    val f = new FileReader("C:\\Users\\yosuke\\Desktop\\代码1\\test.scala")
    //使用并关闭的文件
    f.close()
    f.read()
  catch
    case ex:FileNotFoundException=> println("处理文件缺失的情况")//处理文件缺失的情况
    case ex:IOException=>println("处理其他I/O错误")           //处理其他I/O错误

  val file = new FileReader("C:\\Users\\yosuke\\Desktop\\代码1\\test.scala")
  try
    println(file.read()) //使用文件
  finally
    file.close()          //确保关闭文件


  /**
   * 示例7.12展示了确保非内存资源被正确关闭的惯用做法。
   *  这些资源可以是
   *      文件、套接字、数据库连接等。
   *
   * 首先获取资源，然后在try代码块中使用资源，最后在finally代码块中关闭资源。
   * 关于这个习惯，Scala和Java是一致的。Scala提供了另一种技巧，即贷出模式（loanpattern)，
   * 可以更精简地达到相同的目的。我们将在9.4节详细介绍贷出模式。
   */


  /**
   * 与Scala的大多数其他控制结构一样，try-catch-finally最终返回一个值。
   * 例如，示例7.13展示了如何实现解析URL，但当URL格式有问题时返回一个默认的值。
   * 如果没有异常抛出，整个表达式的结果就是try子句的结果;
   * 如果有异常抛出且被捕获，整个表达式的结果就是对应的catch子句的结果;
   * 而如果有异常抛出但没有被捕获，整个表达式就没有结果。如果有finally子句，则该子句计算出来的值会被丢弃。
   * finally子句一般都用于执行清理工作，如关闭文件。
   */

  def urlFor(path:String):URL =
    try new URL(path)
    catch case e:MalformedURLException =>
      new URL("http://www/baidu.com")


  //调用g0将得到1。这两个函数的行为都很可能让多数程序员感到意外，因此，最好避免在finally子句中返回值，最好将finally子句用来确保某些副作用的发生
  def f():Int = try return 1 finally return 2
  println(f())

  def g():Int=try 1 finally 2
  println(g())
}
