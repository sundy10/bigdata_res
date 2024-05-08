package chapter06


object scala05_Extension_methods extends App {

  class Rational(n: Int, d: Int) {
    require(d != 0)

    private val g = gcd(n.abs, d.abs)
    val numer = n / g
    val denom = d / g

    def this(n: Int) = this(n, 1)


    override def toString: String = s"$numer/$denom"

    private def gcd(a: Int, b: Int): Int = {
      if b == 0 then a else gcd(b, a % b)
    }

    def +(that: Rational): Rational = Rational(numer * that.denom + that.numer * denom, denom * that.denom)

    def +(i: Int): Rational =
      Rational(numer + i * denom, denom)

    def -(that: Rational): Rational =
      Rational(numer * that.denom - that.numer * denom, denom * that.denom)

    def -(i: Int): Rational =
      Rational(numer - i * denom, denom)

    def *(that: Rational): Rational =
      Rational(numer * that.numer, denom * that.denom)

    def *(i: Int): Rational =
      Rational(numer * i, denom)

    def /(that: Rational): Rational =
      Rational(numer * that.denom, denom * that.numer)

    def /(i: Int): Rational =
      Rational(numer, denom * i)
  }

  object Rational {
    def apply(number: Int, denom: Int = 1): Rational = new Rational(number, denom)
  }

  /**
   * 6.12扩展方法
   *
   * 现在你已经可以写r *2，但是你可能还想交换两个操作元的位置，即2*r。很遗憾，这样还不行:
   *
   * 这里的问题是2* r等价于2.*(r)，因此这是一个对2这个整数的方法调用。但Int类并没有一个接收Rational参数的乘法方法。
   * 不过，Scala有另外一种方式来解决这个问题:可以为Int类创建一个接收有理数的扩展方法。可以向编译器里添加行:
   */

  extension (x: Int)
    def +(y: Rational) = Rational(x) + y
    def -(y: Rational) = Rational(x) - y
    def *(y: Rational) = Rational(x) * y
    def /(y: Rational) = Rational(x) / y

  val r = Rational(2, 3)
  println(2 * r) //4/3
  println(2 * r * 4) //4/3

}
