package chapter18

object scala05_lowerbound extends App {

//  class Queue[T](private val leading: List[T],
//                 private val trailing: List[T]):
//    private def mirror =
//      if leading.isEmpty then
//        new Queue(trailing.reverse, Nil)
//      else this
//
//    def head = mirror.leading.head
//
//    def tail =
//      val q = mirror
//      new Queue(q.leading.tail, q.trailing)
//
//    def enqueue(x: T) =
//      new Queue(leading, x :: trailing)

  /**
   * 18.5 下界
   *
   * 回到Queue类。你看到了，之前示例18.4中的Queue[T]定义不能以T协变，因为T作为enqueue方法的参数类型出现，而这是一个逆变点。
   *
   * 幸运的是，有一个办法可以解决:可以通过多态让 enqueue方法泛化(即给enqueue方法本身一个类型参数）并对其类型参数使用下界( lower bound)。
   * 示例18.6给出了实现这个想法的新Queue类的定义。
   */
  //带有下界的类型参数
  class Queue[+T](private val leading:List[T],private val trailing:List[T]):
    def enqueue[U>:T](x:U) =
      new Queue[U](leading,x::trailing)

  /**
   * 新的定义给enqueue方法添加了一个类型参数U，并用“U >: T”这样的语法定义了U的下界为T。
   * 这样一来，U必须是T的超类型。Renqueue方法的参数类型现在为U而不是T，方法的返回值现在是Queue[U]而不是Queue[T]。
   * 举例来说，假设有一个Fruit类，以及两个子类Apple和Orange.按照Queue类的新定义，可以对Queue[Apple]追加一个Orange，其结果是一个Queue[Fruit]。
   * (超类型和子类型的关系是满足自反律的，意思是一个类型同时是自己的超类型和子类型。尽管T是U的下界,你仍然可以将一个T传入enqueue方法中。)
   *
   * 修改过后的enqueue方法定义是类型正确的。
   * 直观地讲，如果T是一个比预期更具体的类型（例如，相对Fruit而言的Apple)，则对enqueue方法的调用依然可行，因为U(Fruit)仍是T (Apple）的超类型。
   *  @(从技术上讲，这里发生的情况是对下界而言的，协变点和逆变点发生了翻转。类型参数U出现在逆变点（1次翻转)，而下界(>:T)是一个协变点(2次翻转)。)
   *
   * enqueue方法的新定义显然比原先的定义更好，因为它更通用。
   * 不同于原先的定义，新的定义允许我们追加任意的队列类型T的超类型u的元素，并得到Queue[U]。
   * 通过这一点加上队列的协变，我们获得了一种很自然的方式来对不同元素类型的队列进行灵活建模。
   *
   * 这显示出型变注解和下标配合得很好。它们是类型驱动设计(type-driven design)的绝佳例子。
   * 在类型驱动设计中，接口的类型可以引导我们做出细节的设计和实现。
   * 在队列这个例子中，你可能一开始并不会想到用下界来优化enqueue方法的实现。
   * 不过你可能已经决定让队列支持协变，在这种情况下，编译器会指出enqueue方法的型变错误。
   * 通过添加下界来修复这个型变错误让enqueue方法更加通用，也让整个队列变得更加好用。
   *
   * @这也是Scala倾向于声明点（declaration-site）型变而不是使用点(use-site）型变的主要原因，而Java的通配处理采用的是后者。
   * @如果采用使用点型变，则我们在设计类的时候只能靠自己。最终由类的使用方来通配，而如果他们弄错了，一些重要的实例方法就不再可用了。
   * @型变是一个很难处理好的事情，用户经常会弄错，然后得出通配和泛型过于复杂的结论。
   * @而如果采用定义点（definition-site)型变(与声明点型变一样)，就可以向编译器表达你的意图，由编译器复核那些你想要可用的方法是否真的可用。
   */


}
