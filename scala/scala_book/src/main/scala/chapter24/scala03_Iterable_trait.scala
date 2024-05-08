package chapter24

import scala.collection.mutable.ListBuffer

object scala03_Iterable_trait extends App {

  /**
   * 24.3 Iterable 特质
   *
   * 在集合类继承关系顶端的是lterable[A]特质，其中，A表示集合元素的类型。
   * 该特质的所有方法都是通过一个名称为iterator的抽象方法来定义的，该方法会逐一交出集合的元素:
   *
   * def iterator :Iterator[A]
   */

  /**
   * @实现lterable特质的集合类只需要定义这个方法即可。其他方法都可以从lterable特质中继承。
   *
   * lterable特质还定义了很多具体的方法，其包含的操作如表24.1所示。 Iterable1
   */
  println(List().sizeIs < 10)//尽可能少地遍历元素的前提下将集合大小与定值做比较

  //这些方法可以归芙劝r。

  /**
   * @迭代操作
   *
   * foreach、grouped、sliding方法会按照迭代器（ iterator）定义的次序迭代地交出集合的元素。
   * grouped和sliding方法返回的是特殊的迭代器。这些迭代器不返回单个元素而是返回原始集合的子序列。
   * 这些子序列的大小要求可以通过这些方法的入参给出。
   *
   * @grouped方法会将元素分段成递进的区块，而sliding方法交出的是对元素的滑动窗口展示。这两者的区别可以通过如下代码清晰体现:
   */
  val xs = List(1,2,3,4,5)
  val git =xs.grouped(3) //an Iterator[List[Int]]  按每个List 3个元素 分两段
  println(git.next())
  println(git.next())

  val sit = xs.sliding(3)  //an Iterator[List[Int]]
  println(sit.next())
  println(sit.next())
  println(sit.next())

  /**
   * @添加操作
   *
   * ++(别名concat)方法可以将两个集合追加到一起，或者将某个迭代器（iterator）的所有元素添加到集合中。
   */

  /**
   * @映射操作
   *
   * map、flatMap和collect方法通对集合元素应用某个函数来产生一个新的集合。
   */

  /**
   * @转换操作
   *
   * toIndexedSeq、toIterable、toList、toMap、toSeq、toSet和toVector方法可以将一个lterable集合转换成不可变集合。
   * 对于所有这些转换，如果原集合已经匹配了需要的集合类型，就会直接返回原集合。
   * 例如，对列表应用toList方法会交出列表本身。toArray和toBuffer方法将返回新的可变集合，
   * 即使接收该调用的集合对象已经匹配了需要的集合类型，也会返回新的可变集合。to方法可以被用来转换到任何其他集合。
   */

  /**
   * @复制操作
   *
   * copyToArray方法，正如它的名称所暗示的，它会将集合元素复制到数组中。
   */

  /**
   * @大小信息操作
   *
   * isEmpty、nonEmpty、size、knownSize、sizeCompare和sizels方法都与集合大小相关。
   * @在某些情况下，计算某个集合的元素数量时可能需要遍历这个集合，如List。
   * @在另一些情况下，集合可能有无限多的元素，如LazyList.from(O)。knownSize、sizeCompare和sizels方法会尽量少地遍历集合元素，
   * @并给出关于集合元素数量的信息。
   */

  /**
   * @元素获取操作
   *
   * head、last、headOption、lastOption和find方法会选中集合中的首个或最后一个元素，或者首个满足前提条件的元素。
   * 不过需要注意的是，并非所有集合都有被定义得很清晰、完整的“首个”和“最后一个”的含义。举例来说，一个哈希集可能会根据元素的哈希键来存储元素，
   * 但这个值可能会变。在这种情况下，哈希集的“首个”元素可能也不同。如果某个集合总是以相同的顺序交出元素，它就是有序( ordered）的。大多数集合都是有序的，
   * 不过有一些（如哈希集)并不是（放弃顺序能带来额外的一些性能优势)。顺序通常对可重复执行的测试而言很重要，
   * 这也是Scala集合提供了所有集合类型的有序版本的原因。比如，HashSet的有序版本是ListedHashSet。
   */

  /**
   * @子集合获取操作
   *
   * takeWhile、 tail、init、 slice、take、 drop、 filter、dropWhile、filterNotwithFilter方法都可以返回满足某个下标区间或前提条件的子集合。
   */

  /**
   * @细分操作
   *
   * groupBy、 groupMap、 groupMapReduce、 splitAt、 span、partition和partitionMap方法可以将集合元素切分成若干个子集合。
   */

  /**
   * @元素测试
   *
   * exists、forall 和 count方法可以用给定的前提条件对集合元素进行测试。
   */

  /**
   * @折叠操作
   *
   * foldLeft、foldRight、reduceLeft、reduceRight方法可以对连续的元素应用某个二元操作。
   */

  /**
   * @特殊折叠操作
   *
   * sum、product、min和口max方法用于操作特定类型的集合（数值型或可比较类型)。
   */

  /**
   * @字符串操作
   *
   * mkString和addString方法提供了不同的方式来将集合转换成字符串。
   */

  /**
   * @视图操作
   *
   * 视图是一个惰性求值的集合。你将在24.13节了解到更多关于视图的内容。
   */

  /**
   * @lterable特质的子类目
   *
   * 在类继承关系中，lterable特质之下有3个特质:Seq、Set和Map。
   * 这些特质的一个共同点是它们都实现了PartialFunction特质，(关于偏函数的细节请参考13.7节。)
   * 定义了相应的apply和isDefinedAt方法。不过，每个特质实现PartialFunction特质的方式各不相同。
   */

  /////////////////////////////////////////////////////

  /**
   * 对序列而言，apply方法用于获取位置下标，而元素下标总是从0开始的。也就是说，Seq(1,2,3)(1)== 2。
   * @对集而言，apply方法用于进行成员测试。例如，Set('a', "b', 'c)('b') == true而Set)('b) == false。
   * 而对映射而言，apply方法用于进行选择。例如，Map(a' -> 1, 'b'->10, 'c' -> 100)('b') == 10。
   */

  /**
   * 在接下来的3节中，将分别介绍这3种集合的细节。
   */





}
