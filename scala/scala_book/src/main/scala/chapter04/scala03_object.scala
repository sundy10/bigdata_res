package chapter04

object scala03_object extends App {

  /**
   * 正如第1章提到的,Scala比Java更面向对象的一点是，Scala的类不允许有静态( static）成员。对于此类使用场景，Scala提供了单例对象。
   * 单例对象的定义看上去与类定义很像，只不过class关键字被替换成了object关键字。
   *
   * 类和单例对象的一个区别是单例对象不接收参数,而类可以。
   * 由于无法用new实例化单例对象，也就没有任何手段来向它传参。
   * 每个单例对象都是通过一个静态变量引用合成类(synthetic class)的实例来实现的，
   * 因此单例对象在初始化的语义上与Java的静态成员是一致的。尤其体现在，单例对象在有代码首次访问时才会被初始化。
   *
   * 合成类的名称是对象名加上—个美元符号。因此,名称为ChecksumAccumulator的单例对象的合成类名是ChecksumAccumulator$。
   */
  
  /**
   *  不与某个伴生类共用同一个名称的单例对象叫作独立对象( standalone object)。
   *  
   *  独立对象有很多用途，
   *    包括收集相关的工具方法，
   *    或者定义Scala应用程序的入口，等等。
   */

}
