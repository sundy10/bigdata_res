package chapter17

object scala03_base_type extends App {

  /**
   * 17.3 底类型
   *
   * 在图17.1中的类继承关系的底部，你会看到两个类:
   * scala.Null和
   * scala.Nothing。
   *
   * @这些是Scala面向对象的类型系统用于统一处理某些“极端情况” (corner case)的特殊类型。
   * @Null是null引用的类型，是每个引用类（也就是每个继承自AnyRef类的类)的子类。
   * @Null并不兼容于值类型。比如，你并不能将null赋值给一个整数变量:
   * val i:Int = null (编译错误)
   * @Nothing位于Scala的类继承关系的底部，是每个其他类型的子类型。
   * @不过，并不存在这个类型的任何值。为什么需要这样一个没有值的类型呢?我们在7.4节曾讨论过，Nothing的用途之一是给出非正常终止的信号。
   *
   * 举例来说，Scala标准类库的Predef对象有一个error方法，其定义如下:
   *
   * def error(message: String): Nothing =
   * throw new RuntimeException(message)
   *
   * error方法的返回类型是Nothing，这告诉使用方该方法并不会正常返回(它会抛出异常)。
   * @由于Nothing是每个其他类型的子类型，可以以非常灵活的方式来使用error这样的方法。例如:
   */
  def divide(x: Int, y: Int): Int =
    if y != 0 then x / y
    else sys.error("can't divide by zero")

  /**
   * 这里x / y条件判断的“then”分支的类型为Int，而else分支（即调用error方法的部分)的类型为Nothing。
   * 由于Nothing是Int的子类型，因此整个条件判断表达式的类型就是Int，正如方法声明要求的那样。
   */
}
