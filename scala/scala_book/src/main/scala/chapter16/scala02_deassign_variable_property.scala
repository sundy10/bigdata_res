package chapter16

import java.util.Date

object scala02_deassign_variable_property extends App {

  /**
   * 16.2 可被重新赋值的变量和属性
   *
   * 我们可以对一个可被重新赋值的变量做两种基本操作:
   * 获取它的值和 将它设置为新值。
   * 在诸如JavaBeans的类库中，这些操作通常被包装成单独的getter和setter方法，我们需要显式定义这些方法。
   */

  /**
   * @在Scala中，每一个非私有的var成员都隐式地定义了对应的getter和setter方法。不过，这些getter和setter方法的命名习惯与Java的命名习惯不一样。
   * @var x的getter方法命名为“x”，而它的setter方法命名为“x_=”。
   *
   *      举例来说，如果出现在类中，如下的var定义:
   */
  var hour = 12

  /**
   * @除了定义一个可被重新赋值的字段,还将生成一个名称为"hour”的getter方法和一个名为“hour_=”的setter方法。
   * @其中的字段总是被标记为private[this]，意味着这个字段只能从包含它的对象中访问。而getter和setter方法则拥有与原来的var相同的可见性。
   * @如果原先的var定义是公有的，则它的getter和setter方法也是公有的;如果原先的var定义是受保护的，则它的getter和setter也是受保护的;以此类推。
   *
   * 参考示例16.2中的Time类，它定义了两个公有的var，即hour和minute。
   * 这个实现与示例16.3中的类定义完全等效。在示例16.3的定义中，局部字段h和m的名称是随意选的，只要不与已经用到的名称冲突即可。
   */
  class Time:
    var hour = 12
    var minute = 0

  private val time = new Time
  time.hour_=(1)
  time.hour = 1
  println(time.hour)

  //公有var是如何被展开成getter和setter方法的
  class Time1:
    private var h = 12
    private var m = 0

    def hour: Int = h

    def hour_=(x: Int) = h = x

    def minute: Int = m

    def minute_=(x: Int) = m = x

  /**
   * 这个将公有var展开成getter和setter方法的机制有趣的一点在于，我们仍然可以直接定义getter和setter方法，而不是定义一个var。
   *
   * @通过直接定义这些访问方法，我们可以按自己的意愿来解释变量访问和赋值的操作。例如，示例16.4中的Time类变种包含了针对hour和minute赋值的要求，明确了哪些值是不合法的。
   */
  class Time3:
    private var h = 12
    private var m = 0

    def hour: Int = h

    def hour_=(x: Int) =
      require(0 <= x && x < 24)
      h = x

    def minute = m

    def minute_(x: Int) =
      require(0 <= x && x < 60)
      m = x


  /**
   * @某些语言对于这些类似变量的值有特殊的语法表示，它们不同于普通变量的地方在于getter和setter方法可以被重新定义。
   * @例如，C#由属性来承担这个角色。从效果上讲,Scala总是将变量解读为setter和getter方法的习惯，让我们在不需要特殊语法的情况下获得了与C#属性一样的功能。
   *
   * 属性可以有很多用途。在示例16.4中,setter方法强调了一个恒定的规则，防止变量被赋予非法值。我们还可以用属性来记录所有对getter和setter方法的访问。
   * @我们可以将变量和事件集成起来，比如，每当变量被修改时都通知某些订阅者方法。
   */

  /**
   * 有时候，定义不与任何字段关联的getter和setter方法也是有用的，且 Scala允许我们这样做。
   * 举例来说，示例16.5给出了一个Thermometer类，这个类封装了一个表示温度的变量，可以被读取和更新。温度可以用摄氏度和华氐度来表示。这个类允许我们用任意一种标度来获取和设置温度。
   */

  import scala.compiletime.uninitialized

  class Thermometer:
    var celsius: Float = uninitialized

    def fahrenheit = celsius * 9 / 5 + 32

    def fahrenheit_=(f: Float) = celsius = (f - 32) * 5 / 9

    override def toString = s"${fahrenheit}F/${celsius}"

  /**
   * 这个类定义体的第一行定义了一个var变量celsius，用来包含摄氏度的温度。
   * celsius变量一开始被设置为默认值，因为我们给出了'uninitialized'作为它的“初始值”。
   *
   * @更确切地说，某个字段的“=uninitialized”初始化代码会给这个字段赋一个零值(zero value)。
   *                                                   具体零值是什么取决于字段的类型。数值类型的零值是0，布尔值的零值是false，引用类型的零值是null。这与Java中没有初始化代码的变量效果一样。
   * @注意，在Scala中并不能简单地去掉“=uninitialized”。如果我们是这样写的:
   * var celsius:Float
   * @将会定义一个抽象变量，而不是一个没有被初始化的变量。
   *
   * 在celsius变量之后，是getter方法“fahrenheit”和setter方法"fahrenheit_=”的定义，它们访问的是同一个温度变量，但是以华氏度表示，并没有单独的变量来以华氐度保存温度。
   * 华氏度的getter和setter方法会自动与摄氐度做必要的转换。参考下面使用Thermometer对象的例子:
   */
  val t = new Thermometer
  println(t)

  t.celsius = 100
  println(t)

  t.fahrenheit = -40
  println(t)


}
