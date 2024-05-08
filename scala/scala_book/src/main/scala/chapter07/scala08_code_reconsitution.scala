package chapter07

object scala08_code_reconsitution extends App {

  /**
   * 7.8 对指令式代码进行重构
   *
   * 对 1 to 10 for表达式运算返回新序列 Seq
   *
   * @return Seq序列
   */
  def makeRowSeq(row: Int): Seq[String] =
    for col <- 1 to 10 yield
      val prod = (row * col).toString
      val padding = " " * (4 - prod.length) // *和 python类似
      padding + prod

  //以字符串类型返回一行
  def makeRow(row: Int) = makeRowSeq(row).mkString

  //以字符串类型返回逐行的表格
  def multiTable() =
    val tableSeq: Seq[Seq[String]] = //行字符串序列
      for row <- 1 to 10
        yield makeRowSeq(row)

    println(tableSeq.mkString("\n")) //对每一行添加换行

  multiTable()

  /**
   * 结语:
   *
   * Scala内建的控制结构很小，但能解决问题。
   * 内建的控制结构与指令式的控制结构类似，但由于有返回值，它也支持更函数式的编程风格。
   * 同样重要的是，它很用心地省去了一些内容，让Scala最强大的功能特性之一，即函数字面量，得以发挥威力。
   * 下一章将详细介绍函数字面量。
   */
}
