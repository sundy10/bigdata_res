package chapter03

object scala04_ImperativeOrLambda_programme extends App {
  /*
   *  首先从代码层面识别两种风格的差异。一个显著的标志是如果代码包含任何var变量，则它通常是指令式风格的;
   *    一个向函数式编程风格转变的方向是尽可能不用var。而如果代码完全没有var(也就是说代码只包含val)，则它很可能是函数式的。
   *    因此,一个向函数式编程风格转变的方向是尽可能不用var。
   */

  def formatArgs(args:List[String]) =args.mkString("\n")

  val res: String = formatArgs(List("zero", "one", "two"))
  assert(res == "zero\none\ntwo")//断言 失败抛出 AssertionError

}
