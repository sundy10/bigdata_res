package chapter21

object scala02_context_parameter_type extends App {

  /**
   * 21.2 参数化的上下文参数类型
   *
   * 上下文参数最常见的用途可能是提供关于早前参数列表中被显式地提到的类型的信息，就像Haskell的类型族那样。
   * 这是用Scala编写函数时 实现特定目的多态(ad hoc polymorphism)的重要方式:你的函数可以被应用于那些类型讲得通的值，而对于其他类型的值则无法通过编译。
   * 参考示例14.1 (288页)中的两行插入排序代码。isort的定义只针对整数列表。
   * 为了对其他类型的列表排序，需要让isort的参数类型更加通用。要做到这一点，首先需要引入类型参数T，将List参数类型中的Int替换为T，就像这样:
   */
  //不能编译
  //  def isort[T](xs:List[T]):List[T] =
  //    if xs.isEmpty then Nil
  //    else insert(xs.head,isort(xs.tail))
  //
  //  def insert[T](x:T,xs:List[T]):List[T] =
  //    if xs.isEmpty || x <= xs.head then x::xs
  //    else xs.head :: insert(x,xs.tail)

  /**
   * 在完成上述变更之后，如果你尝试编译isort，则会得到如下的编译器信息:
   * value <= is not a member of T, ...
   *
   * 虽然Int类定义了 <= 方法用来判定某个整数是否小于或等于另一个整数，但是其他类型可能需要不同的比较大小的方法，或者根本不能比较大小。
   * 如果想让isort对 Int之外的其他类型的元素列表进行排序操作，则需要更多的信息来决定如何比较给定的两个元素。
   *
   * 为了解决这个问题，可以向isort提供适用于List(元素)类型的“小于或等于”函数。
   * 这个“小于或等于”函数必须消费两个类型T的实例并返回 表示第一个T实例是否小于或等于第二个T实例的 Boolean值:
   */
  def isort[T](xs: List[T])(lteq: (T, T) => Boolean): List[T] =
    if xs.isEmpty then Nil
    else insert(xs.head, isort(xs.tail)(lteq))(lteq)

  def insert[T](x: T, xs: List[T])(lteq: (T, T) => Boolean): List[T] =
    if xs.isEmpty || lteq(x, xs.head) then x :: xs
    else xs.head :: insert(x, xs.tail)(lteq)

  /**
   * 不同于 <= 方法，这里的insert助手方法在排序过程中使用lteq参数对元素进行两两比较。
   * 允许向isort传入比较函数使得对任意类型T的列表进行排序成为可能，只要你能给出对T适用的比较函数。
   * 例如，你可以用这个版本的isort对Int、String和示例6.5(112页)中的Rational类的列表进行排序，就像这样:
   */
  val lteqInt = (x: Int, y: Int) => x <= y
  val lteqStr = (x: String, y: String) => x.compareTo(y) <= 0
  println(isort(List(4, -10, 10))(lteqInt))
  println(isort(List("cherry", "blackberry", "apple", "pear"))(lteqStr))

  /**
   * 14.10节曾提到，Scala编译器对参数列表中的参数从左向右逐一推断参数类型。
   * 因此，编译器能够基于在第一个参数列表中传入的List[T]的元素类型T 推断出在第二个参数列表中给出的x和y的类型:
   */

  println(isort(List(4, -10, 10))((x, y) => x <= y))
  println(isort(List("cherry", "blackberry", "apple", "pear"))((x, y) => x.compareTo(y) <= 0))

  /**
   * 经过这样的改动，isort更加通用了，但这个通用的代价是代码变得更啰唆:每次调用时都需要给出比较函数，
   * 而且isort的定义必须把这个比较函数透传给每一次递归调用的isort，以及insert助手函数。
   * 这个版本的isort已不再是曾经那个简单的排序表达。
   */

  /**
   * 如果把isort的比较函数做成上下文参数，则可以同时减少isort的实现和isort调用点的代码。
   * 虽然也可以把(Int,Int) => Boolean做成上下文参数，但是这并不是最优的，因为类型要求过于笼统了。
   * 比如，你的程序可能包含了很多接收两个整数并返回一个布尔值的函数，但这些函数可能与对这两个整数进行排序没有任何关系。
   * 由于（编译器）对上下文参数的查找是基于类型的，因此应该尽量确保你的上下文参数的类型正确反映了它的真实意图。
   *
   * 针对某个特定的目的（如排序）定义类型通常是好的做法，不过，正如之前提到的，在使用上下文参数时，使用特定类型尤为重要。
   * 经过仔细定义的类型不仅能保证你使用正确的上下文参数，还能帮助你更清晰地表达意图。
   * 另外，它还可以让你渐进式地培育程序，在通过提供更多功能来丰富类型的同时，不打破类型之间已有的契约。
   * 你可以像这样来定义一个目的在于判定两个元素的次序的类型:
   */
  trait Ord[T]:
    def compare(x: T, y: T): Int

