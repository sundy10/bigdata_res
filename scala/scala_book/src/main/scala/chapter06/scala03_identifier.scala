package chapter06

object scala03_identifier extends App {

  /**
   * 字母数字组合标识符( alphanumeric identifier)
   * 以字母或下画线开头,可以包含更多的字母、数字或下划线
   *
   *
   * 虽然下画线是合法的标识符，但是它在Scala程序中并不常用，其中一部分原因是与Java保持一致，
   * 不过另一个原因是下画线在Scala代码中还有许多其他非标识符的用法。
   * 因为上述原因，最好不使用像to_string、_init_或name_这样的标识符。
   * 字段、方法参数、局部变量和函数的驼峰命名应该以小写字母开头，
   * 如length、flatMap和s等。类和特质的驼峰命名应该以大写字母开头，如BigInt、List和口UnbalancedTreeMap等。
   *
   *
   */


  /**
   * 在常量命名上，Scala的习惯与Java不同。在Scala中，常量( constant)这个词并不仅仅意味着val。
   *
   * Java对常量的命名习惯是全大写，并用下画线分隔不同的单词，
   * 如MAX_VALUE或PI。而Scala的命名习惯是只要求首字母大写。
   * 因此，以Java风格命名的常量，如X_OFFSET，在Scala中也可以正常工作，
   * 不过Scala通常使用驼峰命名法命名常量，如XOffset。
   */
  val XOffset = 1


  /**
   * 操作标识符(operator identifier）由一个或多个操作符构成。操作符指的是那些可以被打印出来的ASCI字符，如+、:、?、~、#等。下面是一些操作标识符举例
   *
   * + ++ ::: <?> :>
   */

  1 < -2


  /**
   * 混合标识符( mixed identifier）由一个字母数字组合标识符、
   * 一个下画线和一个操作标识符组成。
   * 例如，unary_+表示+操作符的方法名，
   * myvar_=表示赋值的方法名。
   * 除此之外，形如myvar_=这样的混合标识符也被Scala编译器用来支持属性（ properties)，更多内容详见第16章。
   */

  /**
   * 字面标识符（ literal identifier）是用反引号括起来的任意字符串(`…`)。字面标识符举例如下:
   */
//  val `x` = 1
//  val `<clinit>` = 1
//  val `return` = 1
//  val `yield` = 1

  //可以将任何能被运行时接收的字符串放在反引号中，作为标识符。
  // 其结果永远是一个(合法的)Scala标识符。甚至当反引号中的名称是 Scala保留字( reserved word)时也生效。
  // 一个典型的用例是访问Java的Thread类的静态方法 yield。不能直接写Thread.yield()，因为yield是Scala的保留字。
  // 不过，仍然可以在反引号中使用这个方法名，就像这样:Thread. yield 0。
  Thread.`yield`()

}
