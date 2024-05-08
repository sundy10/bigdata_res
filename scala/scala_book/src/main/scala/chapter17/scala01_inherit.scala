package chapter17

import java.lang

object scala01_inherit extends App {

  /**
   * 第17章 Scala的继承关系
   *
   * 本章将从整体上介绍Scala的继承关系。
   * 在Scala中，每个类都继承自同一个名称为Any的超类。由于每个类都是Any类的子类，因此在Any类中定义的方法是全类型(universal）的:可以在任何对象上被调用。
   * Scala还在继承关系的底部定义了一些有趣的类，如Null和Nothing类，它们本质上是作为通用的子类存在的。
   * 例如，就像Any类是每一个其他类的超类那样，Nothing类是每一个其他类的子类。在本章中，我们将带你领略Scala的整个继承关系。
   */

  /**
   * 17.1 Scala的类继承关系
   *
   * 图 23.1.png 展示了Scala的类继承关系的轮廓。在类继承关系的顶部是Any类，定义了如下方法:
   *
   * final def ==(that: Any): Boolean
   * final def !=(that: Any): Boolean
   * def equals(taht: Any): Boolean
   * def ##: Int
   * def hashCode: Int
   * def toString: String
   */

  /**
   * 由于每个类都继承自Any类，因此Scala程序中的每个对象都可以用==、!=或equals方法做比较，用拼或hashCode方法做哈希，以及用toString方法做格式化。
   *
   * @相等和不相等方法（==和!=)在Any类中被声明为final的，所以它们不能被子类重写。
   *
   * "=="方法从本质上讲等同于equals方法，而 "!=" 方法一定是equals方法的反义。
   * 这样一来，子类可以通过重写equals方法来定制==或!=方法的含义。
   * @唯一一个==方法不等同于equals方法的场景是针对Java的数值类的，如Integer或Long类。
   * @在Java中,new lnteger(1)并不等同于(equal) new Long(1)，尽管对基本类型的值而言，1 == 1L。
   *             由于Scala与Java相比是一个更规则的语言，因此在实现时有必要将这些类的==方法做特殊处理，来解决这个差异。
   *             同理，## 方法提供了Scala版本的哈希算法，
   * @与Java的hashCode方法一样,除了一点:对包装的数值类型而言，它的行为与==方法是一致的。
   * 例如，new Integer(1)和new Long(1) 通过 ## 方法能获得相同的哈希值，尽管它们的Java版hashCode方法是不同的。
   */

  println("a".##)


  /**
   * @跨界相等性 (multiversal equality)
   *
   *        Scala 3引入了“跨界相等性”这个概念，针对那些可能会带来bug的==和!=方法给出编译错误，
   *        比如，对String和Int做相等性判断。我们将在第23章详细介绍这个机制。
   */

  /**
   * 根类Any有两个子类:AnyVal和AnyRef。
   *
   * @AnyVal类是Scala中所有值类的父类。虽然你可以定义自己的值类（参见11.4节)
   * 但Scala提供了9个内建的值类: Byte、Short、Char、Int、Long、Float、Double、Boolean和Unit。
   * @前8个值类对应Java的基本类型，它们的值在运行时是用Java的基本类型的值来表示的。这些类的实例在Scala中都被写作字面量。
   * 例如，42是Int类的实例，'x'是Char类的实例，而false是Boolean类的实例。不能用new来创建这些类的实例。
   * 这一点是通过将值类定义为抽象类的同时，由final的这个“小技巧”来完成的。
   *
   * 所以如果你尝试编写这样的代码:
   * new Int (编译失败)
   */

  /**
   * 另外的那个值类Unit可以粗略地对应到Java的void类型，用来作为那些不返回有趣结果的方法的结果类型。
   *
   * @Unit类有且只有一个实例值，写作()，正如我们在7.2节提到的那样。
   *
   * 我们在第5章曾经解释过，值类以方法的形式支持通常的算术和布尔操作符。例如，Int类拥有名称为+和*的方法，
   * 而Boolean类拥有名称为||和&&的方法。值类同样继承了Any类的所有方法。
   * @注意，值类空间是扁平的，所有值类都是scala.AnyVal类的子类，但它们相互之间并没有子类关系。
   * @不同的值类类型之间存在隐式的转换。例如，在需要时，scala.Int类的一个实例可以（通过隐式转换)被自动放宽成scala.Long的实例。
   *
   * 正如5.10节提到的，隐式转换还被用于给值类型添加更多功能。例如，Int类型支持所有下列操作:
   */
  42.max(43)
  42.min(43)
  1 until 5
  1 to 5
  3.abs
  -3.abs

  /**
   * 工作原理是这样的: min、max、until、to和abs方法都被定义在scala.runtime.RichInt类中，
   * 并且存在从Int类到RichInt类的隐式转换。只要对Int类调用的方法没有在lnt类中定义,而在RichInt类中定义了这样的方法，隐式转换就会被自动应用。
   * 其他值类也有类似的“助推类”和隐式转换。
   * (关于隐式转换的这种用法将在后续版本中被替换成扩展方法。我们将在第22章介绍扩展方法。)
   */

  /**
   * 根类Any的另一个子类是AnyRef类。这是Scala所有引用类的基类。
   * @前面提到过,在Java平台上,AnyRef类事实上只是java.lang.Object类的一个别名。
   * @也就是说，Java编写的类和Scala编写的类都继承自AnyRef类。因此，我们可以这样看待java.lang.Object类:它是AnyRef类在Java平台的实现。
   * 虽然你可以在面向Java平台的Scala程序中任意换用Object类和AnyRef类，但是推荐的风格是尽量都使用AnyRef类。
   */

}
