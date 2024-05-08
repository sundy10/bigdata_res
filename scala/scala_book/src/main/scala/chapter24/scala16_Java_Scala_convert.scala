package chapter24

import java.util
import scala.collection.mutable

object scala16_Java_Scala_convert extends App {

  /**
   * 24.16 Java和Scala集合转换
   *
   * 像Scala一样,Java也有丰富的集合类库。这两者之间有很多相似之处。
   * 比如，两个集合类库都有迭代器、iterable、集、映射和序列。不过它们之间也有一些重大的区别。
   * 特别是Scala的类库更加强调不可变集合，并提供了更多将集合转换成新集合的操作。
   */

  /**
   * 有时候，你可能需要从其中一个集合框架转换到另一个集合框架。
   *
   * @例如，你可能想要访问某个已有的Java集合，并把它当作Scala集合那样。
   * @又或者，你想要将某个Scala集合传递给某个预期Java集合的方法。
   * 这些都很容易做到，因为Scala在JavaConversions对象中提供了所有主要的集合类型之间的隐式转换。
   * 具体来说，你会找到如下类型之间的双向转换:
   *
   * Iterator <=> java.util.Iterator
   * Iterator <=> java.util.Enumeration
   * Iterator <=> java.lang.Iterable
   * Iterator <=> java.util.Collection
   * mutable.Buffer <=> java.util.List
   * mutable.Set <=> java.util.Set
   * mutable.Map <=> java.util.Map
   *
   * 要允许这些转换，只需要像这样做一次引入:
   */
  //import jdk.CollectionConverters.*

  import collection.JavaConverters.*

  /**
   * 现在你就拥有了在Scala集合和对应的Java集合之间自动互转的能力。
   */
  import collection.mutable.*

  val jul: util.List[Int] = ArrayBuffer(1, 2, 3).asJava

  val scala: mutable.Buffer[Int] = jul.asScala

  val m: util.Map[String, Int] = mutable.HashMap("abc" -> 1, "hello" -> 2).asJava

  /**
   * @在内部，这些转换是通过设置一个“包装”对象并将所有操作转发到底层集合对象来实现的。因此，集合在Java和Scala之间转换时，并不会进行复制操作。
   * 一个有趣的属性是，如果你完成一次双向的转换，比如，将Java类型转换成对应的Scala类型后再转换回原先的Java类型，则得到的还是最开始的那个集合对象。
   *
   * 还有其他的一些常用的Scala集合可以被转换成Java类型，不过并没有另一个方向的转换与之对应。这些转换有:
   *
   * Seq => java.util.List
   * mutable.Seq => java.util.List
   * Set => java.util.Set
   * Map => java.util.Map
   *
   * 由于Java并不在类型上区分可变集合和不可变集合，因此在将collection.immutable.List转换成java.util.List后，如果尝试对它进行变更操作，
   * 将会抛出UnsupportedOperationException。参考下面的例子:
   */
  val jul1:java.util.List[Int] = List(1, 2, 3).asJava

  jul1.add(7)   //java.lang.UnsupportedOperationException


  /**
   * 24.17 结语
   *
   * 现在你已经看到了使用Scala集合的大量细节。Scala集合采取的策略是为你提供功能强大的构建单元，而不是很随意的工具方法。
   * 将两三个这样的构建单元组合在一起，就可以表达出大量非常实用的计算逻辑。
   * 这种类库设计风格之所以有效，应当归功于Scala对函数字面量的轻量语法支持，以及它提供了许多持久的不可变的集合类型。
   */


}