    def lteq(x: T, y: T): Boolean = compare(x, y) < 1

  /**
   * 该特质利用更通用的抽象方法compare来实现“小于或等于”函数。
   * 这个compare方法的契约是:在两个参数相等时返回0;在第一个参数大于第二个参数时返回正整数;在第一个参数小于第二个参数时返回负整数。
   * 有了这个定义，就可以接收Ord[T]作为上下文参数来给出针对T的比较大小的方法，参考示例21.2。
   */
  def isort1[T](xs: List[T])(using ord: Ord[T]): List[T] =
    if xs.isEmpty then Nil
    else insert1(xs.head, isort1(xs.tail))

  def insert1[T](x: T, xs: List[T])(using ord: Ord[T]): List[T] =
    if xs.isEmpty || ord.lteq(x, xs.head) then x :: xs
    else xs.head :: insert1(x, xs.tail)

  /**
   * @就像之前提到的，你可以通过在参数前加上using来表示该参数支持隐式传入。
   * 有了这个，你就不再需要在调用该函数时显式地提供这些参数:如果当前作用域存在需要的（满足类型要求的）上下文参数，
   * 编译器就会将这个值传递给你的函数。你可以用given关键字声明它。
   *
   * 与对应上下文相关的类型的伴生对象是存放那些“自然”的上下文参数（即针对某个类型的某个操作的一种自然方式)的一个不错的选择。
   * 例如，存放Ord[Int]类型的自然上下文参数实例的比较好的选择是放在Ord或Int这两个与Ord[Int]类型相关的类型的伴生对象中。
   * @如果编译器在语法规定的作用域内找不到满足条件的Ord[Int]，就会立即在这两个伴生对象中继续查找。
   * 由于你无法修改Int的伴生对象，因此Ord伴生对象就成了最佳选择:
   */
//  object Ord:
//    //(这还不够地道)
//    given intOrd: Ord[Int] = new Ord[Int] :
//      override def compare(x: Int, y: Int): Int = if x == y then 0 else if x > y then 1 else -1


  /**
   * 到目前为止，本章展示的声明上下文参数的例子都可以被称为别名上下文参数(alias given)。
   * 等号(=）左侧的名称是等号右侧值的别名。虽然在等号右侧声明别名上下文参数时定义某个特质或类的匿名实例是很常见的做法，
   * 但是Scala提供了一个更精简的语法，即用with关键字替换等号和“new类名”。示例21.3给出了使用这个更精简的语法的intOrd定义。
   *    (这里的with不同于第11章混入特质的用法。)
   */
  object Ord:
    //这样写是地道的
    given intOrd: Ord[Int] with
       def compare(x: Int, y: Int): Int = if x == y then 0 else if x > y then 1 else -1
    given stringOrd:Ord[String] with
      override def compare(x: String, y: String): Int = x.compareTo(y)

  /**
   * 有了Ord对象中的Ord[Int]上下文参数定义，通过isort执行排序的代码就再次变得精简:
   */
  println(isort1(List(10,2,-10)))

  /**
   * 当省去isort1的第二个参数列表时，编译器会基于参数类型查找合适的上下文参数值来作为第二个参数列表传入。
   * 对于要排序Int的场合，参数类型是Ord[Int]。编译器首先会在当前的语法作用域内查找给定的Ord[Int]，
   * 如果找不到，就会在Ord和Int这两个相关类型的伴生对象中继续查找。
   * 由于示例21.3给出的intOrd上下文参数值恰好具备正确的类型，因此编译器将使用它来填充缺失的参数列表。
   *
   * 而要对字符串排序，只需要提供一个针对字符串比较的上下文参数值即可:
   * 有了Ord对象中的Ord[String]上下文参数声明，就可以用isort对字符串的列表进行排序:
   */
  println(isort1(List("apple","hello","bear")))

  /**
   * 如果上下文参数不接收额外的值参数，则这个上下文参数会在它首次被访问时初始化，就像惰性的val那样。这个初始化的过程是线程安全的。
   * 如果上下文参数接收值参数，则每次访问都会创建新的上下文参数实例，这与def的行为很像。
   * Scala编译器的确会将上下文参数值转换成惰性的val或def，并额外将它标记为在参数前加上using时可用。
   *
   * 译者注:在声明上下文参数时，可以通过using关键字要求参与其构建的参数也通过上下文获取，而不是在当下就确定。
   * 上下文参数本质上是由编译器自动帮我们生成的可被隐式调取的对象。
   * 如果在创建时所有相关信息已知不会随着上下文改变，则编译器只会创建单个对象并复用;
   * 而如果在创建时有部分参数依赖上下文，则编译器会正确地帮我们实现成每次都从上下文重新获取这些参数后动态创建立
   */

}
