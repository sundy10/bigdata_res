package chapter12

object scala03_import extends App {

  /**
   * 12.3 引入
   *
   * 在Scala中，可以用import子句引入包及其成员。被引入的项目可以用File这样的简单名称访问，而不需要限定名称（如java.io.File)。参考示例12.7。
   *
   * import子句使得某个包或对象的成员可以只用它的名称访问，而不需要在前面加上包名或对象名。下面是一些简单的例子: 12.7
   */

  /**
   * 第一个import子句对应Java的单类型引入，而第二个import子句则对应Java的按需（on-demand）引入。
   * @虽然在Scala2的按需引入中跟在后面的是下画线(_）而不是星号(*)，但是在Scala3中已经改成了星号，以与其他语言保持一致。
   * 上述第三个import子句对应Java对类静态字段的引入。
   *
   * 这3个import子句让你对引入能做什么有了一个感性认识，不过Scala的引入实际上更加通用。
   * 首先，Scala的引入可以出现在任何地方，不仅仅是在某个编译单元的最开始。其次，还可以引用任意的值，比如，示例12.8给出的import子句是可以做到的。
   *
   * showFruit方法引入了其参数fruit(类型为Fruit)的所有成员。这样接下来的printIn语句就可以直接引用name和color。
   * 这两个引用等同于fruit.name和fruit.color。这种语法在需要用对象来表示模块时尤其有用，可参考第7章。
   */

  /**
   * @Scala的灵活引用
   *
   *  与Java相比，Scala的import子句要灵活得多。主要的区别有3点，在Scala中,import子句可以:
   *  @出现在任意位置。
   *  @引用对象（无论是单例对象还是常规对象)，而不只是包。
   *  @让你重命名并隐藏某些被引入的成员。
   *
   *  还有一点可以说明Scala的引入更灵活:它可以引入包本身，而不仅仅是这些包中的非包成员。
   *  如果把嵌套的包想象成被包含在上层包内，则这样的处理很自然。
   *  例如，在示例12.9中，被引入的包是java.util.regex，这使得我们可以在代码中使用regex这个简称。要访问java.util.regex包里的Pattern单例对象，可以直接使用regex.Pattern,参考示例12.9。
   */

  /**
   * Scala中的引入还可以重命名或隐藏指定的成员。做法是包在花括号内的引入选择器子句( import selector clause）中，这个子句跟在那个我们要引入成员的对象后面。下面是一些例子:
   *
   * import Fruits.{Apple, Orange}
   * 这只会从Fruits对象引入Apple和Orange两个员。
   *
   * import Fruits.{Apple as McIntosh, Orange}
   * 这会从Fruits对象引入Apple和Orange两个成员。不过Apple对象被重命名为McIntosh，因此代码中要么用Fruits.Apple要么用McI ntosh来访问这个对象。
   * 重命名子句的形式永远都是“<原名> as <新名>”。如果你只打算引入并重命名一个名称的话，则可以省去花括号:
   */
  import java.sql.Date as SDate

  /**
   * 这会以SDate为名引入SQL日期类，这样就可以同时以Date这个名称引入Java的普通日期对象。
   *
   * 这会以S为名引入java.sql包，这样就可以编写类似S.Date这样的代码。
   */
  import java.sql as S

  /**
   * import Fruits.{*}
   * 这会引入Fruits对象的所有成员，其含义与import Fruits.*—样。
   *
   * import Fruits.{Apple as McIntosh，*}
   * 这会引入Fruits对象的所有成员并将Apple重命名为Mclntosh。
   *
   * import Fruits.{Pear as _，*}
   * 这会引入除Pear之外的Fruits对象的所有成员。形如“<原名> as _”的子句将在引入的名称中排除<原名>。
   * 从某种意义上讲，将某个名称重命名为_'意味着将它完全隐藏。
   * 这有助于避免歧义。比如，你有两个包，即Fruits和Notebooks，都定义了Apple类。如果你只想获取名称为Apple的笔记本，而不是同名的水果，则仍然可以按需使用两个引入，就像这样:
   *
   * import Laptops.*
   * import Fruits.iApple as _，*}
   * 这会引入所有的Notebooks成员和所有的Fruits成员（除了Apple)。
   *
   * 这些例子展示了Scala在选择性地引入成员，以及用别名来引入成员方面提供的巨大的灵活度。总之，引入选择器可以包含:
   *
   * 一个简单的名称x。这将把x包含在引入的名称集里。
   * 一个重命名子句x as y。这会让名称为x的成员以y的名称可见。·一个隐藏子句x as_。这会从引入的名称集里排除x。
   * 一个隐藏子句x as_。这会从引入的名称集里排除x。
   * 一个“捕获所有”的*'。这会引入除之前子句中提到的成员之外的所有成员。如果要给出“捕获所有”子句，则它必须出现在引入选择器列表的末尾。
   *
   * 在本节最开始给出的简单import子句可以被视为带有选择器子句的import子句的特殊简写。例如，“"import p.*”等价于“import p.{*}，而“import p.n”等价于“import p.{n”。
   *
   */




}
