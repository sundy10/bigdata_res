package chapter11

import scala.collection.mutable.ArrayBuffer

object scala03_stacked_trait extends App {

  /**
   * 11.3 作为可叠加修改的特质
   *
   * 现在你已经了解了特质的一个主要用途:将瘦接口转换成富接口。
   * 现在我们将转向其另一个主要用途:为类是供可叠加的修改。特质允许你修改类的方法，而这些方法的实现方式允许你将这些修改叠加起来。
   */

  /**
   * 考虑这样一个例子，对某个整数队列叠加修改。
   * 这个队列有两个操作: put，将整数放入队列;
   * get，将整数取出来。队列遵循先进先出原则，所以get应该按照整数被放入队列的顺序返回这些整数。
   */

  /**
   * 给定一个实现了这样一个队列的类，可以通过定义特质来执行如下这些修改。
   *
   * - Doubling:将所有被放入队列的整数翻倍。
   * - Incrementing:将所有被放入队列的整数加1.
   * - Filtering:从队列中去除负整数。
   *
   * 这3个特质代表了修改（(modification)，因为它们可以修改下面的队列类，而不是自己定义完整的队列类。
   * 这3个特质也是可叠加的(stackable)。
   * 你可以从这3个特质中任意选择，将它们混入类，并得到一个带上了你选择的修改的新的类。
   */

  /**
   * 示例11.5给出了一个抽象的IntQueue类。IntQueue类有一个put方法用于将新的整数加入队列，
   * 以及一个get方法用于从队列中去除并返回整数。示例11.6给出了使用ArrayBuffer的IntQueue类的基本实现。
   *
   */
  abstract class IntQueue {
    def get(): Int

    def put(x: Int): Unit
  }

  import scala.collection.mutable.ArrayBuffer

  class BasicIntQueue extends IntQueue {
    private val buf = ArrayBuffer.empty[Int]

    override def get(): Int = buf.remove(0)

    override def put(x: Int): Unit =
      println(s"--$x")
      buf += x
  }

  /**
   * BasiclntQueue类用一个私有字段持有数组缓冲(array buffer)。
   * get方法用于从缓冲的一端移除元素，而put方法用于向缓冲的另一端添加元素。这个实现使用起来是这样的:
   */
  val queue = new BasicIntQueue
  queue.put(10)
  queue.put(20)
  println(queue.get()) //10
  println(queue.get()) //20

  /**
   * 到目前为止很不错。现在我们来看看如何用特质修改这个行为。
   * 示例11.7给出了在放入队列时对整数翻倍的特质Doubling。
   * Doubling特质有两个有趣的地方。
   *
   * @首先它声明了一个超类IntQueue。
   * @这个声明意味着这个特质只能被混入同样继承自IntQueue类的类。因此，可以将Doubling特质混入BasicIntQueue类，但不能将它混入Rational类。
   */

  trait Doubling extends IntQueue {
    abstract override def put(x: Int): Unit = super.put(2 * x)
  }

  /**
   * 第二个有趣的地方是该特质在一个声明为抽象的方法中做了一个super调用。
   * 对普通的类而言，这样的调用是非法的，因为在运行时必定会失败。
   *
   * @不过对于特质来说，这样的调用实际上可以成功。由于特质中的super调用是动态绑定的，只要在给出了该方法具体定义的特质或类之后混入，Doubling特质中的super调用就可以正常工作。
   */

  /**
   * 对于实现可叠加修改的特质，这样的安排通常是需要的。
   * 为了告诉编译器你是特意这样做的，必须将这样的方法标记为abstract override。
   * 这样的修饰符组合只允许用在特质的成员上，不允许用在类的成员上，它的含义是该特质必须混入某个拥有该方法具体定义的类中。
   *
   * 这个特质用起来是这样的:
   */
  class MyQueue extends BasicIntQueue, Doubling

  val myQueue = new MyQueue
  myQueue.put(10)
  println(myQueue.get()) //20

  /**
   * 在这个编译器会话的第一行，定义了MyQueue类，该类扩展自BasiclntQueue类，并混入了Doubling特质。
   * 接下来放入一个10，不过由于Doubling特质的混入，这个10会被翻倍。当我们从队列中获取整数时，得到的将是20。
   *
   * 注意，MyQueue类并没有定义新的代码。
   * 它只是简单地给出一个类然后混入一个特质。
   * 在这种情况下，可以在用new关键字实例化的时候直接给出“BasicIntQueue with Doubling”，而不是定义一个有名称的类，
   * 如示例11.8所示。(要向匿名类混入特质，必须使用with，不能使用逗号。)
   */
  //示例11.8在用new关键字实例化时混入特质
  val withQueue = new BasicIntQueue with Doubling
  withQueue.put(10)
  println(withQueue.get()) //20

  /**
   * 为了弄清楚如何叠加修改，我们需要定义另外两个修改特质，即Incrementing和口Filtering。示例11.9给出了这两个特质的实现代码。
   */
  trait Incrementing extends IntQueue :
    abstract override def put(x: Int): Unit = {
      println("Incrementing")
      super.put(x + 1)
    }

  trait Filtering extends IntQueue :
    abstract override def put(x: Int): Unit = if x >= 0 then {
      println("Filtering")
      super.put(x)
    }

  /**
   * 有了这些修改特质，就可以为特定的队列挑选想要的修改。举例来说，下面是一个既过滤掉负整数又对所有数字加1的队列:
   */
  val queue1 = new BasicIntQueue with Incrementing with Filtering
  queue1.put(-1)
  queue1.put(0)
  println("---")
  queue1.put(1)
  println(queue1.get())//1
  println(queue1.get())//2

  /**
   * 混入特质的顺序是重要的。(—旦特质被混入类,就可以将其称为混入(mixin) ) 确切的规则会在下一节给出。
   * @粗略地讲，越靠右出现的特质越先起作用。当你调用某个带有混入的类的方法时，最靠右的特质中的方法最先被调用。
   * @如果那个方法调用super，则它将调用左侧紧挨着它的那个特质的方法，以此类推。
   * 在示例11.9中，Filtering特质的put方法最先被调用，所以它首先过滤掉了那些负整数。
   * Incrementing特质的put方法排在第二位，因此它做的事情就是在Filtering特质的基础上对剩下的整数加1。
   *
   * 如果将顺序反过来，则结果是首先对整数加1，然后剔除负整数:
   */
  val queue2 = new BasicIntQueue with Filtering with Incrementing
  queue2.put(-1)
  queue2.put(0)
  queue2.put(1)
  println(queue2.get())
  println(queue2.get())
  println(queue2.get())

  /**
   * @总体而言，以这种风格编写的代码能带来相当大的灵活度。
   * @可以通过按不同的组合和顺序混入这3个特质来定义出16种不同的类。
   * 对于这么少的代码来说，灵活度是相当高的，因此你需要随时留意这样的机会，将代码按照可叠加的修改进行组织。
   */
}
