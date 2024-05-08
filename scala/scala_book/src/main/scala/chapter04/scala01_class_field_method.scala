package chapter04

object scala01_class_field_method extends App {

  /**
   * 类是对象的蓝本(blueprint)。一旦定义好一个类,就可以用new关键字根据这个类蓝本创建对象。
   * 在类定义中，需要填入字段（field）和法（method)，这些被统称为成员（member)。通过val或var定义的字段是指向对象的变量,通过def定义的方法则包含了可执行的代码。
   * 字段保留了对象的状态，或者说数据，而方法用这些数据来对对象执行计算。如果你实例化一个类，则运行时会指派一些内存来保存对象的状态图（即它的变量的内容)。
   */
  class ChecksumAccumulator {
    private var sum = 0 //私有字段之恶能被定义在同一个类中的方法访问

    def add(b: Byte): Unit =
      //b = 1  传递给方法的任何参数都只能在方法内部使用。Scala方法参数的一个重要特征是它们都是val而不是var
      sum += b

    //这里的checksum方法用到了两个位运算操作符:～是位补码(bitwise complement) ,&是按位与(bitwise and)。
    def checksum(): Int = ~(sum & 0xFF) + 1
  }

  val acc = new ChecksumAccumulator
  val csa = new ChecksumAccumulator


}
