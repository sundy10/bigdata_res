package chapter21

object scala03_anonymity_context_parameter extends App {

  trait Ord[T]:
    def compare(x: T, y: T): Int

    def lteq(x: T, y: T): Boolean = compare(x, y) < 1

  /**
   * 21.3 匿名上下文参数
   *
   * 虽然你可以把上下文参数的声明想象成惰性的val或def的特殊写法，但是上下文参数与这两个概念还是有一个重要的区别。
   * 举例来说，在声明val的时候，通常给出的是指向该val的值的名称:
   */
  val age = 42

  /**
   * 在这个表达式中，编译器必须推断出age的类型。由于age被初始化成42，而编译器知道42是Int类型的，因此推断出age的类型为Int。
   * 实际的效果就是你提供了一个名称（term) age，而编译器推断出它的类型为Int。
   *
   * 对上下文参数而言，这个过程是倒过来的:你给出一个类型，然后编译器基于可用的上下文参数，帮你合成一个用于表示该类型的名称，
   * 以便在需要这个类型的地方隐式地使用这个名称来表示。我们将这个过程称为名称推断（term inference)，以便与类型推断区分开。
   *
   * 由于编译器是通过类型来查找上下文参数的，通常完全不需要用某个名称来指代这个上下文参数，因此可以以匿名的方式来声明上下文参数值。你可以不这样写:
   */
  given revIntOrd:Ord[Int] with
    override def compare(x: Int, y: Int): Int =  if x == y then 0 else if x > y then 1 else -1

  given revStringOrd:Ord[String] with
    override def compare(x: String, y: String): Int = x.compareTo(y)

  /*
  *而是这样写
  */
  object Ord:
    given Ord[Int] with
      override def compare(x: Int, y: Int): Int =  if x == y then 0 else if x > y then 1 else -1

    given Ord[String] with
      override def compare(x: String, y: String): Int = x.compareTo(y)

  /**
   * 对这些匿名的上下文参数而言，编译器会自动帮助你合成相应的名称。
   * 在isort中，第二个参数可以用这个合成的值来填充，这个值在函数内就变得可见。
   * @如果你只关心你的上下文参数能否在需要时被隐式地提供，则完全不需要为它声明一个名称。
   */
  def isort2[T](xs: List[T])(using ord: Ord[T]): List[T] =
    if xs.isEmpty then Nil
    else insert2(xs.head, isort2(xs.tail))

  def insert2[T](x: T, xs: List[T])(using ord: Ord[T]): List[T] =
    if xs.isEmpty || ord.lteq(x, xs.head) then x :: xs
    else xs.head :: insert2(x, xs.tail)

  println(isort2(List(100,-12,13)))



}
