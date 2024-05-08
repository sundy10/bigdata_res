package chapter10

object scala02_abstract extends App {

  /**
   * 10.2 抽象类
   */

  /**
   * 我们的第一个任务是定义Element类型，用来表示布局元素。
   * 由于元素是一个由字符组成的二维矩形，用一个成员contents来表示某个布局元素的内容是合情合理的。
   * 内容可以用字符串的向量表示，每个字符串代表一行。
   * 因此，由contents返回的结果类型将会是Vector[String]。示例10.1给出了相应的代码。
   */
  abstract class Element{
    def contents:Vector[String]
  }

  /**
   * 在这个类中，contents被声明为一个没有实现的方法。
   * 换句话说，这个方法是Element类的抽象成员(abstract member)。
   * @一个包含抽象成员的类本身也要被声明为抽象的，具体做法是在class关键字之前写上abstract修饰符:
   */

  /**
   * abstract修饰符表明该类可以拥有那些没有实现的抽象成员。
   * 因此，我们不能直接实例化一个抽象类，尝试这样做将遇到编译错误:
   * new Element
   */

  /**
   *  在本章稍后的内容中，你将看到如何创建Element类的子类。
   *  这些子类可以被实例化，因为它们填充了Element抽象类中缺少的contents定义。
   */

  /**
   * 注意，Element类中的content方法并没有标上abstract修饰符。一个方法只要没有实现(即没有等号或方法体)，它就是抽象的。
   * 与Java不同，我们并不需要（也不能)对方法加上abstract修饰符。那些给出了实现的方法叫作具体方法。
   */

  /**
   * 另一组需要区分的术语是声明( declaration)和定义(definition)。Element类“声明”了content这个抽象方法，但目前并没有“定义”具体的方法。
   * 不过下一节将通过定义一些具体方法来增强Element类。
   */

}
