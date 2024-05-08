package chapter18

object scala01_type_parameterized extends App {

  /**
   * 第18章 类型参数化
   *
   * 本章将介绍Scala类型参数化的细节。在这个过程中，我们将通过一个具体的例子来展示第12章介绍过的信息隐藏的技巧: 设计一个纯函数式的队列类。
   *
   * 类型参数化让我们能够编写泛型的类和特质。
   * 例如，集(set)是泛型的，接收一个类型参数，定义为Set[T]。
   * 这样一来，具体的集的实例可以是Set[String]、Set[Int]等，不过必须是某种类型的集。
   *
   * @与Java不同，Scala并不允许原生类型，而是要求我们给出类型参数。型变定义了参数化类型的继承关系，
   * 比如，以Set[String]为例，型变定义了它是不是Set[AnyRef]的子类型。
   *
   * 本章包含3部分。
   * 第一部分介绍一个表示纯函数式队列的数据结构的开发。
   * 第二部分介绍将这个结构的内部表现细节隐藏起来的技巧。
   * 第三部分介绍类型参数的型变，以及它与信息隐藏的关系。
   */

  /**
   * 18.1 函数式队列
   *
   * @函数式队列是一个数据结构，它支持以下3种操作。
   *    - head:返回队列的第一个元素。
   *    - tail:返回除第一个元素外的队列。
   *    - enqueue:返回一个将给定元素追加到队尾的新队列。
   * @与可变队列不同，函数式队列在新元素被追加时，其内容并不会改变，而是会返回一个新的包含该元素的队列。
   * 本章的目标是创建一个名称为Queue的类，使用效果如下:
   */

  import scala.collection.immutable.Queue

  val q = Queue(1, 2, 3)
  println(q)
  val q1 = q.enqueue(4)
  println(q1)

  /**
   * @如果Queue的实现是可变的，则上述代码的第二步enqueue操作会影响q的内容。
   * 事实上，在操作过后，结果队列q1和原始队列q都将包含序列1、2、3、4。不过对函数式队列而言，被追加的值只会出现在结果q1中，而不会出现在被执行该操作的队列q中。
   * @纯函数式队列还与列表有一些类似。它们都被称为完全持久化 (fully persistent)的数据结构，在经过扩展或修改之后，旧版本将继续保持可用。
   *                              它们都支持head和tail操作。不过列表通常用::操作在头部扩展，而队列通常用enqueue操作在尾部扩展。
   */

  /**
   * 如何实现才是高效的呢?在最理想的情况下，一个函数式（不可变)的队列与一个指令式（可变)的队列相比，
   *
   * @不应该有从根本上讲更高的额外开销。也就是说，所有的3种操作，即head、tail和enqueue操作都应该以常量时间完成。
   *
   * 实现函数式队列的一种简单方式是用列表作为表现类型。这样一来，head和tail都只是被简单地翻译成对列表的操作，而enqueue操作则通过列表拼接来实现。
   *
   * 这让我们得到下面的实现:
   */
  class SlowAppendQueue[T](elems: List[T]): //不高效
    def head = elems.head

    def tail = new SlowAppendQueue(elems.tail)

    def enqueue(x: T) = SlowAppendQueue(elems ::: List(x))

  /**
   * 这个实现的问题出在enqueue操作上。它的时间开销与队列中存放的元素数量成正比。
   * 如果你想要常量时间的追加操作，则可以尝试将底层列表中的元素顺序反转过来，使最后追加的元素出现在列表的头部。这让我们得到下面的实现:
   * (xs ::: ys这样的列表拼接所需要的时间与首个入参xs的长度成正比。)
   */
  class SlowHeadQueue[T](smele: List[T]): //不高效
    def head = smele.head

    def tail = new SlowAppendQueue(smele.tail)

    def enqueue(x: T) = SlowAppendQueue(x :: smele)

  /**
   * 现在enqueue操作是以常量时间完成的了，但head和tail操作并不是。它们现在的时间开销与队列中的元素数量成正比。
   *
   * 从这两个例子来看，似乎并没有一个实现可以对所有3种操作都做到以常量时间完成。
   * 事实上，这看上去几乎是不可能做到的。不过，将两种操作结合到一起，就可以非常接近这个目标。
   * 这背后的理念是用leadingtrailing两个列表来表示队列。leading列表 包含队列中靠前的元素，而trailing列表包含队列中靠后的元素，并按倒序排列。
   * 整个队列在任何时刻的内容都等于“leading :::  trailing.reverse”。
   *
   * 现在要追加一个元素，只需要用::操作符将它追加到trailing列表中，这样一来，enqueue操作的完成就是常量时间的。
   * 这意味着，当开始为空的队列通过接连的enqueue操作初始化时，trailing列表会增长而leading列表会保持空的状态。
   * 接下来，在首次head或tail操作被执行到空的leading列表之前，整个trailing列表会被复制到leading列表中，同时元素的顺序会被反转。这是通过一个名称为mirror的操作完成的。
   * 示例18.1给出了使用该实现方案的队列。(基本的函数式队列)
   */
  class Queue1[T](private val leading: List[T],
                 private val trailing: List[T]):
    private def mirror =
      if leading.isEmpty then
        new Queue1(trailing.reverse, Nil)
      else this

    def head = mirror.leading.head

    def tail =
      val q = mirror
      new Queue1(q.leading.tail, q.trailing)

    def enqueue(x: T) =
      new Queue1(leading, x :: trailing)

  /**
   * 这个队列实现的复杂度如何呢? mirror操作的耗时与队列元素的数量成正比，但仅当leading列表为空时才发生。
   * 如果leading列表为非空的，就直接返回了。
   * 由于head和tail操作调用mirror操作，它们的复杂度也可能是队列长度的线性值。
   * 不过，随着队列变长，mirror操作被调用的频率也会变低。
   *
   * 的确，假设有一个长度为n的队列，其leading列表为空，那么mirror操作必须将一个长度为n的列表做一次反向复制。
   * 不过下一次mirror复制要做任何工作都需要等到leading列表再次变空时，这将发生在n次tail操作过后。
   * 这意味着你可以让这n次tail操作“分担”1/n的mirror操作的复杂度，也就是常量时间的工作。
   * 假设head、tail和enqueue操作差不多以相同频次出现，那么摊销后的 （amortized）复杂度对每个操作而言就是常量的了。
   * 因此，从渐进的视角看，函数式队列与可变队列同样高效。
   */

  /**
   * 不过，对于这个论点，我们要附加两点说明。
   * 首先，这里探讨的只是渐进行为，而常量因子可能会不一样。
   * 其次，这个论点基于head、tail和enqueue操作的调用频次差不多相同。
   * 如果head操作的调用比其他两个操作的调用频繁得多，这个论点就不成立，
   * 因为每次对head操作的调用都将涉及用mirror操作重新组织列表这个开销较高的操作。
   * 第二点可以被避免，我们可以设计出这样一个函数式队列:在连续的head操作中，只有第一次需要重组。
   * 你可以在本章末尾了解到这具体是如何实现的。
   */


}
