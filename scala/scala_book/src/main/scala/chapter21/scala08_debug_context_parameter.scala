package chapter21

object scala08_debug_context_parameter extends App {

  /**
   * 21.8 调试上下文参数
   *
   * 上下文参数是Scala的一个很强大的特性，不过有时很难正确使用它。本节包含一些用于调试上下文参数的小技巧。
   *
   * 有时你可能会觉得奇怪，编译器为什么没有找到那个你认为它应该能找到的上下文参数。
   * 在这种情况下，用using显式地传入上下文参数是有帮助的。如果在这样做以后编译器依然报错，你就知道编译器为什么不采纳你的上下文参数了。
   *
   * 另一方面，显式地插入上下文参数也有可能让编译器不再报错。如果是这种情况，你就知道是因为其他规则（如可见性规则）阻止了对这个上下文参数的使用。
   */

  /**
   * 当你在调试程序时，有时候能看到编译器插入了什么上下文参数也是很有用的。
   * 我们可以给编译器加上-Xprint:typer选项。如果你以这个选项运行scalac，则编译器会向你展示类型检查器添加了所有上下文参数之后的代码效果。
   * 示例21.8和示例21.9给出了这样一个例子。如果你查看这两份代码清单的最后一行，则会看到enjoy的第二个参数列表（在示例21.8中被省去）被编译器插入了（见示例21.9):
   */

  /**
   * 如果你足够勇敢，则可以试着用scala -Xprint:typer启动交互式命令行，从而打印出其内部使用的经过类型检查的源码。
   * 如果你这样做了，就准备看到在你的核心代码基础上生成的体量庞大的样板代码吧!
   * (诸如IntelliJ和Metals这样的IDE包含展示插入的上下文参数的选项。)
   */


  object Mocha:
    class PreferredDrink(val preference:String)
    given pref:PreferredDrink = new PreferredDrink("mocha")

    def enjoy(name:String)(using drink:PreferredDrink):Unit =
      println(s"Welcome, $name")
      print(". Enjoy a ")
      print(drink.preference)
      println("!")

    def callEnjoy:Unit = enjoy("reader")

  Mocha.callEnjoy


  /**
   * 21.9 结语
   *
   *
   * 上下文参数可以让函数签名更容易阅读:代码阅读者可以将注意力集中在函数的真实意图上，而不是与大量的函数参数样板代码做斗争上，
   * 这些样板代码的参数可以交给函数的上下文来提供。对上下文参数的查询发生在编译期，因此可以确保这些参数值在运行期也可见
   *
   * 本章描述了上下文参数机制。该机制可以隐式地向函数传递参数，这有助于减少样板代码，同时可以让函数消费和操作所有需要的参数。
   * 正如你看到的那样，对上下文参数的查询是基于函数参数的类型进行的:只要有合适的参数类型的值可以用于隐式传参，编译器就会采用这个值并将它传递给目标函数。
   * 你还看到了使用这个机制来完成特定目的多态的例子，如Ordering类型族。
   * 在下一章，你将了解到扩展方法如何使用类型族，而在第23章，你还将看到更多类型族使用上下文参数的例子。
   */



}
