package chapter10

object scala10_final extends App {

  abstract class Element{
    def demo = "Element's implementation invoked"
  }

  /**
   * 10.10 声明final成员
   *
   * @有时，在设计类继承关系的过程中，你可能想确保某个成员不能被子类继承。
   * 在Scala中，与Java—样，可以通过在成员前面加上final修饰符来实现。如示例10.7所示，可以在VectorElement类的demo方法前放一个final修饰符。
   */

  class VectorElement extends Element{
    final override def demo: String = "VectorElement's implementation invoked"
  }

  /**
   * 有了这个版本的VectorElement类，则在其子类LineElement中尝试重写demo方法，会导致编译错误:
   */

  final class LineElement extends VectorElement{
    //override def demo: String = "LineElement's implementation invoked"
  }

  /**
   * @你可能有时候还想确保整个类没有子类，则可以简单地将类声明为不可更改的，做法是在类声明之前添加final修饰符。例如，示例10.8给出了如何声明VectorElement类为不可更改的。
   */

  //无法通过编译
  //class CharElement extends LineElement{}


  /**
   * 现在去掉final修饰符和demo方法，回到Element家族的早期实现。本章剩余部分将集中精力完成该布局类库的一个可工作版本。
   */
}
