package chapter18

object scala02_information_hiding extends App {

  /**
   * 18.2 信息隐藏
   *
   * 示例18.1给出的Queue实现在效率方面来说已经非常棒了。你可能会表示反对，因为为了达到这个效率，我们暴露了不必要的实现细节。
   * 全局可访问的Queue构造方法接收两个列表作为参数，其中一个顺序还是反的，因此很难说这是一个直观的对队列的表示。
   * 我们需要对使用方代码隐藏这个构造方法。本节将展示在Scala中完成这个动作的几种方式。
   */

  /**
   * @1.私有构造方法和工厂方法
   *
   * 在Java中，可以通过标记private来隐藏构造方法。在Scala中，主构造方法并没有显式定义，它是通过类参数和类定义体隐式定义的。
   * 尽管如此，我们还是可以通过在参数列表前加上private修饰符来隐藏主构造方法，如示例18.2所示。
   * @类名和参数之间的private修饰符表示Queue的构造方法是私有的:它只能从类本身及其伴生对象访问。类名Queue依然是公有的，因此可以把它当作类型来使用，但不能调用其构造方法:
   * 既然Queue类的主构造方法不能再通过使用方代码调用，那么我们需要采用其他的方式来创建新的队列。一种可能的方式是添加一个辅助构造方法，就像这样:
   */
  //通过标记private来隐藏主构造方法
  //  class Queue[T] private (private val leading:List[T],
  //                         private val trailing:List[T]):
  //    def this() = this(Nil,Nil)
  //    def this(elems:T*)=this(elems.toList,Nil)

  /**
   * 回忆一下，T*用来表示重复的参数（参考8.8节)。
   *
   * 另一种可能是添加一个工厂方法以通过这样一组初始元素来构建队列。一种不错的实现方式是定义一个与Queue类同名的对象，并提供一个apply方法，如示例18.3所示。
   */
  //  object Queue:
  //    // 用初始的元素xs构造队列
  //    def apply[T](xs:T*) =new Queue[T](xs.toList,Nil)

  /**
   * @通过将这个对象与Queue类放在同一个源文件中，我们让对象成了Queue类的伴生对象。
   * 在12.5节中你曾看到过，伴生对象拥有与对应伴生类相同的访问权限。因此，Queue对象的apply方法可以创建一个新的Queue，尽管Queue类的构造方法是私有的。
   * 注意，由于这个工厂方法的名称是apply，因此使用方代码可以使用诸如Queue(1,2,3)这样的表达式来创建队列。
   * 这个表达式可以展开成Queue.apply(1,2,3)，因为Queue是对象而不是函数。这样一来，在使用方看来，Queue就像是全局定义的工厂方法一样。
   * 实际上，Scala并没有全局可见的方法，每个方法都必须被包含在某个对象或某个类中。
   * 不过，在全局对象中使用名称为apply的方法，可以支持看上去像是全局方法的使用模式。
   */


  /**
   * @2.备选方案:私有类
   *
   * 私有构造方法和私有成员只是隐藏类的初始化和内部表现形式的一种方式。另一种更激进的方式是隐藏类本身，并且只暴露一个反映类的公有接口的特质。
   * 示例18.4的代码实现了这样一种设计。其中定义了一个Queue特质，声明了方法head、tail和enqueue。
   * 所有这3个方法都实现在子类Queuelmpl中，这个子类本身是对象Queue的一个私有的内部类。
   * 这种做法暴露给使用方的信息与之前一样，不过采用了不同的技巧。与之前逐个隐藏构造方法和成员不同，这个版本隐藏了整个实现类。
   */
  //函娄数宝式弋队列的类型抽象
  trait Queue[T]:
    def head: T

    def tail: Queue[T]

    def enqueue(x: T): Queue[T]

  object Queue:
    def apply[T](xs: T*): Queue[T] = QueueImpl[T](xs.toList, Nil)

    private class QueueImpl[T](private val leading: List[T], private val trailing: List[T]) extends Queue[T] :
      def mirror =
        if leading.isEmpty then
          QueueImpl(trailing.reverse, Nil)
        else this

      override def head: T = mirror.leading.head

      override def tail: Queue[T] =
        val q = mirror
        QueueImpl(q.leading.tail, q.trailing)

      override def enqueue(x: T): Queue[T] = QueueImpl(leading, x :: trailing)


}
