package chapter22

object scala04_use_type_family extends App {

  /**
   * 22.4 使用类型族
   *
   * 求绝对值和取反操作时的溢出检测不仅对Int类型有用，对其他类型也有用。任何基于二进制补码的整数运算操作都面临同样的溢出问题，例如:
   */
  println(Long.MinValue.abs)
  println(-Long.MinValue.abs)
  println(Short.MinValue.abs)
  println(-Short.MinValue.abs)
  println(Byte.MinValue.abs)
  println(-Byte.MinValue.abs)

  /**
   * 如果你想对所有这些类型都是供安全的abs和unary_-方法，则可以针对每一个类型都定义单独的成组的扩展方法，但最终的实现可能一样。
   *
   * @为了避免这里的重复代码，可以定义由类型族(type-family)支持的扩展。这样一个特定目的的扩展 (ad hoc extension）
   *                                                     对任何该类型族的上下文参数实例类型都适用。
   */

  /**
   * 我们可以先看看标准类库中是否已经存在合适的类型族。
   * Numeric特质过于笼统，因为这个类型族对不是基于二进制补码的类型(如Double和口Float类型)也提供了上下文参数实例。
   * Integral特质也过于笼统，虽然它没有对Double和Float类型提供上下文参数实例，但是它对BigInt类型提供了上下文参数实例,而BigInt类型不会溢出。
   * 因此，你的最佳选择是自己定义一个专门用于二进制补码的整数类型的新类型族，如示例给出的TwosComplement特质。
   */

  /**
   * 接下来，你可以对那些你想要提供扩展方法的二进制补码类型定义上下文参数实例。
   * 伴生对象是一个不错的用于存放那些你预期用户总是会用到的上下文参数实例的地方。
   * 在示例22.7中，我们为Byte、Short、Int和Long类型定义了TwosComplement特质的上下文参数实例。
   */
  trait TwosComplement[N]:
    def equalsMinValue(n: N): Boolean

    def absOf(n: N): N

    def negationOf(n: N): N

  //针对二进制补码的整数类型的类型族
  object TwosComplement:
    given tcOfByte:TwosComplement[Byte] with
      override def equalsMinValue(n: Byte): Boolean = n ==Byte.MinValue
      override def absOf(n: Byte): Byte = n.abs
      override def negationOf(n: Byte): Byte = (-n).toByte

    given tcOfShort:TwosComplement[Short] with
      override def equalsMinValue(n: Short): Boolean = n ==Short.MinValue
      override def absOf(n: Short): Short = n.abs
      override def negationOf(n: Short): Short = (-n).toShort

    given tcOfInt:TwosComplement[Int] with
      override def equalsMinValue(n: Int): Boolean = n ==Int.MinValue
      override def absOf(n: Int): Int = n.abs
      override def negationOf(n: Int): Int = -n

    given tcOfLong:TwosComplement[Long] with
      override def equalsMinValue(n: Long): Boolean = n ==Long.MinValue
      override def absOf(n: Long): Long = n.abs
      override def negationOf(n: Long): Long = -n

  /**
   * 有了这些定义，就可以定义示例22.8所示的泛化的扩展方法了。
   * 这样一来,也就能对那些合理的类型使用absOption和negateOption方法了。这里有一些例子:
   */
  //在扩展方法中使用类型族
  extension [N](n:N)(using tc:TwosComplement[N])
    def isMinValue:Boolean =tc.equalsMinValue(n)
    def absOption:Option[N] = if !isMinValue then Some(tc.absOf(n)) else None
    def negateOption :Option[N] = if isMinValue then Some(tc.negationOf(n)) else None

  /**
   * 另一方面，在对那些不合理的类型使用这些扩展方法时，编译都会失败:
   *
   * 正如我们在21.4节讨论到的，
   * @类型族提供的是特定目的多态:相关功能对特定的具体类型(即存在类型族上下文参数实例的类型)适用，
   * 但对其他类型会给出编译错误。就扩展方法而言，你可以通过类型族来允许在特定的具体类型上使用扩展方法的语法糖。
   * 除此之外，对其他任何类型使用该扩展方法的尝试都将无法通过编译。
   */
  println(1L.absOption)
  println(Long.MinValue.absOption)



}
