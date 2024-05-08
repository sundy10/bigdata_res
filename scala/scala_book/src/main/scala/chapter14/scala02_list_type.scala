package chapter14

object scala02_list_type extends App {

  /**
   * 14.2 List类型
   *
   * 与数组一样，列表也是同构(homogeneous)的:同一个列表的所有元素都必须是相同的类型。
   * 元素类型为T的列表的类型写作List[T]。例如，下面是同样的4个列表显式添加了类型后的样子:
   */
  val fruit:List[String] = List("apples","oranges","pears")

  val nums:List[Int] = List(1,2,3,4)

  val diag3:List[List[Int]]= List(
    List(1, 0, 0),
    List(0, 1, 0),
    List(0, 0, 1)
  )

  val empty:List[Nothing] = List()
  val empty1:List[Nothing] = Nil


  /**
   * Scala的列表类型是协变(covariant)的，意思是对于每一组类型S和T，如果S是T的子类型，List[S]就是List[T]的子类型。
   * 例如，List[String]是List[Object]的子类型。所以每个字符串列表都可以被当作对象列表，这很自然。
   *
   * 注意，空列表的类型为List[Nothing]。在Scala的类继承关系中，Nothing是底类型，这是一个特殊的类型，是所有其他Scala类型的子类型。
   * 由于列表是协变的，对任何T而言，List[Nothing]都是List[T]的子类型，
   * 因此既然空列表对象的类型为List[Nothing]，那么它可以被当作其他形如List[T]类型的对象。这也是编译器允许我们编写如下代码的原因:
   */
  // List[Nothing] 也是List[String]类型的
  val xs:List[String] = List()


}
