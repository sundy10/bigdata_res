package chapter19

import chapter19.scala02_algebraic_data_type.Eastwood.Ugly
import chapter19.scala02_algebraic_data_type.Eastwood1.Good
import chapter19.scala02_algebraic_data_type.Seinfeld.Nada

object scala02_algebraic_data_type extends App {

  /**
   *
   * 19.2 代数数据类型
   *
   * 代数数据类型(ADT）是由一组有限取值样例组成的数据类型。
   * ADT是一种通过对数据的可取值样例逐一建模的方式来表达领域模型的自然方式。
   * 每个样例表示一个“数据构造方法”，即构建该类型实例的一种特定方式。
   * 在Scala中，一组密封样例类可以组成一个ADT，只要其中至少有一个样例接收参数即可。
   * (EDT则不同，EDT虽然也是由一组密封样例类组成的，但这些样例都不接收参数。)
   * 例如，这里有一个表示3种可能性的ADT:预期的值，“好”的类型;错误的值，“坏”的类型;异常，“丑”的类型。
   */
  enum Eastwood[+G, +B]:
    case Good(g: G)
    case Bad(b: B)
    case Ugly(ex: Throwable)

  /**
   * 就像你在EDT中看到的，不能对某个具体的样例(Good、Bad、Ugly)定义方法，而只能对其公共超类（Eastwood)定义方法。
   * 这里有一个对Good值做变换(如果Eastwood是Good)的map方法的例子:
   */
  enum Eastwood1[+G, +B]:
    def map[G2](f: G => G2): Eastwood1[G2, B] =
      this match {
        case Good(g) => Good(f(g))
        case Bad(g) => Bad(g)
        case Ugly(ex) => Ugly(ex)
      }
    case Good(g: G)
    case Bad(b: B)
    case Ugly(ex: Throwable)

  //这个方法的使用举例:
  val eastWood = Eastwood1.Good(41)
  println(eastWood.map(n=>n+1))
  val Wood1 = Eastwood1.Bad(1)
  println(Wood1.map(f=>f))
  /**
   * ADT的实现与EDT的实现略有不同。
   * @对于每一个接收参数的ADT样例,编译器会在该enum类型的伴生对象中生成一个样例类。因此，对Eastwood而言，编译器会生成类似如下的代码: 23.1.png
   * 虽然这些样例类创建的工厂方法的结果类型是具体的样例类型，但是编译器会将它们扩展为更通用的enum类型。
   */


  /**
   * 如果你需要针对某个样例的更具体的类型，则可以用new而不是工厂方法来构造实例。
   * @举例来说，虽然“Good(1)”的类型为Eastwood[Int,Nothing]，但是“new Good(1)”将具备更具体的类型，即Good[Int,Nothing]。
   */
   val value:Good[Int,Nothing] = new Good(1)

  /**
   * @ADT可以是递归的。
   * 例如，样例类可以接收一个enum类型的实例作为参数。使用递归ADT的一个好的案例是链表。
   * 可以把链表定义为具有两个子类型的密封类:表示空列表的单例对象和接收两个参数(头部元素和尾部列表)的cons单元格类。
   * 这里有一个链表类型，其空列表对象名为Nada，而其cons单元格类名为Yada:
   */
  enum Seinfeld[+E]:
    def ::[E2>:E](o:E2):Seinfeld[E2] = Yada(o,this)
    case Yada(head:E,tail:Seinfeld[E])
    case Nada

  /**
   * 这里的Seinfeld ADT是一个递归类型，因为Yada样例类接收另一个Seinfeld[曰作为其tail参数。
   * 鉴于Seinfeld声明了::方法，你可以像Scala的List那样构建Seinfeld实例，不过不是用Nil而是用Nada(来表示空列表):
   */
  val xs =1::2::3::Nada
  println(xs.toString.toBuffer)


}
