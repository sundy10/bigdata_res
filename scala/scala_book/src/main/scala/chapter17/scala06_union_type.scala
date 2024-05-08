package chapter17

import javafx.scene.input.DataFormat

object scala06_union_type extends App {

  /**
   * 17.6 并集类型
   *
   * Scala提供了与交集类型相对应的并集类型（union type)。
   * 并集类型由两个或多个通过管道符号（|)连在一起的类型构成，如Pluml Apricot。(你可以这样念Plum | Apricot:“Plum或Apncot)
   * 并集类型表示某个对象至少是构成并集类型的其中一个类型的实例。举例来说，类型为Plum | Apricot的对象要么是Plum的实例，
   * 要么是Apricot的实例，要么同时是Plum和Apricot的实例。
   *
   * --我们能找到好几种李子(plum)和杏(apricot）的杂交水果,如杏李(apriplum)、杏李(aprium) .李杏(plumcot)和李杏(pluots)
   */

  /**
   * 与交集类型一样，并集类型也满足交换律: Plum | Apricot 与 pricot | Plum等效。与交集类型相对应，
   *
   * @并集类型是其构成类型的所有排列组合的超类型。
   * 例如，Plum | Apricot是Plum 和 Apricot的超类型。
   * 重要的是，Plum | Apricot不仅是 Plum和Apricot 的超类型，也是离它们最近的公共超类型，又称最小上界（least upper bound)。
   */

  /**
   * Scala 3新增的并集类型和交集类型可以确保Scala的类型系统满足数学意义上的格(lattice)。
   * 格是一个偏序集( partial order)，任意两个类型都有唯一的最小上界和唯一的最大下界( greatest lowerbound)。
   * 在Scala 3中，任意两个类型的最小上界是它们的并集，而最大下界是它们的交集。
   * 举例来说，Plum和Apricot的最小上界是Plum | Apricot，而它们的最大下界是Plum & Apricot。
   */

  /**
   * @并集类型在Scala的类型推断和类型检查的实现方面有着重要的意义。
   * @在Scala2中，类型推断算法必须对某些类型对的最小上界做近似值处理，因为实际上的最小上界是一个无穷序列，而Scala3则只需要简单地使用一个并集类型即可。
   *
   * 为了更好地理解，考虑下面这样一个类继承关系:
   */
  trait Fruit

  trait Plum extends Fruit

  trait Apricot extends Fruit

  trait Pluot extends Plum, Apricot

  /**
   * 这4种类型定义将交出如图 img_2.png 所示的类继承关系。
   * Fruit是Plum 和 Apricot共同的超类型，但并不是离它们最近的公共超类型。
   * Plum | Apricot这个并集类型才是离它们最近的公共超类型，或称最小上界。
   * 由图可知，并集类型Plum | Apricot是Fruit的子类型。的确是这样的，下面来演示一下:
   */
  val plumOrApricot: Plum | Apricot = new Plum {}

  //正常编译，因为Plum | Apricot <:Fruit
  val fruit: Fruit = plumOrApricot

  //在需要Plum | Apricot 的地方不能用Fruit (只能由上界指向下界)
  //val doesNotCompile:Plum |Apricot = fruit

  /**
   * @对称地看，Pluot同时是Plum和Apricot的子类型，但并不是离它们最近的公共子类型。
   * Plum&Apricot这个交集类型才是离它们最近的公共子类型，或称最大下界。
   * 由图可知，交集类型Plum & Apricot是Pluot的超类型。的确是这样的，下面来演示一下:
   */
  val pluot: Pluot = new Pluot {}

  //正常编译，因为 Pluot <: Pluot & Apricot
  val plumAndApricot: Pluot & Apricot = pluot

  /**
   * 对交集类型而言，我们可以调用任何定义在构成类型上的方法，
   * 或者访问任何定义在构成类型上的字段。
   * 例如，在Plum & Apricot的实例上，
   * 可以调用任何定义在Plum或Apricot的方法。
   *
   * 然而，对并集类型而言，我们只能访问其构成类型的公共超类型的成员。
   * 因此，在Plum | Aprioot的实例上，可以访问Fruit的成员（包括其继承自AnyRef和Any的成员）
   * 但不能访问在Plum或Apricot上添加的成员。
   *
   * @要访问这些成员，必须执行模式匹配来判断运行其相应值的实际类型。参考下面的例子:
   */
  def errorMessage(msg:Int | String): String =
    msg match {
      case n: Int => s"Error number: ${n.abs}"
      case s: String => s + "!"
    }

  /**
   * 这里的errorMessage方法的msg参数类型为Int | String。
   * 因此，只能对msg调用在Any中声明的方法，这是因为Any是Int和String唯一的公共超类型。
   * 我们无法直接访问在Int或String中定义的任何其他方法。
   * 例如，要访问Int的abs方法，或者String的字符串拼接操作符（+)，必须对msg执行模式匹配，
   * 如errorMessage方法体展示的那样。下面是一些使用errorMessage方法的例子:
   */
  println(errorMessage("Oops"))
  println(errorMessage(-42))



}
