package chapter17

import chapter11.scala03_stacked_trait.BasicIntQueue
import chapter11.scala03_stacked_trait.Incrementing
import chapter11.scala03_stacked_trait.Filtering

object scala05_intersection_type extends App {

  /**
   *  17.5 交集类型
   *
   *  你可以用 & 将两个或更多的类型连在一起以构成交集类型( intersection type)，
   *  如Incrementing & Filtering。这里有一个使用示例11.5、示例11.6和示例11.9中的类与特质的例子:
   */

  val q:BasicIntQueue & Incrementing & Filtering = new BasicIntQueue with Incrementing with Filtering

  /**
   * @这里的q被初始化成一个匿名类的实例，这个匿名类扩展自BasicIntQueue类且依次混入了Incrementing和Filtering特质。
   * @编译器推断的类型，即BasicIntQueue&Incrementing&Filtering是一个交集类型，表示q引用的这个对象是所有3个类型(BasicIntQueue、lncrementing和Filtering）的实例。
   */

  /**
   * @交集类型是所有其构成类型的排列组合的子类型。 (声明超类 值子类)
   * 例如，类型 B & I & F 是类型B、I、F、B&I、B&F、I&F的子类型，
   *
   * 并且根据自反律（reflexivity)，也是B&I&F自己的子类型。
   * 不仅如此，由于交集类型是满足交换律（commutativity)的，交集类型中出现的类型顺序对结果没有影响。
   * 比如，类型I&F和类型F&I是等效的。因此，类型B&I&F也是类型I&B、F&B、F&I、B&F&I、F&B&I等的子类型。这里有一个展示交集类型之间关系的例子:
   */
  //可以编译，因为 B&I&F <: I&F
  val q2:Incrementing & Filtering =q
  //可以编译，因为 I&F 和F&I 是等价的
  val q3:Filtering & Incrementing =q2




}
