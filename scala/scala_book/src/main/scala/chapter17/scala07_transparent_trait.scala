package chapter17


import scala.collection.mutable.ArrayBuffer

object scala07_transparent_trait extends App {

  abstract class IntQueue {
    def get(): Int

    def put(x: Int): Unit
  }

  class BasicIntQueue extends IntQueue {
    private val buf = ArrayBuffer.empty[Int]

    override def get(): Int = buf.remove(0)

    override def put(x: Int): Unit =
      println(s"--$x")
      buf += x
  }

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

    abstract class IntQueue {
      def get(): Int

      def put(x: Int): Unit
    }


  /**
   * 17.7 透明特质
   *
   * @特质有两个主要用途:
   * @可以通过混入来定义类，
   * @以及定义类型本身。
   *
   * 有时候，你可能想让某个特质主要用于混入，而不是作为（独立存在的)类型。
   * 例如，你可以说，11.3节的Incrementing和Filtering这两个特质在混入的场合很有用，但作为类型价值有限。
   * 在默认情况下，这两个特质定义的类型可以被推断出来。
   * 例如，在下面的语句中，Scala编译器会把q的类型推断为Incrementing和Filtering特质的交集类型:
   */
  val q: BasicIntQueue & Incrementing & Filtering = new BasicIntQueue with Incrementing with Filtering

  /**
   * @你可以使用transparent修饰符来声明，避免让特质的名称出现在被推断的类型信息中。
   * @例如，可以像这样将Incrementing和Filtering特质声明为透明的:
   */
  transparent trait Incrementing1 extends IntQueue :
    abstract override def put(x: Int): Unit =
      if x >= 0 then super.put(x)

  transparent trait Filtering1 extends IntQueue :
    abstract override def put(x: Int): Unit =
      if x >= 0 then super.put(x)

  /**
   * 这样一来，Incrementing和Filtering特质就被定义为透明的，它们的名称就不会再出现在被推断的类型中。
   * 例如，根据之前展示过的同样的实例创建表达式推断的类型不再涉及Incrementing和Filtering特质:
   */
  val q1: BasicIntQueue = new BasicIntQueue with Incrementing1 with Filtering1

  /**
   * @transparent修饰符只会影响类型推断。你仍然可以把透明特质当作类型来使用，只要显式地将其写出来即可。
   * 下面是一个将Incrementing和Filtering这两个透明特质显式地在变量q的类型注解中给出的例子:
   */
  val q2 :BasicIntQueue & Incrementing1 & Filtering1 = new BasicIntQueue with Incrementing1 with Filtering1

  /**
   * 除了那些被显式标记为透明的特质，Scala 3还会将
   *    scala.Product、
   *    java.lang. Serializable
   *    和 java.lang.Comparable
   *    当作透明的来处理。由于这些类型在Scala 3中永远不会被自动推断出来，
   *    @因此如果你确实想以类型的方式来使用它们，则必须通过显式类型注解或归因(ascription）来实现。
   */

  /**
   * 17.8 结语
   *
   * 本章向你展示了Scala的类继承关系中位于顶部和底部的类。
   * 首先，你了解了 如何创建自己的值类，以及如何用它来表示“小类型”;
   * 然后，你了解了 交集类型和并集类型，以及它们如何让Scala的类继承关系满足 格的（数学）属性;
   * 最后，你还了解了如何使用transparent修饰符来防止Scala的类型推断算法将那些主要用于混入的特质推断成类型。
   * 在下一章，你将了解到类型参数化的相关内容。
   */
}
