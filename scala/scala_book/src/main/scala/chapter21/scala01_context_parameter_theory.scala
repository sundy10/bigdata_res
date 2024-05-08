package chapter21

import chapter21.scala01_context_parameter_theory.{PreferredDrink1, PreferredPrompt}

object scala01_context_parameter_theory extends App {

  /**
   * 21.1 上下文参数的工作原理
   *
   * 编译器有时会把someCall(a)替换成someCall(a)(b)，或者把newSomeClass(a)替换成new SomeClass(a)(b)，
   * 从而通过添加一个或多个缺失的参数来完成一次函数调用。这时，整个柯里化的参数列表都被提供出来，而不是单个的参数。
   * 举例来说，如果someCall缺失的参数列表需要3个参数，则编译器可以把someCall(a)替换成someCall(a)(b, c, d)。
   * 为了支持这样的用法，被插入的标识符，如(b, c, d)中的b、c和d，必须在定义时被标记为given，并且someCall或SomeClass自身的定义中的参数列表也必须由using开头。
   */

  /**
   * 下面来看一个例子，假设有很多方法接收一个当前用户偏好的命令行提示符参数（如$或>)。
   * 那么，你可以通过将这个命令行提示符标记为上下文参数的方式来减少样板代码。
   * @首先，把用户偏好的命令行提示符用一个特殊类型封装起来:
   */
  class PreferredPrompt(val preferredPrompt: String)

  /**
   * @然后，重构每一个接收命令行提示符的方法，并将这个参数放到一个单独的由using标记的参数列表中。
   * 例如，下面的这个Greeter对象有一个接收PreferredPrompt作为上下文参数的greet方法:
   */
  object Greeter:
    def greet(name:String)(using  prompt:PreferredPrompt) =
      println(s"Welcome, $name. The system is ready.")
      println(prompt.preferredPrompt)

  /**
   * @要想让编译器隐式地提供这个上下文参数，必须用given关键字定义一个满足预期类型要求（对应到本例中就是PreferredPrompt)的上下文参数实例。
   * 例如，可以像下面这样在一个表示用户偏好的对象中定义这个上下文参数:
   */
  object JillsPrefs:
    given prompt:PreferredPrompt =
      PreferredPrompt("Your wish> ")

  /**
   * 如此一来,编译器就能够隐式地提供这个PreferredPrompt了，前提是它必须在作用域内，参考下面的例子:
   */
  //Greeter.greet("Jill")

  /**
   * 一旦你通过import把PreferredPrompt引入，编译器就会用它来填充缺失的参数列表:
   */


  import JillsPrefs.prompt
  Greeter.greet("Jill")

  /**
   * 由于prompt被声明为上下文参数，因此如果你尝试以常规的方式传参，则程序是无法通过编译的:
   */
  //Greeter.greet("Jill")(JillsPrefs.prompt)

  /**
   * 你必须在调用点用using关键字显式地表示你要填充的是上下文参数，就像这样:
   */
  Greeter.greet("Jill")(using JillsPrefs.prompt)

  /**
   * 需要注意的是,using关键字针对的是整个参数列表，而不是单个的参数。
   * 示例21.1给出了这样一个例子，Greeter对象的greet方法的第二个参数列表(被标记为using的那一个)有两个参数:
   *    prompt(类型为PreferredPrompt)和drink(类型为PreferredDrink)。
   */
  //带有多个参数的隐式参数列表
  class PreferredPrompt1(val preference: String)
  class PreferredDrink1(val preference: String)

  object Greeter1:
    def greet(name :String)(using prompt:PreferredPrompt1,drink:PreferredDrink1) =
      println(s"Welcome, $name. The system is ready.")
      print("But while you work, ")
      println(s"why not enjoy a cup of ${drink.preference}?")
      println(prompt.preference)

  object JoesPrefs1:
    given prompt1:PreferredPrompt1 =
      PreferredPrompt1("relax> ")
    given drink1:PreferredDrink1 =
      PreferredDrink1("tea")

  /**
   * JoesPrefs1单例对象声明了两个上下文参数实例:类型为PreferredPrompt的prompt和类型为PreferredDrink的drink。
   * @不过，就像我们在前面看到的那样，只要这些定义不（以单个标识符的形式)出现在作用域内，编译器就不会用它们来填充greet方法缺失的参数列表:
   */
  import JoesPrefs1.{prompt1,drink1}

  /**
   * 由于prompt和drink现在是以单个标识符的形式出现在作用域内的，因此可以显式地用它们来填充最后的参数列表，就像这样:
   */
  Greeter1.greet("Joe")(using prompt1,drink1)

  /**
   * 而且，由于现在所有关于上下文参数的条件都被满足了，因此也可以选择让Scala编译器帮助你给出prompt1和口drink1，只需把整个参数列表去掉即可:
   */
  Greeter1.greet("Joe")

  /**
   * 关于前面这些例子，有一点需要注意，我们并没有用String作为prompt和drink的类型，尽管它其实就是通过preference字段给出的String。
   * 由于编译器是通过匹配参数的类型和上下文参数实例的类型来选择上下文参数的，因此上下文参数的类型应该是足够“少见”或“特殊”的，从而避免意外的匹配。
   * 举例来说，示例21.1中的PreferredPrompt类型和PreferredDrink类型存在的唯一目的就是提供上下文参数。
   * 这样做的结果就是，如果这些类型的实例本意不是被用作类似greet这样的方法的上下文参数，它们就不太可能存在。
   */



}
