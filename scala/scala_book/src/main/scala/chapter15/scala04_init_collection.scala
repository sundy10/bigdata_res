package chapter15

import scala.collection.mutable

object scala04_init_collection extends App {

  /**
   * 15.4 初始化集合
   *
   * 前面我们已经看到，创建和初始化一个集合最常见的方式是将初始元素传入所选集合的伴生对象的工厂方法中。
   * 我们只需要将元素放在伴生对象名后面的圆括号里即可，Scala编译器会将它转换成对伴生对象的apply方法的调用:
   */
  List(1, 2, 3)
  Set('a', 'b', 'c')

  import scala.collection.mutable

  mutable.Map("hi" -> 2, "there" -> 5)
  Array(1.0, 2.0, 3.0)

  /**
   * 虽然大部分时候我们可以让Scala编译器根据传入工厂方法中的元素来推断出集合的类型，但是有时候我们可能希望在创建集合时指定与编译器不同的类型。对于可变集合来说尤其如此。参考下面的例子
   * 这里的问题是stuff被编译器推断为Int类型的集合。如果我们想要的类型是Any，则需要显式地将元素类型放在方括号里，就像这样:
   */
  var stuff: mutable.Set[Any] = mutable.Set(42)
  stuff += "aabbccdd"

  /**
   * 另一个特殊的情况是当我们用其他的集合初始化当前集合的时候。举例来说，假设有一个列表，但我们希望用TreeSet来包含这个列表的元素。这个列表的内容如下:
   * 我们并不能将colors列表传入TreeSet的工厂方法中:
   *
   * @我们需要将列表转换成带有to方法的TreeSet:
   */
  val colors = List("blue", "yellow", "red", "green")

  val treeSet = colors to mutable.TreeSet

  /**
   * @这个to方法接收一个集合的伴生对象作为参数。可以用它在任意两种集合类型之间做转换。
   */

  /**
   * @转换成数组或列表
   *
   * 除了通用的用来做任意集合转换的to方法，还可以用更加具体的方法来将集合转换成常见的Scala集合类型。
   * 前面提到过，要用其他的集合初始化新的列表，只需要简单地对集合调用toList方法即可:
   */
  treeSet.toList

  /**
   * @而如果要初始化数组，就调用toArray方法:
   */
  println(treeSet.toArray.toBuffer)


  /**
   * 注意,虽然原始的colors列表没有排序，但是对TreeSet调用toList方法得到的列表中的元素是按字母顺序排序的。
   *
   * @当我们对集合调用toList或toArray方法时，生成的列表或数组中元素的顺序与调用elements方法获取迭代器产生的元素顺序一致。
   * @由于TreeSet[String]的迭代器会按照字母顺序产生字符串，因此这些字符串在对这个TreeSet调用toList方法所得到的列表中的元素也会按字母顺序出现。
   *
   * xs to List和xs.toList
   * @的区别在于，toList方法的实现可能会被xs的具体集合类型重写，以提供比默认实现（会复制所有集合元素)更高效的方式来将其元素转换成列表。
   * @例如，ListBuffer集合就重写了toList方法，实现了常量时间和空间的占用。
   * @需要注意的是，转换成列表或数组通常需要复制集合的所有元素，因此对于大型集合来说可能会比较费时。
   * @不过由于某些已经存在的API，我们有时需要这样做。而且，许多集合的元素本来就不多，因复制操作带来的性能开销并不高。
   */

  /**
   * @在可变和不可变的集或映射间转换
   *
   * 还有可能出现的一种情况是将可变的集或映射转换成不可变的版本，或者反过来。
   * 要完成这样的转换，可以用前一页展示的用列表元素初始化TreeSet的技巧。
   * 首先用empty创建一个新类型的集合，然后用++或++=(视具体的目标集合而定)添加新元素。
   * 下面是一个将前面例子中的不可变TreeSet先转换成可变集，再转换成不可变集的例子:
   */

  import scala.collection.mutable

  val mutaSet: mutable.Set[String] = treeSet to mutable.Set
  val immutaSet: mutable.Set[String] = mutaSet to scala.collection.mutable.Set

  /**
   * 也可以用同样的技巧来转换可变映射和不可变映射:
   */
  val muta = mutable.Map("i"->1,"ii"->2)
  val immu =muta to scala.collection.immutable.Map
  println(immu)


}
