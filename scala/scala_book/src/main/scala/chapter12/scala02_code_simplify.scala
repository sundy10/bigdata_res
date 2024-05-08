package chapter12



object scala02_code_simplify extends App {

  /**
   * 12.2 对相关代码的精简访问
   *
   * 把代码按照包层次结构划分以后，不仅有助于人们浏览代码，也是在告诉编译器，同一个包中的代码之间存在某种相关性。
   * 在访问同一个包的代码时，Scala允许我们使用简短的、不带限定前缀的名称。
   */

  /**
   * 示例12.4给出了3个简单的例子。
   * 首先，就像你预期的那样，一个类不需要前缀就可以在自己的包内被别人访问。这就是newStarMap能够通过编译的原因。
   * StarMap类与访问它的new表达式同属于bobsrockets. navigation包，因此并不需要加上包名前缀。
   *
   * 其次，包自身也可以从包含它的包里被不带前缀地访问。
   * 在示例12.4中，需要注意Navigator类是如何实例化的。new表达式出现在bobsrockets包中，这个包包含了bobsrockets.navigation包。
   * 因此，它可以简单地用navigation访问bobsrockets.navigation包的内容。
   *
   * 再次，在使用嵌套打包语法时，所有在包外的作用域内可被访问的名称，在包内也可以被访问。
   * 示例12.4给出的例子是用addShip()创建new Ship。
   * 该方法有两层打包:外层的bobsrockets包和内层的bobsrockets. fleets包。由于Ship在外层可以被访问，因此在addShip()中也可以被引用。
   *
   *  package bobsrockets:
   *    package navigation:
   *      class Navigator:
   *        //不需要写成bobsrockets.navigation.
   *        StarMapval map = new StarMap
   *
   *      class StarMap
   *     class Ship:
   *      //不需要写成bobsrockets.navigation.Navigator
   *      val nav = new navigation.Navigator
   *     package fleets:
   *      class Fleet:
   *      //不需要写成bobsrockets.Ship
   *      def addShip = new Ship
   */

  /**
   * 注意，这类访问只有当你显式地嵌套包时才有效。如果你坚持每个文件只有一个包的做法，则(就像Java一样)只有那些在当前包内定义的名称才（直接)可用。
   * 在示例12.5中，bobsrockets.fleets包被移到了顶层。由于它不再位于bobsrockets包内部，来自bobsrockets包的内容不再直接可见，因此new Ship将给出编译错误。
   *
   * 如果用缩进嵌套包让你的代码过于向右侧缩进，也可以使用多个package子句，但不使用缩进。例如，如下代码同样将Fleet类定义在两个嵌套的包(bobsrockets和fleets）里，就像你在示例12.4中看到的一样:
   * (这种不带花括号(译者注:代码缩进)的多个package了句连在一起的样式称作“链式包子句”(chained package clauses))
   */

  /**
   * 最后一个小技巧也很重要。
   * 有时，你会遇到需要在非常拥挤的作用域内编写代码的情况，包名会相互遮挡。
   * 在示例12.6中，MissionControl类的作用域内包含了3个独立的名称为launch的包。
   * bobsrockets. navigation包里有一个launch包,bobsrockets包里有一个launch包，顶层还有一个launch包。
   * 你应该如何分别引用推进器类Booster1、Booster2和口Booster3呢?
   *
   * 访问第一推进器类Booster1很容易，但是直接引用launch包会指向bobsrockets.navigation.launch包，因为这是最近的作用域内定义的launch包。
   * 因此，可以简单地用launch.Booster1来引用第一个推进器类。
   * 访问第二个推进器类 Booster2也不难，可以用bobsrockets. launch. Booster2，这样就能清晰地表达你要引用的是哪一个包。
   * 此时问题就剩下第三个推进器类Booster3了，那么考虑到嵌套的launch包遮挡了位于顶层的那一个，应当如何访问Booster3呢?
   *
   * 为了解决这个问题，Scala提供了一个名称为 _root_ 的包，这个包不会与任何用户编写的包冲突。
   * 换句话说，每个你能编写的顶层包都被当作_root_包的成员。例如，示例12.6中的 launch和bobsrockets都是_root_包的成员。
   * 因此,_root_.launch表示顶层的那个launch包，而_root_.launch.Booster3指定的就是那个最外围的推进器类。
   */

}
