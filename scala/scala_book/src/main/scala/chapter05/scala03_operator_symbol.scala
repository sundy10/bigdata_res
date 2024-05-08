package chapter05

object scala03_operator_symbol extends App {

  val sum = 1 + 2 //Scala 将调用1.+(2)


  /**
   *
   *
   * 事实上,Int类包含了多个重载（overloaded）的+方法，分别接收不同的参数类型。
   * 例如，Int类还有另—个也叫作+的方法，接收—个Long参数,返回一个Long类型的结果。例如:
   */
  val longSum = 1 + 2L // Scala 将调用1.+(2L)

  val s = "Hello, world!"
  s indexOf 'o' // Scala 将调用s.indexOf('o')
  "shell" contains "e" // Scala 将调用 shell.contains('e')

  /**
   * 中缀操作符
   *
   * 任何方法都可以是操作符
   *
   * 在Scala中，操作符并不是特殊的语法，任何方法都可以是操作符。是否让方法成为操作符取决于你如何“用”它。
   * 当你写下"s.indexOf('0')”时,indexOf并不是操作符;但当你写下“s indexOf'o”时，indexOf就是操作符了，因为你用的是操作符表示法。
   */

  //前缀操作符
  -7
  -2.0
  (2.0).unary_-
  def unary_!(boolean: Boolean): Boolean ={
    boolean
  }
  println(! false)


  //后缀操作符
  //implicit scala.language.postfixOps
  7.toString










}
