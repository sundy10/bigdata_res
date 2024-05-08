package chapter07

object scala06_no_break_continue extends App {

  /**
   * 你可能已经注意到了，我们并没有提到break或continue。Scala去掉了这两个命令，因为它们与接下来一章会讲到的函数字面量不搭。
   * 在while循环中，continue的含义是清楚的，不过在函数字面量中应该是什么含义才合理呢?
   * 虽然Scala同时支持指令式和函数式的编程风格，但是在这个具体的问颗上，它更倾向于函数式编程风格，以换取语言的简单。
   * 不过别担心，就算没有了break和continue，还有很多其他方式可以用来编程。而且，如果你用好了函数字面量，则使用这里提到的其他方式通常比原来的代码更短。
   */

  /**
   * 最简单的方式是用if换掉每个continue，用布尔值换掉每个break。
   * 布尔值表示包含它的while循环是否继续。
   *
   * 例如，假设你要检索参数列表，找一个以“.scala”结尾但不以连字符开头的字符串，那么用Java的话，
   * 你可能会这样写(如果你喜欢while循环、break和continue) :
   */
  var i = 0
  var foundIt = false

  while i < args.length && foundIt do
    if args(i).startsWith("-") then
      if args(i).endsWith(".scala") then
        foundIt = true
      else
        i = i + 1
    else
      i = i + 1


  def searchFrom(i:Int):Int =
    if i>=args.length then -1
    else if args(i).startsWith("-") then searchFrom(i+1)
    else if args(i).endsWith(".scala") then i
    else searchFrom(i+1)

  /**
   * 示例7.17的这个版本采用了对用户来说有意义的函数名，并且使用递归替换了循环。
   * 每一个continue都被替换成一次以i＋1作为入参的递归调用，从效果上讲，跳到了下一个整数值。
   *
   *      一旦习惯了递归，很多人都会认为这种风格的编程方式更易于理解。
   */

  /**
   * Scala编译器实际上并不会对示例7.17中的代码生成递归的函数。
   * 由于所有的递归调用都发生在函数尾部(tail-call position)，因此编译器会生成与while循环类似的代码。
   * 每一次递归都会被实现成跳回函数开始的位置。8.10节将会对尾递归优化做更详细的讨论。
   */
}
