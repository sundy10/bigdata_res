package chapter20

object scala01_abstract_member extends App {

  /**
   * 第20章 抽象成员
   *
   * @如果类或特质的某个成员在当前类中没有完整的定义，它就是抽象(abstract)的。
   * @抽象成员的本意是为了让声明该成员的类的子类来实现。在很多面向对象的语言中都能找到这个理念。
   * 例如，Java允许声明抽象方法，Scala也允许声明这样的方法，像10.2节中介绍的那样。
   * @不过Scala走得更远，将这个概念完全泛化了:除了抽象方法，Scala也允许声明抽象字段甚至抽象类型作为类和特质的成员。
   */

  /**
   * @本章将描述所有4种抽象成员:抽象类型、抽象方法、抽象的val和抽象的var。在这个过程中，我们还将探讨特质参数化字段、惰性的val及路径依赖类型等。
   */

  /**
   * 20.1 抽象成员概览
   *
   * 下面这个特质声明了4种抽象成员，
   *
   * @即一个抽象类型(T)、
   * @一个抽象方法(transform) 、
   * @一个抽象的val(initial)
   * @和一个抽象的var(current):
   */
  trait Abstract:
    type T

    def transform(x: T): T

    val initial: T
    var current: T

  /**
   * Abstract特质的具体实现需要填充每个抽象成员的定义。下面是一个提供了这些定义的例子:
   */
  class Concrete extends Abstract :
    type T = String

    override def transform(x: String): String = x + x

    override val initial: String = "hi"

    var current: String = initial

  /**
   * 这个实现通过定义T为String类型的别名的方式给出了类型名T的具体含义。transform操作将给定的字符串与自己拼接，而initial和current的值都被设置为"hi"。
   *
   * 这个例子应该能让你了解一个粗略的关于Scala里都有什么样的抽象成员的概念。本章剩余的部分将呈现细节并解释这些新的抽象成员的形式，以及一般意义上的类型成员都有哪些好处。
   */

}
