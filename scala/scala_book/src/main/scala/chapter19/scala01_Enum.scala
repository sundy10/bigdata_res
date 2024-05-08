package chapter19

import chapter19.scala01_Enum.Direction.North

object scala01_Enum extends App {

  /**
   * 第19章 枚举
   *
   * Scala 3引入了枚举(enum)来精简密封样例类继承关系的定义。
   *
   * @枚举可以被用来定义主流的面向对象编程语言（如Java）中常见的枚举数据类型(enumerated data type)，
   * @以及函数式编程语言（如Haskell）中常见的代数数据类型(algebraic data type)。
   *                                          在 Scala中，这两个概念是被当作同一个光谱的两极来对待的，它们都采用enum定义。本章将对这两个概念做详细介绍。
   */

  /**
   * 19.1 枚举数据类型
   *
   * 枚举数据类型又称“EDT”，在你想要一个取值限于一组可穷举的带名值的类型时十分有用。
   * (虽然enum是枚举数据类型的更通用的简称,但是我们在本书中将使用EDT来表示enum，因为Scala的enum还被用于定义代数数据类型,即ADT。)
   * 这些带名值称作EDT的样例(case)。例如，可以像这样来定义用于表示指南针的4个方向的EDT:
   */
  enum Direction:
    case North, East, South, West

  /**
   * 这个简单的枚举将会生成一个名称为Direction的密封类，(我们将这个密封类称为“枚举类型”(enum type)。)
   *
   * @其伴生对象将包含4个被声明为val的值。这些val，即North、East、South和West，将具备类型Direction。
   * 有了这个定义，就可以（举例来说)定义一个 用模式匹配来反转指南针方向的方法，例如:
   */

  import Direction.{North, East, South, West}

  def invert(dir: Direction): Direction =
    dir match {
      case North => South
      case East => West
      case South => North
      case West => East
    }

  println(invert(North))
  println(invert(East))

  /**
   * @枚举数据类型之所以被称作“枚举”，是因为编译器会给每个(取值)样例关联一个Int序数(ordinal)。
   * @序数从0开始，每个（取值)样例以在枚举中定义的次序加1。你可以通过名称为ordinal的方法访问序数，这个方法是编译器针对每个EDT自动生成的。例如:
   */
  println(North.ordinal)
  println(East.ordinal)
  println(South.ordinal)
  println(West.ordinal)

  /**
   * @编译器还会针对每个EDT的枚举类型在其伴生对象中生成一个名称为values的方法。
   * @这个方法返回一个包含了该EDT所有样例（以声明次序)的数组。该数组的元素类型与枚举类型相同。
   * @例如，Direction.values将返回一个依次包含North、East、South和West的Array[Direction]:
   */
  val values: Array[Direction] = Direction.values
  println(values.toBuffer)

  /**
   * 最后，编译器还会向伴生对象中添加一个名称为valueOf的方法。
   * 该方法将一个字符串转换成该枚举类型的一个实例（前提是字符串完全匹配某一个样例名)。
   * 如果给定的字符串匹配不到任何样例，将会得到一个抛出的异常。这里有一些例子:
   */
  Direction.valueOf("North")
  Direction.valueOf("East")
  //Direction.valueOf("Up")

  /**
   * 你也可以给EDT传入值参数。这里有一个新版本的Direction，会捕获(capture)一个表示指南针表盘上的方向角度的Int值:
   */
  enum Direction1(val degress: Int):
    case North extends Direction1(0)
    case East extends Direction1(90)
    case South extends Direction1(180)
    case West extends Direction1(270)

  /**
   * 由于degrees被声明为参数化字段，因此你可以在任何Direction实例上访问它。这里有一些例子:
   */

  println(Direction1.North.degress)
  println(Direction1.South.degress)

  /**
   * @你还可以对enum类型添加自定义的方法，并放在enum的定义体中。
   * 例如，可以重新定义之前展示过的invert方法，并使其作为Direction的成员，就像这样:
   * 这样一来，你就可以“礼貌”地请Direction反转自己了:
   */
  enum Direction2(val degress: Int):
    case North extends Direction2(0)
    case East extends Direction2(90)
    case South extends Direction2(180)
    case West extends Direction2(270)

    def invert: Direction2 =
      this match {
        case North => South
        case East => West
        case South => North
        case West => East
      }

  println(Direction2.North.invert)
  println(Direction2.East.invert)

  /**
   * @你也可以自己定义EDT的伴生对象，只要不显式地给出values和valueOf方法，Scala就依然会帮你生成。
   * 例如，这里有一个Direction的伴生对象，定义了计算与传入角度值最近的方向的方法:
   */
  object Direction2:
    def nearestTo(degress: Int): Direction2 =
      val rem = degress % 360
      val angle = if rem < 0 then rem + 360 else rem
      val (ne, se, sw, nw) = (45, 135, 225, 215)
      angle match {
        case a if a > nw || a <= ne => North
        case a if a > ne && a <= se => East
        case a if a > se && a <= sw => South
        case a if a > se && a <= nw => West
      }

  /**
   * 伴生对象既提供了声明的方法，也提供了（自动)生成的方法。这里有一个同时使用了Direction对象中声明的nearestTo方法和（自动）生成的values方法的例子:
   * 这里的allButNearest函数返回一个包含除离传入角度值最近的方向之外所有方向的列表。下面是实际使用的例子:
   */
  //筛选不在入参的方位
  def allButNearest(degrees: Int): List[Direction2] =
    val nearest = Direction2.nearestTo(degrees)
    Direction2.values.toList.filter(_ != nearest)

  allButNearest(42)

  /**
   * @枚举的一个限制是不能针对单个样例定义方法，而是必须将方法作为enum类型自己的成员来声明。
   * @这就意味着这些方法对enum的所有样例都可见。(当然，也可以对特定的样例类型定义扩展方法，但是如果已经到了这个地步，还不如干脆手写这组密封样例类继承关系。)
   * @我们给enum提供样例清单的主要目的是规定一组用于构建该enum类型实例的固定可选方式。
   */

  /**
   * @与Java的枚举集成
   *
   * 在Scala中定义Java枚举，只需要简单地让你的Scala EDT扩展java.lang.Enum并以类型参数的形式给出Scala的enum类型即可。例如:
   */
  enum DirectionWithJava extends java.lang.Enum[DirectionWithJava]:
    case North,East,South,West

  /**
   * 除通常的 Scala EDT 特性之外，这个版本的Direction也是java. lang.Enum。
   * 举例来说，可以用java.lang.Enum 中定义的compareTo方法:
   */
  println(DirectionWithJava.East.compareTo(DirectionWithJava.South))

}
