package chapter08

object scala01_method extends App {

  /**
   * 第8章 函数与闭包
   *
   *  随着程序变大，需要使用某种方式将它切成更小的、更便于管理的块。
   *  Scala提供了对于有经验的程序员来说都很熟悉的方式来切分控制逻辑:将代码切成不同的函数。
   *  事实上，Scala提供了几种Java中没有的方式来定义函数。
   *  除了方法（即那些以某个对象的成员形式存在的函数)，还有嵌套函数、函数字面量和函数值等。
   *  本章将带你领略Scala中所有的这些函数形式。
   *
   *
   */

  /**
   *  8.1 方法
   *
   * 定义函数最常用的方式是作为某个对象的成员;这样的函数被称为方法。
   * 例如，示例8.1展示了两个方法，可以合在一起读取给定名称的文件并打印所有超过指定长度的行。
   * 在被打印的每一行之前都加上了该行所在的文件名。
   *
   * @padLine方法接收两个参数:line和minWdth 它首先检查当前行的长度是否小于给定宽度，如果是，则在行尾追加合适数量的空格，使得该行的长度与minVWdth相等。
   */

  object Padding{
    def padLines(text:String,minWidth:Int):String =
      val paddedLines =
        for line <- text.linesIterator yield
          padLine(line,minWidth)
      paddedLines.mkString("\n")

    private def padLine(line:String,minWidth:Int):String =
      if line.length>=minWidth then line
      else line + " " * (minWidth - line.length)

  }

  val str =
    """safhasdgcdfjkaklds
      |fhjisdfuj
      |jhasdfjla
      |218gcdahsdla
      |d231iekwgcdsjakdkl
      |""".stripMargin

  println(Padding.padLines(str,40))

}
