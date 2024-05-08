package chapter22

import chapter22.scala04_use_type_family.TwosComplement

object scala05_context_parameter_extend_method extends App {

  /**
   * 22.5 针对上下文参数的扩展方法
   *
   * 在前一节，TwosComplement特质的用意是实现对具体的一组类型开启扩展方法的设计目标。
   *
   * @由于我们的首要目标是为用户提供扩展方法，因此用户应该可以很容易地决定何时以及是否启用它。
   * 在这样的设计场景下，放置扩展方法的最好的选择是单例对象。
   * 基于这个单例对象，用户可以采用引入的方式将扩展方法带入语法作用域内，让它可用。
   * 举例来说，你可以将用于溢出检测的成组的扩展方法放置在名称为TwosComplementOps的对象中，如示例22.9所示。
   */
  //在单例对象中放置扩展方法
  object TwosComplementOps:
    extension[N] (n: N)(using tc: TwosComplement[N])
      def isMinValue: Boolean = tc.equalsMinValue(n)
      def absOption: Option[N] = if !isMinValue then Some(tc.absOf(n)) else None
      def negateOption: Option[N] = if isMinValue then Some(tc.negationOf(n)) else None

  /**
   * 这样一来，你的用户就可以采用如下方式将这些语法糖邀请到代码中:
   */

  import TwosComplementOps.*

  /**
   * 有了这个引入，这些扩展方法就可以被应用了:
   */
  println(-42.absOption)

  /**
   * 就TwosComplementOps对象而言，其主要的设计目标是扩展方法，而类型族扮演了支持角色。
   * 不过通常是反过来的:类型族是主要目标，而扩展方法扮演了让类型族更易于使用的支持角色。
   * 在这些场合下，放置扩展方法的最佳选择是类型族特质本身。
   */

  /**
   * 例如，在第21章，我们定义了Ord类型族，目的是让isort这个插入排序方法更为通用。
   * 虽然在第21章展示的解决方案中达成了这个目的（ isort可以被用于任何存在可见的Ord[T]上下文参数实例的类型T中)，
   * 但是如果我们能添加若干个扩展方法，则Ord类型族用起来会更舒服。
   */

  /**
   * 每个类型族特质都接收一个类型参数，这是因为类型族实例知道如何对那个类型的对象执行某项操作。
   * 举例来说，Ord[T]知道如何对两个类型T的实例做比较以判定其中一个实例是否大于、小于或等于另一个实例。
   * 由于针对T的类型族实例独立于它要操作的T的实例,因此使用类型族的语法可能会显得有些凌乱。
   * 例如，在示例21.2中，插入方法接收一个Ord[T]的上下文参数实例并用它来判定T的实例是否小于或等于一个已排好序的列表的头部元素。下面是从示例21.2摘抄的insert方法:
   *
   * def insert2[T](x: T, xs: List[T])(using ord: Ord[T]): List[T] =
   * if xs.isEmpty || ord.lteq(x, xs.head) then x :: xs
   * else xs.head :: insert2(x, xs.tail)
   *
   * @虽然这里的“ord.Ilteq(x,xs.head)”没有什么错误，但是很显然，更加自然的方式可以是这样的:
   *
   * x <= xs.head   //啊! 多么清晰
   *
   * 可以用成组的扩展方法来启用这样的 <=语法糖（还有<、>和>=)。在这个迭代版本中，扩展方法被放置在单例对象OrdOps中，如示例22.10所示。
   */

  import chapter21.scala03_anonymity_context_parameter.Ord

  //在单例对象中放置Ord的扩展方法 (还不是最好的设计)
  object OrdOps:
    extension[T] (lhs: T)(using ord: Ord[T])
      def <=(rhs: T): Boolean = ord.lteq(lhs, rhs)

  /**
   * 有了示例22.10所示的OrdOps对象的定义，用户就可以通过引入来邀请这些语法糖，就像这样:
   */
  def insert1[T](x: T, xs: List[T])(using ord: Ord[T]): List[T] =
    import OrdOps.*
    if xs.isEmpty || x <= xs.head then x :: xs
    else xs.head :: insert1(x, xs.tail)

