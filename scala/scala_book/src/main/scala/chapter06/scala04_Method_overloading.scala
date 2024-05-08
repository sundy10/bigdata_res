package chapter06

object scala04_Method_overloading extends App {

  /**
   * 现在每个算术方法都有两个版本:一个接收有理数作为参数;
   * 另一个则接收整数作为参数。
   *
   * 换句话说，每个方法名都被重载了，因为每个方法名都被用于多个方法。
   * 举例来说，+这个方法名被同时用于一个接收Rational参数的方法和另一个接收Int参数的方法。
   * 在处理方法调用时，编译器会选取重载方法中正确匹配了入参类型的版本。
   * 例如，如果x.+(y)中的y是有理数，编译器就会选择接收Rational参数的+方法。
   * 但如果入参是整数，编译器就会选择接收Int参数的那个方法。
   */

  class Rational(n: Int, d: Int) {
    require(d != 0)

    private val g = gcd(n.abs, d.abs)
    val numer = n / g
    val denom = d / g

    def this(n: Int) = this(n, 1)

    def +(that: Rational): Rational = Rational(numer * that.denom + that.numer * denom, denom * that.denom)

    def +(i: Int): Rational =
      Rational(numer + i * denom, denom)

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

    override def toString: String = s"$numer/$denom"

    private def gcd(a: Int, b: Int): Int =
      if b == 0 then a else gcd(b, a % b)
  }

  val r = Rational(2, 3)
  println(r * r) //4/9
  println(r * 2) //4/3

}
