package chapter07

object scala05_match extends App {

  /**
   * Scala的match表达式允许你从若干可选值(alternative）中选择，就像其他语言中的switch语句那样。
   * 一般而言，match表达式允许你用任意的模式( pattern)来选择（参见第13章)。
   * 抛开一般的形式不谈，目前我们只需要知道使用match可以从多个可选值中进行选择即可。
   */

  val arg = "eggs1"

  val firstArg = if !arg.isEmpty then arg else ""

  firstArg match {
    case "salt" => println("pepper")
    case "chips" => println("salsa")
    case "eggs" => println("bacon")
    case _ => println("huh?")
  }

  /**
   * Scala的match表达式与Java的switch相比，有一些重要的区别。
   * 其中一个区别是任何常量、字符串等都可以被用作样例，而不仅限于
   *
   * Java的case语句支持的整型、枚举和字符串常量。在示例7.14中，可选值是字符串。
   * 另一个区别是在每个可选值的最后并没有break。
   * 在Scala中, break是隐含的，并不会出现某个可选值执行完成后继续执行下一个可选值的情况。
   * 这通常是我们预期的（不直通到下一个可选值)，代码因此变得更短，也避免了一类代码错误，使得程序员不会再不小心直通到下一个可选值了。
   */

  /**
   * 不过Scala的match表达式与Java的switch语句相比最显著的不同在于，match表达式会返回值
   * 这样的代码不仅更短（至少字数更少了)，它还将两件不同的事情解耦了∶首先选择食物，然后将食物打印出来。
   */

  val friend =
    firstArg match {
      case "salt" => "pepper"
      case "chips" => "salsa"
      case "eggs" => "bacon"
      case _ => "huh?"
    }

  println(friend)

}
