package chapter14

object scala04_LIst_operate extends App {

  val fruit = "apples"::("oranges"::("pears"::Nil))

  val empty:List[Nothing] = Nil

  val diag3:List[List[Int]]= List(
    List(1, 0, 0),
    List(0, 1, 0),
    List(0, 0, 1)
  )

  /**
   * 14.4 列表的基本操作
   *
   * 对列表的所有操作都可以用下面这3项来表述。
    - head:返回列表的第一个元素。
    - tail:返回列表中除第一个元素之外的所有元素。
    - isEmpty:返回列表是否为空列表。
   */

  println(empty.isEmpty)
  println(fruit.isEmpty)
  println(fruit.head)
  println(fruit.tail.head)
  println(diag3.head)

  /**
   * 其中, head和tail方法只对非空列表有定义。当我们从一个空列表调用它们时，将抛出异常:
   */
  //println(Nil.head)

  /**
   * 作为演示如何处理列表的例子，应当考虑按升序排列一个数字列表的元素。
   * 一个简单的算法是插入排序（ insertion sort)。这个算法的工作原理如下:对于非空列表x ::xs，先对xs排序，再将第一个元素x插入这个排序结果中正确的位置。
   *
   * 对一个空列表排序将交出空列表。使用Scala代码来表示，这个插入排序算法如示例14.1所示。
   */
  def isort(xs:List[Int]):List[Int]=
    if xs.isEmpty then Nil
    else insert(xs.head,isort(xs.tail))
    // x head  tail
    //insert(4,isort(3,1,2))
    //insert(4, insert(3, isort(1,2)  ) )
    //insert(4, insert(3, insert(1,isort(2))  ) )
    //insert(4, insert(3, insert(1,insert(2,isort(Nil)))  ) )

    //isort(4,)

  def insert(x: Int, xs: List[Int]):List[Int] =
    if xs.isEmpty || x<=xs.head then x::xs
    else xs.head::insert(x,xs.tail)


  println(isort(List(12,342,438,428,3916,319)).toBuffer)

}
