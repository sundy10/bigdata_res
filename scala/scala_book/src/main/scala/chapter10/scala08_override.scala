package chapter10

object scala08_override extends App {

  /**
   * 10.8 使用override修饰符
   *
   * 需要注意的是，LineElement类的width和height方法的定义前面都带上了override修饰符。
   * 你曾在6.3节的toString方法的定义中看到过这个修饰符。
   * @Scala要求我们在所有重写了父类具体成员的成员之前加上这个修饰符。而如果某个成员并不重写或继承基类中的某个成员，则这个修饰符是被禁用的。
   * 由于LineElement类的height和width方法的确重写了Element类中的具体定义，因此override这个修饰符是必需的。
   */

  /**
   * 这样的规则为编译器提供了有用的信息，可以帮助我们避免某些难以捕获的错误，让系统得以更加安全地进化。
   * 举例来说，如果你碰巧拼错了方法或不小心给出了错误的参数列表，则编译器将反馈出错误消息:
   */

  /**
   * 这个override的规约对于系统进化来说更为重要。
   * 比如，你打算定义一个2D绘图方法的类库。
   * 你公开了这个类库，并且有很多人使用。
   * 在这个类库的下一个版本中，你打算给基类Shape添加一个新的方法，签名如下:
   *
   * def hidden():Boolean
   */

  /**
   * 你的新方法将被多个绘图方法用来判定某个形状是否需要被绘制出来。这有可能会带来巨大的性能提升，
   * 不过你无法在不产生破坏使用方代码的风险的情况下添加这个方法。毕竟，类库的使用者可能定义了带有不同hidden方法实现的Shape子类。
   * 而且或许使用方的方法实际上会让接收调用的对象消失而不是测试该对象是否隐藏。由于两个版本的hidden方法存在重写的关系，你的绘图方法最终会让对象消失，但这显然并不是你要的效果。
   */

  /**
   * 这些“不小心出现的重写”就是所谓的“脆弱基类”(fragilebase class）问题最常见的表现形式。
   * 这个问题之所以存在，是因为如果你在某个类继承关系中对基类（我们通常将其称为超类）添加新的成员，则将面临破坏使用方代码的风险。
   * Scala并不能完全解决脆弱基类的问题，但与Java相比，它对此种情况有所改善。
   * 如果这个绘图类库和使用方代码是用Scala编写的，则使用方代码中原先的hidden实现并不会带上override修饰符，因为当时并没有其他方法使用了这个名称。
   * 也就是说，使用你的类库的代码并不会表现出错误的行为，而是会得到一个编译期错误，这通常是更优的选择。
   */

}