package chapter24

import scala.collection.mutable

object scala08_mutable extends App {

  /**
   * 24.8 具体的可变集合类
   *
   * 现在你已经了解了Scala在标准类库中提供的常用的不可变集合类，下面我们来看看那些可变的集合类。
   */

  /**
   * @数组缓冲(ArrayBuffer)
   *
   * 我们在15.1节已经介绍过数组缓冲。
   * @数组缓冲包括一个数组和一个大小。对数组缓冲的大部分操作都与对数组的操作速度一样，
   * 因为这些操作只是简单地访问和修改底层的数组。
   * @数组缓冲可以在尾部高效地添加数据。对数组缓冲追加元素需要的时间为平摊的常量时间。
   * @因此，数组缓冲对那些通过向尾部追加新元素来高效构建大集合的场景而言非常有用。下面是一些例子:
   */
  val buf = collection.mutable.ArrayBuffer.empty[Int]
  buf += 1
  buf += 10
  println(buf.toArray)


  /**
   * @列表缓冲(ListBuffer)
   *
   * 我们在15.1节也已经介绍过列表缓冲。列表缓冲与数组缓冲很像，只不过它内部使用的是链表而不是数组。
   * 如果你打算在构建完成后将缓冲转换成列表，就可以直接用列表缓冲。参考下面的例子:
   * (注: 本例和本节其他示例的解释器响应中出现的“buf.type”是一个单例类型(singleton type)。
   * buf.type意味着该变量持有的就是那个由buf引|用的对象。)
   */
  val buf1 = collection.mutable.ListBuffer.empty[Int]
  buf1 += 1
  buf1 += 10
  println(buf.toList)

  /**
   * @字符串构建器(StringBuilder)
   * @正如数组缓冲有助于构建数组，列表缓冲有助于构建列表一样，字符串构造器也有助于构建字符串。
   * 由于字符串构建器十分常用，它已经被引入默认的命名空间中。我们只需要简单地用new StringBuilder来创建即可，就像这样:
   */
  val strBuf = new StringBuilder
  strBuf += 'a'
  strBuf ++= "bcdef"
  println(strBuf.toString())

  /**
   * @数组双向队列(ArrayDeque)
   *
   * 数组双向队列是一个可变序列，支持在头部和尾部高效地添加元素。
   * 其内部使用的是一个可以重新调整大小的数组。如果你需要对缓冲做头部追加和尾部追加，就应当使用ArrayDeque而不是ArrayBuffer。
   */

  /**
   * @可变队列(Queue)
   *
   * 除了不可变队列，Scala还提供了可变队列。你可以像使用不可变队列那样使用可变队列，
   * 不过在追加元素的时候需要使用+=和++=操作符而不是enqueue方法。
   * @并且，对可变队列而言，dequeue方法只会简单地移除队列头部元素并返回。这里有一个例子:
   */
  val queue = new mutable.Queue[String]()
  queue += "a"
  queue ++= List("b", "c")
  println(queue.dequeue())
  println(queue)

  /**
   * @栈(Stack)
   *
   * Scala提供了可变的栈(stack)。这里有一个例子:
   */
  val stack = new mutable.Stack[Int]()
  stack.push(1)
  stack.push(2)
  println(stack.top)
  println(stack.top)
  println(stack.pop())
  println(stack.top)

  /**
   * 注意，Scala并没有不可变的栈，因为列表提供了同样的功能。对不可变的栈执行压栈（ push）操作与对列表执行::操作是一样的。
   * 而出栈( pop）操作相当于对列表同时调用head和tail方法。
   */


  /**
   * @可变数组序列(ArraySeq)
   * @可变数组序列是固定大小的可变序列，其内部使用Array[AnyRef]来存放元素,在Scala中的实现是ArraySeq类。
   * 如果你想要使用数组的性能特征，但又不想创建泛型的序列实例(你不知道元素的类型，
   * 也没有一个可以在运行时提供类型信息的ClassTag)，则可以选用可变数组序列。我们会在24.9节讲到数组的这些问题。
   */
  val seq: mutable.ArraySeq.type = mutable.ArraySeq

  /**
   * @哈希表
   *
   * 哈希表(hash table)底层用数组存放其元素，并且元素的存放位置取决于该元素的哈希码。
   * 向哈希表添加元素只需要常量时间，只要数组中没有其他元素拥有相同的哈希码即可。
   * 因此，只要哈希表中的对象能够按哈希码分布得足够均匀，哈希表的操作就非常快。
   * 正因为如此，Scala中默认的可变映射和可变集的实现都基于哈希表。
   *
   * 哈希集和哈希映射用起来与其他集或映射一样。参考下面这些简单的例子:
   */
  val map = collection.mutable.HashMap.empty[Int, String]
  map += (1 -> "make a web site")
  map += (3 -> "profit!")
  println(map.contains(1))
  println(map.contains(2))

  /**
   *    @对哈希表进行迭代时并不能保证按照某个特定的顺序。
   * 迭代只不过是简单地依次处理底层数组的元素，底层数组的顺序是什么样的就是什么样的。
   * 如果你需要采用某种有保证的迭代顺序，则可以使用 链式的哈希映射或哈希集，而不是常规的哈希映射或哈希集。
   *    @链式的哈希映射或哈希集与常规的哈希映射或哈希集的区别在于，它还包含了一个按照元素添加顺序保存的元素链表。
   *    对这样的集合的遍历总是按照元素添加的顺序来进行的。
   */

  /**
   * @弱哈希映射(mutable.WeakHashMap)
   *
   * 弱哈希映射(weak hash map）是一种特殊的哈希映射。对于这种哈希映射，垃圾收集器并不会跟踪 映射到其中的键的链接。
   * 这意味着如果没有其他引用指向某个键，则该键到它的关联值会从映射中消失。
   *    @弱哈希映射对类似缓存这样的任务而言十分有用，即那些你想要重用某个计算耗时的函数结果的场景。
   * 如果这些代表入参的键和函数结果被保存在常规的哈希映射中，则这个映射会无限增长，所有的键都不会被当作垃圾处理。
   * 使用弱哈希映射可以避免这个问题。一旦某个键对象不再可及，该条目就从会弱哈希映射中移除。
   * Scala中弱哈希映射的实现是对底层Java实现java.util.VWeakHashMap的包装。
   */


  /**
   * @并发映射
   *
   * 并发映射(concurrent map）可以被多个线程同时访问。
   * 除了常见的映射操作，concurrent.Map特质还提供了一些操作，如表24.11所示。
   *
   * concurrent. Map特质定义了用于允许并发访问的映射的接口。标准类库提供了该特质的两个实现。
   * 第一个实现是Java的java.util.concurrent. ConcurrentMap，
   * 通过标准的Java/Scala集合转换，可以自动转换成Scala映射（我们将在24.16节介绍这类转换)。
   * 第二个实现是TrieMap，这是一个哈希数组映射的字典树的无锁( lock-free）实现。
   */

  /**
   * @可变位组
   *
   * 可变位组与不可变位组一样，只不过它可以被当场修改。
   * 可变位组在更新方面比不可变位组要稍微高效一些，因为它不需要将那些没有改变的Long值反复 复制。这里有一个例子:
   */
  val bits = scala.collection.mutable.BitSet.empty
  bits += 1
  bits += 3
  println(bits)



}
