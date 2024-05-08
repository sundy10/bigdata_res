package chapter08

import scala.util.Random

object scala10_tail_recursion extends App {

  def improve(guess: Double): Double = Random.nextDouble()

  def isGoodEnough(guess: Double): Boolean = guess > 0.5

  /**
   * 8.10 尾递归
   *
   * 在7.2节提到过，如果要将一个不断更新var的while循环改写成只使用val的更加函数式的风格，则可能需要用到递归。
   * 参考下面这个递归的函数例子，它是通过反复改进猜测直到结果足够好的方式来取近似值的:
   */

  def approximate(guess: Double): Double =
    if isGoodEnough(guess) then guess
    else approximate(improve(guess))

  println(approximate(0.1))

  /**
   * 有了合适的isGoodEnough和improve的实现，像这样的函数通常被用于搜索。如果你希望approximate函数运行得更快，则可能会尝试使用while循环来加快它的速度，就像这样:
   */

  def approximateLoop(initialGuess: Double): Double =
    var guess = initialGuess
    while !isGoodEnough(guess) do
      guess = improve(guess)
    guess

  println(approximateLoop(0.2))

  /**
   * 这两个版本的approximate函数到底哪一个更好呢?从代码简洁和避免使用var的角度来看，第一个函数式风格的版本胜出。
   *
   * 不过指令式风格的方式是不是真的更高效呢?事实上，如果我们测量执行时间的话，这两个版本几乎完全一样!
   *
   * 这听上去有些出人意料，因为递归调用看上去比简单地从循环的末尾跳到开始要更“膨胀”。
   * 不过，在上面这个approximate函数的例子中，
   *
   * @Scala编译器能够执行一个重要的优化。注意，递归调用是approximate函数体在求值过程中的最后一步。像approximate函数这样在最后一步调用自己的函数，被称为尾递归 （tail recursive）函数。
   * @Scala编译器能够检测到尾递归函数并将它跳转到函数的最开始，而且在跳转之前会将参数更新为新的值。
   *
   * 这背后的意思是我们不应该回避使用递归算法来解决问题。递归算法通常比基于循环的算法更加优雅、精简。如果解决方案是尾递归的，那么我们并不需要支付任何（额外的)运行时开销。
   */

  /**
   * @跟踪尾递归函数
   *
   * 尾递归函数并不会在每次调用时构建一个新的栈帧，
   * @所有的调用都会在同一个栈帧中执行。这一点可能会令检查某个失败程序的栈跟踪信息 (stack trace)的程序员意外。例如，下面这个函数在调用自己若干次之后会抛出异常:
   */

  //这个函数并不是尾递归的，因为它在递归调用之后还执行了加1操作。当你调用这个函数时，将会得到预期的结果
  def boom(x: Int): Int =
    if x == 0 then throw new Exception("boom!")
    else boom(x - 1) + 1

  //boom(100)

  /**
   * 如果你把boom改成尾递归的:
   *
   * 这一次，你将只会看到一个bang的栈帧。你可能会想是不是bang在调用自己之前就崩溃了，但事实并非如此。
   */

  def bang(x: Int): Int =
    if x == 0 then throw new Exception("boom!")
    else bang(x - 1)

  //bang(100)


  /**
   * @尾递归优化
   *
   * approximate函数编译后的代码在本质上与approximateLoop函数编译后的代码是一致的。
   * 两个函数都被编译成相同的13条指令的Java字节码。
   * 如果你仔细检查Scala编译器对尾递归函数approximate生成的字节码，则会看到，
   * 虽然isGoodEnough和improve是在方法体内被调用的，但是approximate函数自身并没有调用它们。Scala编译器已经将递归调用优化掉了∶
   * @编译源码在222页
   */

  /**
   * @尾递归的局限
   *
   * 在Scala中使用尾递归是比较受限的，因为用JVM指令集实现 形式更高级的尾递归非常困难。
   * Scala只能对那些直接尾递归调用自己的函数做优化。
   * 如果递归调用是间接的，例如，下面示例中的两个相互递归的函数，Scala就无法优化它们:
   */
  def isEven(x: Int): Boolean =
    if x == 0 then true else isOdd(x - 1)

  def isOdd(x: Int): Boolean =
    if x == 0 then false else isEven(x - 1)

  println(isOdd(2))


  /**
   *  同样地，如果最后一步调用的是一个函数值(不是发起调用的那个函数自己)，则也无法享受到尾递归优化。参考下面这段递归程序:
   */

  val funValue = nestedFun
  def nestedFun(x:Int):Unit =
    if x!=0 then
      println(x)
      funValue(x-1)

  /**
   * funValue变量指向一个本质上只是打包了对nestedFun调用的函数值。
   * 当你将这个函数应用到某个入参上时，它会转而将nestedFun应用到这个入参上，然后返回结果。
   * 因此，你可能希望Scala编译器能执行尾递归优化，不过编译器在这个情况下并不会这样做。
   * 尾递归优化仅适用于某个方法或嵌套函数在最后一步操作中直接调用自己，并且没有经过函数值或其他中间环节的场合。(如果你还没有完全理解尾递归，建议反复阅读本节。)
   */




  /**
   * @8.11结语
   *
   * 本章带你全面地了解了Scala中的函数。
   * 不仅限于方法，Scala还提供了局部函数、函数字面量和函数值;
   * 不仅限于普通的函数调用，Scala还提供了部分应用的函数和带有重复参数的函数等。
   * 只要有可能，函数调用都会以优化后的尾部调用实现，因此许多看上去很漂亮的递归函数运行起来也能与用while循环手工优化的版本一样快。
   * 下一章将在此基础上继续向你展示Scala对函数的丰富支持，以帮助你更好地对控制进行抽象。
   */
}
