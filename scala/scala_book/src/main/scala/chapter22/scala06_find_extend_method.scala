package chapter22

object scala06_find_extend_method extends App {

  /**
   * 22.6 Scala 如何查找扩展方法
   *
   * 当编译器看到你在尝试对一个对象引用调用一个方法时，它首先会检查这个方法是不是在该对象的类本身有定义。
   * 如果有定义，就直接选中这个方法，不再继续查找扩展方法。
   * 如果没有定义，这个方法调用就是一个候选的编译错误。(这是条通用规则:如果任何代码片段能够按照原样通过编译，则Scala编译器不会将它改写成其他模样)
   * 不过，在报告编译错误之前，编译器会查找能解决这个候选的编译错误的扩展方法或隐式转换。
   * @只有当未找到可以用来解决这个候选的编译错误的扩展方法或隐式转换时，编译器才会将编译错误报告出来。
   */

  /**
   * Scala将扩展方法的查找分为两个阶段。
   *    在第一个阶段，编译器会在语法作用域内查找。
   *    在第二个阶段，它会搜索3个地方:那些在作用域内的上下文参数的成员，接收者的类、超类、超特质的伴生对象，以及这些伴生对象中的上下文参数的成员。
   *    作为第二个阶段的一部分，编译器还会尝试对接收者的类型进行类型转换。
   */

  /**
   * 如果编译器在任一阶段找到多个可被应用的扩展方法，则它将选中更具体的那一个，就像编译器在多个可选的重载方法中挑选最具体的那一个一样。
   * 如果它找到的最具体的可选扩展方法不止一个，它就会给出一个编译器报错，这个报错会包含那些存在歧义的扩展方法的清单。
   */

  /**
   * 一个定义可以 以3种方式出现在语法作用域内:它可以被直接定义、被引入或者被继承。
   * 举例来说，下面对88调用absOption方法的例子之所以能通过编译，是因为在使用前，absOption这个扩展方法就已经以单个标识符的形式被引入了:
   *
   * import TwosComplementOps.absOption
   *  88.absOption    //Some(88)
   *
   * 因此，对absOption这个扩展方法的查找在第一个阶段就完成了。
   * 而示例22.12中由 <= 触发的(扩展方法)查找是在第二个阶段完成的。
   * 被应用的扩展方法是示例22.11中给出的<=。它在通过using参数传入的Ord[T]上下文参数实例中被调用。
   */

  /**
   * 22.7 结语
   *
   * 扩展方法是对代码播撒语法糖的一种方式:它能让某个对象调用看起来就像是在那个对象的类中定义的函数一样，
   * 但实际上是将这个对象传入函数的（而不是反过来)。
   *
   * 本章介绍了如何定义扩展方法，以及如何使用其他人定义的扩展方法。我们还看到了扩展方法和类型族是如何相互补充的，
   * 以及如何更好地一起使用它们。在下一章，我们将更深入地介绍类型族。
   */


}
