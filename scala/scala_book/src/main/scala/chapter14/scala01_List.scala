package chapter14

object scala01_List extends App {

  /**
   * 第14章 使用列表
   *
   * 列表可能是Scala程序中最常使用的数据结构了。本章将对列表做详细的介绍，
   * 不仅会讲到很多可以对列表执行的通用操作，还将对使用列表的一些重要的程序设计原则做出讲解。
   */

  /**
   * 14.1 List字面量
   *
   * 前面的章节已经介绍过列表。一个包含元素'a'、'b和'c'的列表写作List("a', 'b', 'c')。下面是另外一些例子:
   */

  val fruit = List("apples", "oranges", "pears")
  val nums = List(1, 2, 3, 4)
  val diag3 = List(
    List(1, 0, 0),
    List(0, 1, 0),
    List(0, 0, 1)
  )
  val empty = List()

  /**
   * @列表与数组非常像，不过有两个重要的区别。首先，列表是不可变的。也就是说，列表的元素不能通过赋值改变。
   * @其次，列表的结构是递归的（即链表，linked-list)，而数组是扁平的。
   */
  
}
