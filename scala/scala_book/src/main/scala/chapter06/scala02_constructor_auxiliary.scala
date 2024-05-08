package chapter06

object scala02_constructor_auxiliary extends App {

  /**
   * 辅助构造器
   *
   * 在Scala中，每个辅助构造方法都必须首先调用同一个类的另一个构造方法。
   * 换句话说，Scala每个辅助构造方法的第一条语句都必须是这样的形式:“this(..)”。
   * 被调用的这个构造方法要么是主构造方法(就像Rational示例那样)，要么是另一个出现在发起调用的构造方法之前的另一个辅助构造方法。
   * 这个规则的净效应是Scala的每个构造方法最终都会调用该类的主构造方法。这样一来，主构造方法就是类的单一入口。
   */

  class Rational(n: Int, d: Int) {
    require(d != 0)

    //最大公约数
    private val g = gcd(n.abs, d.abs)

    val numer: Int = n / g
    val denom: Int = d / g

    def this(n: Int) = this(n, 1)

    override def toString: String = s"$numer/$denom"

    def +(that: Rational): Rational = Rational(
      numer * that.denom + that.numer * denom,
      denom * that.denom
    )

    def *(that: Rational): Rational =
      Rational(numer * that.numer, denom * that.denom)

    def lessThan(that: Rational) =
      numer * that.denom < that.numer * denom

    def max(that: Rational) =
      if lessThan(that) then that else this

    private def gcd(a: Int, b: Int): Int =
      if b == 0 then a else gcd(b, a % b)
  }

  println(Rational(66, 42)) //11/7
  println(Rational(1, 2))
  println(Rational(2, 3))
  println(Rational(1, 2) + Rational(2, 3)) //7/6


  /**
   * 另一个值得注意的点是，按照Scala的操作符优先级（在5.9节介绍过)，对于Rational类来说，
   * *方法会比+方法绑得更紧。换句话说，涉及Rationa对象的+和*操作，其行为会像我们预期的那样。
   * 比如，x+x *y会被当作x +( *y)执行，而不是(x+ x)*y:
   */

  

}
