package chapter07

object scala02_while extends App {

  /**
   * 用while循环计算最大公约数
   *
   * @param x
   * @param y
   * @return 最大公约数
   */
  def gcdLoop(x: Long, y: Long) =
    var a = x
    var b = y
    while a != 0 do
      val temp = a
      a = b % a
      b = temp
    b

  println(gcdLoop(10, 100))

  /**
   * 在这样一个(也是唯一的一个)类型为Unit的值，这个值叫作单元值(unit value)，
   * 被写作()。存在这样一个()值，可以说是Scala的Unit与Java的void的不同。
   */

  import scala.io.StdIn.readLine

  while
    val line = readLine()
    println(s"Read:$line")
    line != "1"
  do println(1)


  //最大公约数的函数式递归写法
  def gcd(x: Long, y: Long): Long =
    if y == 0 then x else gcd(y, x % y)


}
