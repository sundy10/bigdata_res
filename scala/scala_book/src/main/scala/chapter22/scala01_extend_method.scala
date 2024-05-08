package chapter22

object scala01_extend_method extends App {

  /**
   * 22.1 扩展方法的基础
   *
   * 想象这样的场景:你需要比较字符串是否相等，并在比较的过程中对字符串中的空白做两项特殊处理。
   * 首先，你需要忽略字符串前后两端的空白; 其次，你需要 要求字符串内部(即去除两端空白后)的空白区域在出现的位置上互相呼应，
   * 同时允许这些互相呼应的空白区域存在（长度上的）差别。
   *
   * 要实现这个逻辑,可以去掉（参加比较的)两个字符串前后两端的空白，将字符串内部任何连续的空白字符替换成单个空格，
   * 然后判断得到的字符串是否相等。这里有一个执行上述变换操作的函数:
   */
  def singleSpace(s:String):String =
    s.trim.split("\\s+").mkString(" ")

  /**
   * 这里的singleSpace函数接收一个字符串，然后将它变换成可以用 == 来比较的形式。
   * 首先，用trim去除字符串两端的空白。然后，调用split方法将进行trim操作后的字符串以连续空白为界 切分开，得到一个数组。
   * 最后，用mkString方法把这些不带空白的字符串重新拼接起来，并以单个空格隔开。这里有一些例子:
   */
  println(singleSpace("A Tale\tof Two   Cities"))//双城记
  println(singleSpace("   It was the\t\tbest\nof times."))//这是最好的时代

  /**
   * 可以用singleSpace函数来比较两个字符串是否相等，并且忽略它们在空白上的差异，就像这样:
   */
  val s = "One Fish, Two\tFish"
  val t = " One Fish, Two Fish"
  println(singleSpace(s)==singleSpace(t))

  /**
   * 这个设计非常合理。我们可以把singleSpace函数放在某个合适的单例对象中，然后开始下一个任务。
   * 不过，从人机交互界面的视角来看，你可能会觉得你的用户可能会倾向于能够直接在String上调用这个函数，就像这样:
   *
   * s.singleSpace == t.singleSpace
   */

  /**
   * 这样的写法会让这个函数使用起来感觉更面向对象。
   * 由于String是标准类库的一部分，因此为你的用户实现这个写法的最简单的方式是将singleSpace函数定义为扩展方法。参考示例22.1。
   * (其他更有挑战的方式是通过JavaCommunity Process或Scalalmprovement Process来将singleSpace添加到String。)
   */
  //针对String的扩展方法
  extension (s:String)
    def singleSpaceExt:String =
      s.trim.split("\\s+").mkString(" ")

  /**
   * extension关键字能够制造这样一种假象（illusion):在不修改类本身的前提下对这个类添加了成员函数。
   * 在extension之后的圆括号内，你可以放置一个你想“添加”方法的（目标)类型的变量。这个变量指向的对象被称为扩展方法的接收者（receiver)。
   * 在本例中，"(s: String)”表示你想对String添加这个方法。在这个“开场白”之后，你就可以像编写其他方法那样编写这个方法，
   * 区别是你可以在方法体内使用接收者，即本例中的s。
   */

  /**
   * 对扩展方法的使用被称为应用(application)。这里有一个例子，扩展方法singleSpaceExt被引用了两次来比较两个字符串（是否相等)︰
   */
  println(s.singleSpaceExt == t.singleSpaceExt)

  /**
   * 虽然扩展方法的定义看起来有些像一个接收 接收者作为构造方法参数的匿名类的定义（这样一来，接收者对象就对匿名类的所有方法可见)，
   * 但是实际上并不是这样。扩展方法的定义会当场（in place)被改写为直接接收 接收者作为参数的方法。
   * @举例来说，编译器会将示例上述中的扩展方法定义改写为示例所示的形式。
   */
  //带有内部扩展标记
  def singleSpaceExt1(s:String):String =
    s.trim.split("\\s+").mkString(" ")

  /**
   * @对于被改写的方法，唯一特殊的点在于编译器会给它一个内部标记，将它标记为扩展方法。
   * @让这个扩展方法可见的最简单的方式是把被重写的方法名引入当前的语法作用域内。下面是用REPL展示的例子: 23.1.png
   *
   * 在这个REPL会话中，由于singleSpace在语法作用域内，且内部标记为扩展方法，因此它可以被正常地应用:
   */

  /**
   * @由于Scala是当场改写扩展方法，在扩展方法被应用时，不会有额外的、不必要的装箱动作。
   * @而对Scala2中采用的隐式类而言，却并不总是这样。因此，扩展方法带给你的是“没有遗憾的语法糖”。
   * @在接收者中调用扩展方法，如s.singleSpace，总是能带给你等同于将接收者传递给相应的非扩展方法，如singleSpace(s)的性能表现。
   */




}
