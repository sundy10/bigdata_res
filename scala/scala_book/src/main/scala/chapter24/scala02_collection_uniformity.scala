package chapter24

import java.awt.Color
import scala.collection.immutable.HashMap
import scala.collection.{LinearSeq, SortedSet, mutable}

object scala02_collection_uniformity extends App {

  /**
   * 24.2 集合的一致性
   *
   * 下面给出了一些重要的集合类。这些类拥有相当多的共性。例如，每一种集合都可以用相同的一致语法来创建，先写下类名，再给出元素:
   */
  Iterable("x", "y", "z")
  Map("x" -> 24, "y" -> 25, "z" -> 26)
  Set(Color.RED, Color.GREEN, Color.BLUE)
  SortedSet("hello", "world")
  mutable.Buffer("x", "y", "z")
  IndexedSeq(1.0, 2.0)
  LinearSeq("a", "b", "c")

  //同样的原则也适用于特定的集合实现
  List(1, 2, 3)
  HashMap("x" -> 24, "y" -> 25, "z" -> 26)

  /**
   * 所有集合的toString方法也会生成上述格式的输出，即类型名称加上用圆括号括起来的元素。
   * 所有的集合都支持由lterable提供的API，不过它们的方法都返回自己的类型而不是根类型lterable。
   * 例如，List的map方法的返回类型为List，而Set的map方法的返回类型为Set。这样一来，这些方法的静态返回类型就比较精确:
   */
  println(List(1, 2, 3))
  println(List(1,2,3).map(_+2))
  println(Set(1,2,3).map(_*2))

  /**
   * 相等性对所有集合类而言也是一致的，这在24.12节会展开讨论。
   *
   * 图 23.1.png 中的大部分类都有3个版本:根、可变的和不可变的版本。唯一例外的是Buffer特质，它只作为可变集合出现。
   *
   * 下面将对这些类逐一进行讲解。
   */

}