  /**
   * 可以直接写“x <= xs.head”，而不是“ord.leqt(x,xs.head)”。
   * 除此之外，我们实际上并不需要对这个Ord实例命名，因为之后不再(直接)使用它了。因此, “(using ord: Ord[T)”可以被简化为"(using Ord[T])”。
   */

  /**
   * 这个方案是可行的，如果任何存在Ord实例的场合都有这个语法糖就好了。
   * 由于通常情况下都是这样的——Scala会帮助我们查找可以应用的上下文参数实例，
   * 因此放置这些扩展方法的最佳位置不是OrdOps这样的单例对象，而是Ord类型族特质本身。
   * 这样能够确保这些扩展方法在该类型族实例已经存在于作用域内时都可以被应用。可以写成如示例所示的样子:
   */
  //在类型族特质中放置扩展方法
  trait Ord1[T]:
    def compare(x: T, y: T): Int

    def lt(x: T, y: T): Boolean = compare(x, y) < 1

    def lteq(x: T, y: T): Boolean = compare(x, y) <= 1

    def gt(x: T, y: T): Boolean = compare(x, y) > 1

    def gteq(x: T, y: T): Boolean = compare(x, y) >= 1
    //(这是最好的设计)
    extension (lhs: T)
      def <(rhs: T): Boolean = lt(lhs, rhs)
      def <=(rhs: T): Boolean = lteq(lhs, rhs)
      def >(rhs: T): Boolean = gt(lhs, rhs)
      def >=(rhs: T): Boolean = gteq(lhs, rhs)

  /**
   * @在将这些扩展方法写在类型族特质本身之后，这些扩展方法就可以在任何使用该类型族的上下文参数实例的地方可见。
   * 以insert方法为例，这些扩展方法可以“正确工作”，而不需要被显式引入，如示例22.12所示。
   */
  //使用类型族特质中定义的扩展方法 这里编译器根据需要自动创建了Ord1的实例
  def insert2[T](x: T, xs: List[T])(using Ord1[T]): List[T] =
    if xs.isEmpty || x <= xs.head then x :: xs
    else xs.head :: insert2(x, xs.tail)

  /**
   * 由于不再需要引入“OrdOps.*”，因此示例22.12给出的这个版本的insert方法比前一个版本更精简。
   * 不仅如此，扩展方法本身也变得更简单了。
   * 我们可以比较一下示例22.10和示例22.11的版本。
   *
   * @由于扩展方法是类型族特质自身的一部分，它已经有一个指向类型族实例的引用，也就是this，
   * @因此你不再需要传递名称为ord的Ord[Int]实例，也就不能用这个实例来调用类型族的方法，如lt和lteq。
   * @但是你可以在this引用上调用这些方法，因此“ord.lt(Ihs,rhs)”就变成了“lt(Ihs,rhs)”。
   *
   * 由于Scala会当场改写扩展方法，因此这些方法将变成类型族特质自己的成员，如示例22.13所示。
   *
   * trait Ord1[T]:
   *    def compare(x: T, y: T): Int
   *    def lt(x: T, y: T): Boolean = compare(x, y) < 1
   *    def lteq(x: T, y: T): Boolean = compare(x, y) <= 1
   *    def gt(x: T, y: T): Boolean = compare(x, y) > 1
   *    def gteq(x: T, y: T): Boolean = compare(x, y) >= 1
        //With internal extension markers:
   *    def < (rhs: T)(lhs: T): Boolean = lt(lhs, rhs)
   *    def <= (rhs: T)(lhs: T): Boolean = lteq(lhs, rhs)
   *    def > (rhs: T)(lhs: T): Boolean = gt(lhs, rhs)
   *    def >= (rhs: T)(lhs: T): Boolean = gteq(lhs, rhs)
   *
   * 在查找可以解决某个类型错误的扩展方法时，Scala会检查Ord[T]上下文参数实例的内部。而且，Scala用来查找扩展方法的算法有些复杂。接下来将介绍这些细节。
   */


}
