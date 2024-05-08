package chapter08

object scala02_local_fuction extends App {

  /**
   * 前一节的padLines方法的构建展示了函数式编程风格的一个重要设计原则:程序应该被分解成许多函数，
   * @每个函数都只做明确定义的任务。
   * 单个函数通常都很小。这种风格的好处是可以让程序员灵活地将许多构建单元组装起来，完成更复杂的任务。
   * @每个构建单元都应该足够简单，简单到能够被单独理解的程度。
   */


  /**
   * 这种风格的一个问题是助手函数的名称会影响整个程序的命名空间。在编译器中，这并不是太大的问题，
   *
   * 不过一旦函数被打包进可复用的类和对象中，我们通常希望类的使用者不要直接看到这些函数。
   * 因为这些函数离开了类和对象单独存在时通常都没有什么意义，而且通常你会希望在后续采用其他方式重写该类时，保留删除助手函数的灵活性。
   */


  /**
   * 在Java中，帮助你达到此目的的主要工具是私有方法。
   * 这种私有方法的方式在Scala中同样有效（见示例8.1)，不过Scala还提供了另一种思路:可以在某个函数内部定义函数。
   * 就像局部变量一样，这样的“局部函数”(local function）只在包含它的代码块中可见。例如:
   *
   * 作为局部函数,padLine在padLines内有效，但不能从外部访问。
   */

  def padLines(text:String,minWidth:Int):String =
    def padLine(line:String):String =
      if line.length>=minWidth then line
      else line + " " * (minWidth - line.length)


    val paddedLines =
      for line <- text.linesIterator yield padLine(line)
    paddedLines.mkString("\n")

  /**
   * 使用外层函数的参数是Scala提供的通用嵌套机制的常见而有用的示例。
   * 7.7节介绍的嵌套和作用域对Scala所有语法结构都适用，函数当然也不例外。这是一个简单的原理，但非常强大。
   */



}

