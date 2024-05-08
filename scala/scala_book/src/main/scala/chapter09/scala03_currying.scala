package chapter09

object scala03_currying extends App {

  /**
   * 9.3 柯理化
   *
   * 第1章提到过,Scala允许创建新的控制抽象，"感觉就像是语言原生支持的那样”。
   * 虽然到目前为止你看到的例子的确都是控制抽象，但是应该不会有人会误以为它是语言原生支持的。
   * 为了弄清楚如何做出那些用起来感觉更像是语言扩展的控制抽象，首先需要理解一个函数式编程技巧，那就是柯里化（ currying)。
   *
   * 柯里化( currying）又称部分求值。一个柯里化的函数首先会接收一些参数，
   * 接收了这些参数后，该函数并不会立即求值，而是继续返回另外—个函数，刚才传入的参数在函数形成的闭包一中被保存起来。
   * 待到函数被真正需要求值的时候，之前传入的所有参数都会被─次性用于求值。
   *
   * 一个经过柯里化的函数在应用时支持多个参数列表，而不是只有一个。
   * 示例9.2展示了一个常规的、没有经过柯里化的函数，用于对两个Int参数x和y做加法。
   */

  def plainOldSum(x: Int, y: Int) = x + y

  println(plainOldSum(1, 2))

  /**
   * 与此相反，示例9.3展示了一个相似功能的函数，不过这次是经过柯里化的。
   * 与使用一个包含两个Int参数的列表不同，这个函数可以被应用到两个参数列表上，且每个列表包含一个Int参数。
   */
  def curriedSum(x: Int)(y: Int) = x + y

  println(curriedSum(1)(2))

  /**
   * @这里发生的事情是，当你调用curriedSum函数时，实际上是连着做了两次传统的函数调用。
   * @第一次调用接收了一个名称为x的Int参数，返回一个用于第二次调用的函数值，这个函数接收一个名称为y的Int参数。
   * 参考下面这个名称为first的函数，从原理上讲，与前面提到的curriedSum函数的第一次传统函数调用做了相同的事情:
   */
  def first(x: Int) = (y: Int) => x + y

  //把first函数应用到1上(换句话说，调用第一个函数，传入1)，将交出第二个函数:
  val second: Int => Int = first(1) //second的类型为： Int=> Int

  //将第二个函数应用到2上，将交出下面的结果:
  println(second(2))

  /**
   * 这里的first和second函数只是对柯里化过程的示意，它们并不与 curriedSum函数直接相关。
   *
   * 尽管如此，我们还是有办法获取指向curriedSum函数的“第二个”函数的引用。
   * 可以用eta延展在部分应用的函数表达式中使用curriedSum函数，就像这样:
   */
  val onePlus = curriedSum(1) // onePlus 的类型为 Int => Int

  /**
   * 通过上述代码,我们得到一个指向函数的引用，且这个函数在被调用时，将对它唯一的Int入参加1后，返回结果:
   */
  println(onePlus(2))

  //如果想得到一个对它唯的Int入参加2的函数，则可以这样做:
  val twoPlus = curriedSum(2)
  println(twoPlus(2))
}
