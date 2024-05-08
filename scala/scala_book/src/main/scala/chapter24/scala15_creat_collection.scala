package chapter24

import scala.collection.immutable.HashSet

object scala15_creat_collection extends App {

  /**
   * 24.15 从头创建集合
   *
   * 你已经见过这样的语法:List(1,2,3)，用于创建由3个整数组成的列表;
   * Map('A' -> 1, 'C' -> 2)，用于创建带有两个绑定的映射。这实际上是Scala集合的一个通行的功能。
   * 你可以选择任何一个集合名，然后用圆括号给出元素的列表，结果就是带有给定元素的新集合。参考下面的例子:
   */
  Iterable() //空集合
  List() //空列表
  List(1.0, 2.0)
  Vector(1.0, 2.0)
  Iterator(1, 2, 3)
  Set("dog", "cat", "bird")
  HashSet("dog", "cat", "bird")
  Map('a' -> 7, 'b' -> 0)

  /**
   * 这些代码“在背后”都是调用了某个对象的apply方法。例如，上述代码的第三行展开以后就是:
   */
  List.apply(1.0,2.0)

  /**
   * 因此这是一个对List类的伴生对象的apply方法的调用。该方法接收任意数量的入参并基于这些入参构造出列表。
   * Scala类库中的每一个集合类都有一个带有这样的apply方法的伴生对象。至于集合类代表具体的实现，如List、LazyList、Vector等，
   * 还是特质，如Seq、Set或lterable，并不重要。对后者而言，调用apply方法将会生成该特质的某种默认实现。参考下面的例子:
   */
  List(1,2,3)
  Iterable(1,2,3)//List(1,2,3)
  scala.collection.mutable.Iterable(1,2,3)// ArrayBuffer(1,2,3)

  /**
   * 除了apply方法，每个集合的伴生对象还定义了 另一个成员方法empty，用于返回一个空的集合。
   * 因此除了写List()，也可以写List.empty，除了写Map()，也可以写Map.empty，等等。
   *
   * @Seq特质的后代还通过伴生对象提供了其他工厂方法，如表24.15所示。概括下来，包括如下这些。
   *    - concat:将任意数量的可遍历集合拼接在一起。
   *    - fill和tabulate:生成指定大小的单维或多维的集合，并用某种表达式或制表函数初始化。
   *    - range:用某个常量步长生成整数的集合。
   *    - iterate和unfold: 通过对某个起始元素或状态反复应用某个函数来生成集合。
   */


}
