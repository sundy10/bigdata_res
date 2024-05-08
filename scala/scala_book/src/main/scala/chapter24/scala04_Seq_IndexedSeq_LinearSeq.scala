package chapter24

import scala.collection.mutable.ListBuffer

object scala04_Seq_IndexedSeq_LinearSeq extends App {

  /**
   * 24.4 序列型特质Seq、IndexedSeq和LinearSeq
   *
   * Seq特质代表序列。
   * @序列是一种有长度（length）且元素都有固定的从O开始的下标位置的Ilterable特质。
   *
   * Seq特质包含的操作如表Seq.png Seq1.png所示。
   */
  println(List(2) :+ 1)

  //各操作归类如下。

  /**
   * @下标和长度操作
   *
   * apply、isDefinedAt、length、indices、lengthCompare和lengthls方法。
   * 对Seq而言，apply方法用于获取下标,类型为Seq[T]的序列是一个接收Int入参（下标)并交出类型为T的序列元素的偏函数。
   * 换言之，Seq[T]扩展自PartialFunction[Int,T]。序列的元素被从0开始索引(下标)，直到序列的长度减1。
   * 序列的length方法是通用集合的size方法的别名。
   * lengthCompare方法允许我们对两个序列的长度进行比较，哪怕其中一个序列的长度是无限的。
   * lengthls方法是sizels方法的别名。
   */

  /**
   * @下标检索操作
   *
   * indexOf、 lastIndexOf、indexOfSlice、lastIndexOfSlice、indexWhere、lastlndexWhere、
   * segmentLength和prefixLength方法
   *  @可以返回与给定值相等或满足某个前提条件的元素的下标。
   */

  /**
   * @添加操作
   *
   * +:(别名prepended)、++:(别名prependedAll)、:+(别名appended)、:++（别名appendedAll)
   * 和padTo方法可以返回通过在序列头部或尾部添加元素得到的新序列。
   */

  /**
   * @更新操作
   *
   * updated 和 patch方法可以返回通过替换原始序列中某些元素后得到的新序列。
   */

  /**
   * @排序操作
   *
   * sorted、sortWith 和 sortBy方法可以根据不同的条件对序列元素进行排序。
   */

  /**
   * @多重集操作
   *
   * intersect(交集)、diff(差集)、
   * distinct、distinctBy方法可以对两个序列的元素执行集类操作或移除重复项。
   */


  /**
   * @如果序列是可变的，则它会提供额外的带有副作用的update方法，允许序列元素被更新。
   * @回想一下第3章，类似seq(idx)=elem这样的方法只不过是seq.update(idx,elem)的简写。
   *
   * 需要注意update和updated方法的区别。
   * @update方法可以当场修改某个序列元素的值，且仅能用于可变序列。
   * @而updated方法对所有序列都可用，且总是会返回新的序列，而不是修改后的原序列。
   *
   * 每个Seq特质都有两个子特质———LinearSeq和IndexedSeq，且它们各自拥有不同的性能特征。
   * @线性的序列拥有高效的head和tail方法，而经过下标索引的序列拥有高效的apply、length和（如果是可变的)update方法。
   * @List是一种常用的线性序列，LazyList也是。而Array和ArrayBuffer是两种常用的经过下标索引的序列。
   * @Vector提供了介于索引和线性访问的有趣的妥协。从效果上讲，它既拥有常量时间的索引开销，也拥有时间线性的访问开销。
   * @由于这个特点，向量(vector）是混用两种访问模式（索引的和线性的）的一个好的基础。我们将在24.7节详细介绍向量。
   *
   *
   * @可变的IndexedSeq特质添加了一些用于当场变换其元素的操作。
   * 这些操作（见表24.3)并不像Seq上可用的map和sort那样返回新的集合实例。
   *
   * mutable.IndexedSeq特质包含的操作 IndexedSeq.png
   */

  /**
   * @缓冲(Buffer)
   *
   * @可变序列的一个重要子类目是缓冲。缓冲不仅允许对已有元素进行更新，还允许元素插入、移除和在缓冲末尾高效地添加新元素。
   * @缓冲支持的主要的新方法有:用于在尾部添加元素的+=（别名append)和++=(别名appendAll)方法，
   * @用于在头部添加元素的+=:(别名prepend）和++=:(别名prependAll）方法，用于插入元素的insert和insertAll方法，
   * @以及用于移除元素的remove、
   * @-=(别名substractOne)和--=(别名substractAll）)方法等(这两个方法只会移除第一个元素，不会遍历完Buffer)，
   * @如图Buffer.png,Buffer1.png所示。
   */
  println(ListBuffer(1,2,1,3,2) --= List(1,2))

  /**
   * 两个常用的Buffer实现是ListBuffer和ArrayBuffer。
   * 正如它们的名称所暗示的，ListBuffer的背后是列表，支持高效地转换为列表，
   * 而ArrayBuffer的背后是数组，支持快速地转换成数组。你曾在15.1节中看到过一部分ListBuffer的实现。
   */


}
