package chapter22

object scala03_collective_extension extends App {

  /**
   * 22.3 成组的扩展方法
   *
   * @当你想要对同一个类型添加多个方法时，可以用成组的扩展(collective———extension)来一起定义。
   * 举例来说，由于Int类的很多操作都会溢出，因此你可能会想要定义一些能够检测到溢出的扩展方法。
   *
   * 针对Int值的二进制补码计算过程是反转所有位然后加1。这样的表现形式允许减法被实现为一个二进制补码后面紧跟着一个加法。
   * 这个表现形式可以只用一个零值，而不是一个正零值和一个负零值。
   * 另一方面，由于没有负零值，因此会多出来一个槽位可以用来表示其他值。
   * (整数1的补码表现形式同时支持正零值和负零值，Float和Double值使用的IEEE 754浮点数格式也是这样。)
   * 这个多出来的值会被放到负整数的远端。这就是最小的可被表达的负整数比最大的可被表达的正整数的负值小1的原因。
   */
  println(Int.MaxValue) //2147483647
  println(Int.MinValue) //-2147483648

  /**
   * Int类的某些方法正是因为最大值和最小值不对称才会溢出。
   * 以Int类的abs方法为例，这个方法用于计算整数的绝对值。Int值的最小值的绝对值是2147483648，不过这个值无法用Int值来表达。
   * 这是因为Int值的最大值2147483647比它小1，对Int.MinValue调用abs方法会溢出，从而得到的是最初的MinValue:
   */
  println(Int.MinValue.abs) //-2147483648

  /**
   * 如果你想要一个返回Int值的绝对值且能检测到溢出的方法，则可以像这样定义扩展方法:
   */
  extension (n: Int)
    def absOption: Option[Int] =
      if n != Int.MinValue then Some(n.abs) else None
    def negateOption: Option[Int] =
      if n != Int.MinValue then Some(-n) else None

  /**
   * 对Int.MinValue调用abs方法会溢出，但对其调用absOption方法会返回None。
   * 而在其他情况下，absOption方法会返回用Some包装起来的调用abs方法的结果。这里有一些使用absOption方法的例子:
   */
  println(42.absOption)
  println(-42.absOption)
  println(Int.MinValue.absOption)
  println(Int.MinValue.absOption)

  /**
   * 另一个可能在Int值的最小值的位置上溢出的操作是取反(negation)。在MinValue的位置上，Int类的unary_-方法会再次返回MinValue:
   *
   * (如5.4节所介绍的，Scala会把 -Int.MinValue改写为对Int.MinValue的unary_-方法的调用，即Int.MinValue.unary_-。)
   */
  println(-Int.MinValue)

  /**
   * 如果你也想要一个unary_-的安全版本，则可以与absOption方法一起定义成组的扩展，
   * 如示例22.4所示。这个扩展方法将absOption和negateOption方法都添加到Int类中。这里有一些使用negateOption方法的例子:
   */
  println(42.negateOption)
  println(-42.negateOption)
  println(Int.MinValue.negateOption)
  println(Int.MinValue.negateOption)

  /**
   * 在成组的扩展方法中一起定义的方法被称为兄弟方法(siblingmethod）。
   * 在成组的扩展方法中的某一个方法中，你可以像调用同一个类的成员方法那样调用兄弟方法。
   * 举例来说，如果你决定同时给Int类添加一个isMinValue扩展方法，
   * 则可以在另外两个方法，也就是absOption和negateOption方法中直接调用这个方法，如示例22.5所示。
   */
  //调用兄弟扩展方法
  extension (n: Int)
    def isMinValue1: Boolean = n == Int.MinValue
    def absOption1: Option[Int] =
      if !isMinValue1 then Some(n.abs) else None
    def negateOption1: Option[Int] =
      if !isMinValue1 then Some(-n) else None

  /**
   * 在示例给出的成组的扩展方法中，absOption和negateOption方法都调用了isMinValue这个兄弟方法。
   * 在这些场景下，编译器会把调用改写为对接收者的调用。
   * 以示例22.5的扩展方法为例，编译器会把isMinValue调用改写为n.isMinValue，如示例22.6所示。
   */
  //都带有内部扩展标记 由编译器改写的成组的扩展方法
  def isMinValue2(n: Int): Boolean = n == Int.MinValue

  def absOption2(n: Int): Option[Int] =
      if !isMinValue2(n) then Some(n.abs) else None

  def negateOption2(n: Int): Option[Int] =
      if !isMinValue2(n) then Some(-n) else None


}
