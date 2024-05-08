package chapter10

object scala05_override_field extends App {

  abstract class Element {
    def contents: Vector[String]

    def height: Int = contents.length

    def width: Int = if height == 0 then 0 else contents(0).length
  }

  /**
   * 10.5 重写方法和字段
   *
   * 统一访问原则只是Scala比Java在处理字段和方法上更加统一的一个区别。
   * 另一个区别是，Scala中字段和方法属于同一个命名空间。
   * @这使得用字段重写无参方法变为可能。
   * 举例来说，可以将VectorElement类中的contents实现从方法改成字段，这并不需要修改Element类中的contents定义，如示例10.4所示。
   */

  class VectorElement(conts: Vector[String]) extends Element {
    val contents: Vector[String] = conts
  }

  /**
   * 这个版本的VectorElement类中的contents字段（用val定义)是Element类的contents方法（用def定义)的一个没有问题的实现。
   * 另一方面，Scala禁止在同一个类中使用相同的名称命名字段和方法，但在Java中这是被允许的。
   */

  /**
   * 一般来说，Scala只有两个命名空间用于定义，不同于Java的4个。Java的4个命名空间分别是:字段、方法、类型和包。
   * 而Scala的两个命名空间分别是:
   *      @值（字段、方法、包和单例对象)
   *      @类型（类和特质名)
   *
   *  @Scala将字段和方法放在同一个命名空间的原因是为了让你可以用val来重写一个无参方法，这在Java中是不被允许的。
   *
   *  在Scala中，包也与字段和方法共用一个命名空间的原因是让你能引入包(而不仅仅是类型的名称)和单例对象的字段及方法。这同样是Java不允许的。我们将在12.3节做更详细的介绍
   */

}
