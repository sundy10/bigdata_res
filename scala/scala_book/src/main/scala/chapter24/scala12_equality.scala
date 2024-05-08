package chapter24

import scala.collection.mutable

object scala12_equality extends App {

  /**
   * 24.12 相等性
   *
   * 集合类库对相等性和哈希的处理方式是一致的。
   * 一方面，它将集合分为集、映射和序列等不同类目。不同类目下的集合永远不相等。
   * 例如，Set(1,2,3)不等于List(1,2,3)，尽管它们包含相同的元素。
   *
   * 另一方面，在相同的类目下，当且仅当集合拥有相同的元素时才相等
   *
   * @(对序列而言，不仅要求元素相同，还要求顺序相同)。 例如，List(1,2,3)== Vector(1,2,3)，而HashSet(1, 2)== TreeSet(1, 2)。
   *
   *                            至于集合是不可变的还是可变的并不会影响相等性检查。
   *                            对可变集合而言，相等性的判断仅取决于执行相等性判断时的元素。这意味着，随着元素的添加和移除，可变集合可能会在不同的时间点与不同的集合相等。
   * @当我们用可变集合作为哈希映射的键时，这是个潜在的“坑”。例如:
   */

  import collection.mutable.{HashMap, ArrayBuffer}

  val buf = ArrayBuffer(1, 2, 3)
  val map = mutable.HashMap(buf -> 3)  //Map( (ArrayBuffer(1,2,3), 3) )
  map(buf)    //3
  buf(0) += 1
  map(buf) //key not found: ArrayBuffer(2, 2, 3)

  /**
   * @在本例中，最后一行的选择操作很可能会失败，因为数组buf的哈希码在倒数第二行被改变了。因此，基于哈希码的查找操作会指向不同于xs (buf)的存储位置。
   */

}
