package chapter21

object scala07_multi_context_parameter extends App {

  /**
   * 21.7 当有多个上下文参数可选时
   *
   * @可能会存在这样一种情况:作用域内有多个上下文参数，且每一个上下文参数都能工作。在大多数时候，Scala会在这样的情况下拒绝使用任何上下文参数。
   * 上下文参数在那些被省去的参数列表完全显而易见且纯粹是样板代码时效果最好。
   * 毕竟，在有多个上下文参数可选时，应该选哪一个并不是那么明显。我们来看示例21.7。
   */
  //多个上下文参数
  class PreferredPrompt(val preference:String)

  object Greeter:
    def greet(name:String)(using prompt:PreferredPrompt) =
      println(s"Welcome, $name. The system is ready.")
      println(prompt.preference)

  object JillsPrefs:
    given jillsPrompt:PreferredPrompt =
      PreferredPrompt("Your wish>")

  object JoesPrefs:
    given joesPrompt:PreferredPrompt =
      PreferredPrompt("relax>")

  import JillsPrefs.jillsPrompt

  Greeter.greet("name")


  /**
   * 示例21.7的JillsPrefs和JoesPrefs两个对象都提供了PreferredPrompt上下文参数。
   * 如果把这两个上下文参数都引入，则在当前的语法作用域内会同时出现两个不同的标识符，即jillsPrompt和joesPrompt :
   *
   * 如果这时再次尝试调用Greeter.greet，则编译器会拒绝在这两个可用的上下文参数中做选择。
   */

  /**
   * 这里的歧义很真实。Jill偏好的命令行提示符与Joe偏好的完全不同。
   * @在这种情况下，程序员应该显式地给出想要哪一个。只要有多个上下文参数可选，编译器就会拒绝做出选择，
   * 除非其中一个上下文参数比其他上下文参数更具体。这种情况与方法重载是一样的。
   * 当你调用foo(null)而代码中有两个不同的重载foo方法都接收null时，编译器就会拒绝这样的代码，它认为这个方法调用的目标是有歧义的。
   */

  /**
   * 而当可选的上下文参数中有一个上下文参数比其他上下文参数严格来说更具体时，编译器会选择这个更具体的上下文参数。
   * 这背后的理念是，只要有理由相信程序员总是会选择某一个上下文参数，那就不要 要求程序员显式地写出来。
   * 毕竟，方法重载也有相同的（宽松)处理机制。继续前面的例子，如果可选的foo方法中有一个接收String而另一个接收Any，
   * 就选择接收String的版本，因为它显然更具体。
   *
   * 更准确地说，当满足如下条件中的任意一条时，某个上下文参数就比另一个上下文参数更具体:
   *    @·前者的类型是后者的子类型。
   *    @·前者的包含类扩展自后者的包含类。
   */

  /**
   * 如果你有两个可能存在歧义的上下文参数，又有明显的首选和次选之分，
   * 则可以把后者放在“LowPriority”(低优先)特质中而把前者放在这个特质的子类或子对象中
   * 这样一来，即使上下文参数可能会带来歧义，编译器也会选择首选的那个上下文参数，只要它可用即可。
   * 当高优先的上下文参数不可用而低优先的可用时，编译器会选择低优先的那一个。
   */




}
