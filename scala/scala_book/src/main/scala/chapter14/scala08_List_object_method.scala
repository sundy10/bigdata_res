package chapter14

object scala08_List_object_method extends App {

  /**
   * 14.8 List对象的方法
   *
   * 到目前为止，我们在本章介绍的所有操作都是List类的方法，但我们其实是在每个具体的列表对象上调用它们的。
   *
   * @还有一些方法是定义在全局可访问对象scala.List上的，这是List类的伴生对象。
   * @某些操作方法是用于创建列表的工厂方法，而另一些操作方法则用于对特定形状的列表进行操作。这两类方法在本节都会介绍。
   */

  /**
   * @从元素创建列表:List.apply方法
   *
   * 我们已经看到过不止一次诸如List(1,2,3)这样的列表字面量。
   * 这样的语法并没有什么特别的地方。List(1,2,3)这样的字面量只不过是简单地将List对象应用到元素1、2、3上而已。也就是说，它与List.apply(1,2,3)是等效的:
   */
  println(List.apply(1, 2, 3)) //List(1, 2, 3)

  /**
   * @创建数值区间:List.range方法
   *
   * 我们在介绍map和flatMap方法的时候曾经用到过range方法，该方法创建的是一个包含了一个区间的数值的列表。
   * 这个方法最简单的形式是List.rang(from, until)，用于创建一个包含了从from开始递增到until减1的数的列表。
   * 所以终止值until并不是区间的一部分。
   */
  println(List.range(1, 5)) //List(1, 2, 3, 4)
  println(List.range(1, 8, 2)) //List(1, 3, 5, 7)
  println(List.range(9, 1, -3)) //List(9, 6, 3)
  println(List.range(9, 1, 3)) //List()

  /**
   * @创建相同元素的列表:List.fill方法
   *
   * fill方法创建包含零个或多个同一个元素副本的列表。它接收两个参数:要创建的列表长度和需要重复的元素。两个参数各自以不同的参数列表给出:
   */
  println(List.fill(5)("a"))
  println(List.fill(5)) //chapter14.scala08_List_object_method$$$Lambda$4/1393931310@2ef9b8bc
  println(List.fill(3)("hello"))

  /**
   * 如果我们给fill方法的参数多于1个，它就会创建多维的列表。也就是说，它将创建出列表的列表、列表的列表的列表，等等。多出来的这些参数需要被放在第一个参数列表中。
   */
  println(List.fill(3, 3)('b'))

  /**
   * @表格化一个函数:List.tabulate方法
   *
   * tabulate方法创建的是一个根据给定的函数计算的元素的列表，其入参 与List.fill方法的一样:
   * 第一个参数列表给出要创建列表的维度，
   * 而第二个参数列表给出描述列表的元素。唯一的区别是，元素值不再是固定的，而是从函数计算得来的:
   */
  val squares = List.tabulate(5)(n => n * n)
  println(squares)
  val multiplication =List.tabulate(5,5)(_*_)
  println(multiplication)
  //println(List.tabulate(4,4,4)(_*_*_))

  /**
   * @拼接多个列表:List.concat方法
   *
   * concat方法将多个列表拼接在一起。要拼接的列表可通过concat方法的直接入参给出:
   */
  println(List.concat(List('a','b'),List('c')))
  println(List.concat(List(),List('b'),List('c')))
  println(List.concat())



}
