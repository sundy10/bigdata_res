package chapter08

object scala03_first_class_function extends App {

  /**
   * Scala支持一等函数（first-class function)。
   * 我们不仅可以定义函数并调用它，还可以用匿名的字面量来编写函数并将其作为值进行传递。
   * 第2章介绍了函数字面量，并在图2.2(33页)中展示了基本的语法。
   */


  /**
   * @函数字面量被编译成类，并在运行时实例化成函数值(functionvalue)。
   * @因此，函数字面量和函数值的区别在于，函数字面量存在于源码，而函数值以对象形式存在于运行时。这与类（源码）和对象(运行时)的区别很相似。
   *
   * (scala中的函数会编译成java中类的形式存在)
   */

  //=>表示该函数将左侧的内容（任何整数x）转换成右侧的内容(x+ 1)。因此，这是一个将任何整数x映射成x+1的函数。
  //(x: Int) => x + 1

  //函数值是对象，所以可以将它存放在变量中。函数值也是函数，所以也可以用常规的圆括号来调用它。下面是对这两种操作的示例:
  val increase = (x: Int) => x + 1

  println(increase(10))


  /**
   * 如果想要在函数字面量中包含多于1条语句，则可以将函数体用花括号括起来，使每条语句占一行，组成一个代码块（block)。
   * 像方法一样，当函数值被调用时，所有的语句都会被执行，并且该函数的返回值就是对最后一个表达式求值的结果。
   *
   * 每个函数值都是某个扩展自scala包的FunctionN系列中的一个特质的类的实例，
   * 比如，Function0表示不带参数的函数，Function1表示带一个参数的函数，等等。
   *
   * 每一个FunctionN特质都有一个apply方法 用来调用该函数。
   */

  val addTwo = (x: Int) =>
    val increment = 2
    x + increment

  println(addTwo(10))


  /**
   * 现在你已经看到了函数字面量和函数值的细节和用法。
   * 很多Scala类库都让你有机会使用它们。
   *
   * 例如，所有的集合类都提供了foreach方法。
   * 它接收一个函数作为入参，并对它的每个元素调用这个函数。下面是使用该方法打印列表所有元素的例子:
   *
   * (foreach方法定义在lterable特质中，这是Lits、Set、Array和Map的通用超特质。详情请参考第15章。)
   */

  val someNumbers = List(-11, -10, -5, 0, 5, 10)

  someNumbers.foreach((x:Int)=>println(x))


  /**
   * 再举一个例子，集合类型还有一个filter方法。
   * 这个方法可以从集合中选出那些满足由调用方指定的条件的元素。这个指定的条件由函数表示。
   * 例如，(x: Int) => x>0这个函数可以被用来做过滤。这个函数将所有正整数映射为true，而将所有其他整数映射为false。下面是filter方法的具体用法:
   */
  println(someNumbers.filter((x:Int)=>x>0))




}
