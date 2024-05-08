package chapter24

object scala10_string extends App {

  /**
   * 24.10 字符串
   *
   * 与数组一样，字符串也并不直接是序列，但是它可以被转换成序列，从而支持所有序列操作。下面是一些可以在字符串上执行的操作示例:
   */
  val str = "hello"
  str.reverse
  str.map(_.toUpper)
  str.drop(3)
  str.slice(1, 4)
  val s: Seq[Char] = str

  /**
   * 这些操作由两个隐式转换支持，我们在23.5节曾经介绍过。
   *
   * 第一个优先级较低的转换将String映射成WappedString类，这个类是immutable.IndexedSeq的子类。
   * 这个转换在前一个例子中的最后一行得以应用，将字符串转换成了Seq。
   *
   * 另一个较高优先级的转换将字符串映射成StringOps对象，
   * 这个对象给字符串添加了所有不可变序列的方法。这个转换在前面示例中的reverse、map、drop和slice等处被隐式插入。
   */

}
