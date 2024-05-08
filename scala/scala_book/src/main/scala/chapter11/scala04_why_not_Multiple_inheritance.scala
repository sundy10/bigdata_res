package chapter11

import chapter11.scala03_stacked_trait.{BasicIntQueue, Doubling, Incrementing}

object scala04_why_not_Multiple_inheritance extends App {

  /**
   * 11.4 为什么不用多继承
   *
   * @特质是一种从多个像类一样的结构中继承的方式，不过与许多其他语言中的多重继承有着重大的区别。
   * @其中一个区别尤为重要:对super的解读。在多重继承中，super调用的方法在调用发生的地方就已经确定了。
   * @而特质中的super调用的方法取决于类和混入该类的特质的线性化（linearization)。正是这个区别让前一节介绍的可叠加修改成为可能。
   */

  /**
   * 在深入探究线性化之前，我们花一些时间来考虑一下传统多重继承的语言中要如何实现可叠加修改。
   * 假设有下面这段代码，不过这一次按照多重继承来解读，而不是特质混入:
   *
   * val q = new BasicIntQueue with Incrementing with Doubling
   * q.put(42) //  应该调用哪一个put方法
   *
   * 第一个问题是:这次调用执行的是哪一个put方法?也许规则是最后一个超类胜出，那么在本例中Doubling特质的put方法会被执行。
   * 于是Doubling特质会对其参数翻倍，然后调用super.put，就结束了，不会有加1发生。
   * 同理，如果规则是首个超类胜出，则结果的队列将对整数加1，但不会翻倍。
   * 这样一来，没有一种顺序是可行的。
   *
   * 也许还可以尝试这样─种可能:让程序员自己指定调用super时到底使用哪一个超类的方法。
   * 例如，假设有下面这段代码，在这段代码中，super看上去显式地调用了Incrementing和Doubling特质:
   */
  //多重继承思维实验
  trait MyQueue1 extends BasicIntQueue, Incrementing, Doubling :
    override def put(x: Int): Unit =
      super[Incrementing].put(x)
      super[Doubling].put(x)        //这是合法的Scala代码 但很少有人这么用

  /**
   * 如果这就是Scala提供的唯一方案，则它将带来新的问题（相比这些问题，代码啰唆点根本不算什么)。
   * 这样做可能发生的情况是基类的put方法被调用了“两次”:一次在加1的时候，另一次在翻倍的时候，不过两次都不是用加1或翻倍后的值调用的。
   */

  /**
   * @简单来说，多重继承对这类问题并没有好的解决方案。
   * 你需要回过头来重新设计，重新组织你的代码。
   * 相比较而言，使用Scala特质的解决方案是很直截了当的。
   * 你只需要简单地混入Incrementing和Doubling特质即可，因为Scala对特质中super的特殊处理完全达到了预期的效果。
   *
   * 这种方案与传统的多重继承相比，很显然有某个区别，但是这个区别究竟是什么呢?
   */

  /**
   * @前面我们提示过了，答案是线性化。
   * 当你用new关键字实例化一个类的时候，Scala会将类及它所有继承的类和特质都拿出来，将它们“线性”地排列在一起。
   * 然后，当你在某一个类中调用super时，被调用的方法是这个链条中向上最近的那一个。
   * @如果除了最后一个方法，所有的方法都调用了super，最终的结果就是叠加在一起的行为(?)
   */

  /**
   * 与Java默认方法的比较
   *
   * 从Java 8开始，可以在接口中包含默认方法。虽然这些方法看上去与Scala特质中的具体方法很像，但是它们是非常不同的，因为Java并不会执行线性化。
   * 由于（Java）接口不能声明字段，也不能从Object类之外的超类继承，因此默认方法只能通过子类实现的接口方法来访问对象状态。
   * 而Scala特质中的具体方法则可以通过特质中声明的字段（或者通过super访问超特质类或超类中的字段)来访问对象状态。
   * @不仅如此，如果你的Java类同时继承了来自不同超接口中签名相同的默认方法，则Java编译器会要求你自己实现这个方法。在你的实现中，可以通过在super前给出接口名的方式来调用其中一个或两个实现，如“Doubling.super.put(x)”。
   * 而Scala则让你的类可以继承线性化关系中距离最近的那个实现。
   */

  /**
   * 与Scala允许实现可叠加修改的行为的目的不同，Java的默认方法的设计目标是允许类库设计者对已存在的接口添加方法。
   * 在Java 8之前，这并不实际，因为这样做会打破任何实现了相关接口的类的二进制兼容性。
   * 但是目前，Java已经允许我们在类自己未提供实现的前提下使用默认的实现，无论该类是否在新方法被添加到接口之后被重新编译过。
   */

  /**
   * 线性化的确切顺序在语言规格说明书中有描述。这个描述有些复杂，
   * @不过你需要知道的要点是，在任何线性化中，类总是位于所有它的超类和混入的特质之前。
   * 因此，当你写下调用super的方法时，那个方法绝对是用于修改超类和混入特质的行为的，而不是反过来。
   */


  /***
   * @注意
   *
   * 本节剩下的部分将描述线性化的细节。如果你目前不急于理解这些细节，则可以安心地过。
   *
   * Scala线性化的主要属性可以用下面的例子来说明:假设你有一个Cat类，
   * 这个类继承自超类Animal和两个超特质Furry和口FourLegged ,而FourLegged特质又扩展自另一个特质HasLegs。
   *
   * Cat类的继承关系和线性化如图11.1所示。
   * 继承是用传统的UML表示法标记的:白色、空心的三角箭头表示继承，其中箭头指向的是超类型;
   * 黑色、实心的非三角箭头表示线性化，其中箭头指向的是super调用的解析方向。
   * {@linkplain  scala04_z1.png}
   */
  class Animal
  trait Furry extends Animal
  trait HasLegs extends Animal
  trait FourLegged extends HasLegs
  class Cat extends Animal,Furry,FourLegged



  /**
   * Cat类的线性化从后到前的计算过程如下。Cat类的线性化的最后一个部分是其超类Animal的线性化。
   * 这段线性化被直接不加修改地复制过来。(这些类型的线性化如表11.1所示。）
   * 由于Animal类并不显式地扩展某个超类，也没有混入任何超特质，它默认扩展自AnyRef类，
   * 而AnyRef类扩展自Any类。这样一来，Animal类的线性化看上去就是这样的:
   * {@linkplain  scala04_z2.png}
   *
   * 线性化的倒数第二个部分是首个混入（即Furry特质)的线性化，
   * 不过所有已经出现在Animal类的线性化中的类都不再重复出现，每个类在Cat类的线性化中只出现一次。结果是:
   *  {@linkplain  scala04_z3.png}
   *
   *
   *  在这个结果之前，是FourLegged类的线性化，同样地，任何已经在超类或首个混入中复制过的类都不再重复出现:
   *  {@linkplain  scala04_z4.png}
   *
   *  最后，Cat获的线性化中的第一个类是Cat自己:
   *  {@linkplain  scala04_z5.png}
   */


  /**
   * 当这些类和特质中的任何一个通过super调用某个方法时，被调用的是在线性化链条中出现在其右侧的首个实现。
   *
   */




}
