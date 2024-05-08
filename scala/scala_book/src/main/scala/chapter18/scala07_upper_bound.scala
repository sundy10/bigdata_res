package chapter18



object scala07_upper_bound extends App {

  /**
   * 18.7 上界
   *
   * 在示例14.2 (302页)中，展示了一个接收比较函数作为第一个入参，以及一个要排序的列表作为第二个（柯里化的）入参的归并排序函数。
   * 也可以用另一种方式来组织这样一个排序函数，那就是要求列表的类型是混入了Ordered特质的。
   * 就像我们在11.2节提到的，通过混入Ordered特质并实现Ordered特质的抽象方法compare，我们可以让类的使用方代码用<、>、<=和>=来比较这个类的实例。
   * 例如，示例18.10中混入了Ordered特质的Person类。
   */
  //混入了Ordered特质的Person类
  class Person(val firstName: String, val lastName: String) extends Ordered[Person] :
    override def compare(that: Person): Int =
      val lastNameComparison = lastName.compareToIgnoreCase(that.lastName)
      if lastNameComparison != 0 then
        lastNameComparison
      else
        firstName.compareToIgnoreCase(that.firstName)

    override def toString: String = s"$firstName $lastName"

  //有了这样的定义，可以像这样来比较两个人:
  val robert = new Person("Robert", "Jones")
  val sally = new Person("Sally", "Smith")
  println(robert < sally)

  /**
   * @为了确保传入这个新的排序函数的列表类型混入了Ordered特质，需要使用上界（upper bound)。
   *                                               上界的指定方式与下界的指定方式类似，只不过不是用表示下界的>:符号，而是用<:符号，如示例18.11所示。
   *
   *                                               通过“T<:Ordered[T]”这样的语法，我们告诉编译器类型参数T有一个上界Ordered[T]。
   *                                               这意味着传入orderedMergeSort方法的列表的元素类型必须是Ordered特质的子类型。
   *                                               所以，我们能够将List[Person]传递给orderedMergeSort方法，因为Person类混入了Ordered特质。参考下面的列表:
   */
  val people = List(
    Person("Larry", "Wall"),
    Person("Anders", "Hejlsberg"),
    Person("Guido", "van Rossum"),
    Person("Alan", "Kay"),
    Person("Yukihiro", "Matsumoto")
  )
//带有上界的归并排序函数
  def orderedMergeSort[T <: Ordered[T]](xs: List[T]): List[T] =
    def merge(xs: List[T], ys: List[T]): List[T] =
      (xs, ys) match
        case (Nil, _) => ys
        case (_, Nil) => xs
        case (x :: xs1, y :: ys1) =>
          if x < y then x :: merge(xs1, ys)
          else y :: merge(xs, ys1)

    val n = xs.length / 2
    if n == 0 then xs
    else
      val (ys, zs) = xs.splitAt(n)
      merge(orderedMergeSort(ys), orderedMergeSort(zs))

  /**
   * 由于这个列表的元素类型Person混入了Ordered[Person]（也就是说，它是Ordered[Person]的子类型)，因此可以将这个列表传入orderedMergeSort方法中:
   *
   * @虽然示例中的排序函数对于说明上界这个概念很有帮助，但是它实际上并不是Scala中利用Ordered特质设计排序函数的最通用的方式。
   * @举例来说，我们并不能使用orderedMergeSort方法对整数列表进行排序，因为Int类并不是Ordered[Int]的子类型:
   *
   * 在21.4节，我们将展示如何使用上下文参数(given parameter)和类型族(typeclass)来实现一个更通用的解决方案。
   */

  /**
   * 18.8 结语
   *
   * 本章介绍了信息隐藏的若干技巧，如私有构造方法、工厂方法、类型抽象和对象私有成员，
   * 还介绍了如何指定数据类型型变及型变对于类实现意味着什么，最后介绍了一种可以帮助我们更灵活地使用型变标注的技巧，即对方法类型参数使用下界。
   * 在下一章，我们将把注意力转向枚举。
   */


}
