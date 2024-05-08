package chapter21

import chapter21.scala03_anonymity_context_parameter.isort2

object scala04_type_family_context_parameter extends App {

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


  /**
   * 21.4 作为类型族的参数化上下文参数
   *
   * 你可以对任何想要排序的类型T提供Ord[T]上下文参数。比如，你可以通过定义一个Ord[Rational]上下文参数来允许对示例6.5中展示的Rational类的列表进行排序。
   * 由于这是一种对有理数排序的自然的方式，因此把这个上下文参数实例放在Rational类的伴生对象中很合适:
   */

  import scala03_anonymity_context_parameter.Ord

  object Rational:
    def apply(number: Int, denom: Int = 1): Rational = new Rational(number, denom)

    given rationalOrd: Ord[Rational] with
      def compare(x: Rational, y: Rational) =
        if x.numer * y.denom < x.denom * y.numer then -1
        else if x.numer * y.denom > x.denom * y.numer then 1
        else 0

  /**
   * 然后，就可以对Rational类的列表进行排序了:
   */
  println(isort2(List(Rational(4, 5), Rational(1, 2), Rational(2, 3))))

  /**
   * 根据里氏替换原则，对象可以被替换成它的子类型而不改变程序的预期正确性。
   * 这一点是面向对象编程中子类型超类型关系的核心。在示例给出的最新版本的isort2中，
   * 看上去你可以把String的列表替换成Int或Rational的列表，而isort2仍然能够如预期一般工作。
   * 这似乎在暗示Int、Rationa和String可能具有某个公共的超类型，比如，某种“可排序”类型。(11.2节和18.7节描述的Ordered特质就是这样的可排序类型)
   * 然而它们并没有这样的公共超类型。不仅如此，想要定义这样一种针对Int或String类型的超类型也是不可能的，因为它们是Java和Scala标准库的一部分。
   */

  /**
   * 为特定的类型T提供Ord[T]的上下文参数实例相当于给T颁发了一张“可排序的类型”俱乐部的会员卡，尽管实际上这些类型并没有任何公共的可排序的超类型。
   * 这样一组类型被称为类型族(typeclass)。
   * 举例来说，到现在为止，Ord类型族包括3个类型: Int、String和Rational。这一组类型（如果用T来表示)都存在对应的Ord[T]的上下文参数实例。
   * 第23章将给出更多的类型族的示例。由于示例21.2给出的isort2实现接收类型为Ord[T]的上下文参数，
   * 因此它可以被归类为一种特定目的多态: isort2能对特定的类型T的列表排序（条件是存在Ord[T]的上下文参数实例)，
   * 但不能对其他类型的列表排序（无法通过编译)。用类型族实现特定目的多态是合乎范式的Scala编程中的一项重要的、常用的技巧。
   */

  /**
   * @Scala的标准类库提供了针对不同目的的现成可用的类型族，比如，定义相等性或者在排序时指定元素次序。
   * @本章中使用的Ord类型族是Scala的math.Ordering类型族的不完整的重新实现。Scala类库定义了常见类型，如Int和String的Ordering类型族实例。
   *
   * 示例21.4给出了使用Scala的Ordering类型族的isort版本。注意这个版本的isort的上下文参数并没有参数名，只是使用了using关键字加上参数类型Ordering[T]的写法。
   * 这种写法被称为匿名参数( anonymous parameter)。由于这个参数只是隐式地在函数中被使用(被隐式地传入insert和isort)，因此Scala并不要求我们给它起名。
   */
  //使用Ordering的插入排序函数
  def isort3[T](xs: List[T])(using Ordering[T]): List[T] =
    if xs.isEmpty then Nil
    else insert3(xs.head, isort3(xs.tail))

  def insert3[T](x: T, xs: List[T])(using ord: Ordering[T]): List[T] =
    if xs.isEmpty || ord.lteq(x, xs.head) then x :: xs
    else xs.head :: insert3(x, xs.tail)

  /**
   * 作为另一个说明什么是特定目的多态的例子，我们可以回顾一下示例18.11(406页)中的orderedMergeSort方法。
   * 这个排序方法可以对任何(类型自身为)Ordered[T]的子类型T的列表进行排序。
   * 这种写法被称为子类型多态(subtyping polymorphism)，如我们在18.7节讲解的，
   * Ordered[T]这个类型上界意味着你不能对Int或 String的列表使用orderedMergeSort方法。
   * 示例21.5给出的msort则不同，你可以用它来对Int String的列表排序，
   * 因为它要求的Ordering[T]是不同于T的单独的(separate）一组类型关系。
   * 虽然你无法通过更改Int来让它扩展Ordered[Int]，但是可以定义并提供一个Ordering[Int]上下文参数实例
   */

  /**
   * 示例21.4给出的isort和示例21.5给出的msort都是使用上下文参数提供关于某个在更早的参数列表中被显式地给出的类型的更多信息的例子。
   * 具体而言，类型为Ordering[T]的上下文参数提供了关于类型T的更多信息，对应到本例中就是如何对T排序。
   * 类型T在xs参数的类型List[T]中被提到，而该参数出现在更早的参数列表中。由于在任何对对isort和msort的调用中都必须显式地提供xs，
   * 因此编译器在编译期就能确切地知道T，从而能够判定是否存在可用的类型为Ordering[T]的上下文参数实例。如果存在，则编译器将隐式地传入第二个参数列表。
   */


}
