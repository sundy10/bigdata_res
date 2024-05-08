package chapter14

object scala07_high_order_method extends App {

  /**
   * 14.7 List类的高阶方法
   *
   * 许多对列表的操作都有相似的模式,有一些模式反复出现。
   * 例如:以某种方式对列表中的每个元素做转换，验证是否列表中所有元素都满足某种属性，
   * 从列表元素中提取满足某个指定条件的元素，或者使用某种操作符来组合列表中的元素。
   * 在Java中，这些模式通常需要通过固定写法的for循环或while循环来组装。
   * 而Scala允许我们使用高阶操作符来更精简、更直接地表达，并且通过List类的高阶方法实现这些高阶操作。本节我们将对这些高阶方法进行探讨。
   *
   * （这里我们所说的“高阶操作符”(high-order operator)指的是用在操作符表示法中的高阶函数。我们在9.1节提到过，如果一个函数接收一个或多个函数作为参数，它就是高阶的）
   */

  /**
   * @对列表作映射:map、flatMap和foreach方法
   *
   * xs map f这个操作将类型为List[T]的列表xs和类型为T=> U的函数f作为操作元，返回一个通过应用f到xs的每个元素后得到的列表。
   * 例如:
   */
  println(List(1, 2, 3).map(_ + 1))
  val words = List("the", "quick", "brown", "fox")
  println(words.map(_.length))
  println(words.map(_.toList.reverse.mkString))

  /**
   * flatMap方法与map方法类似，不过它要求右侧的操作元是一个返回元素列表的函数。
   * 它将这个函数应用到列表的每个元素上，然后将所有结果拼接起来返回。下面的例子展示了map和和flatMap方法的区别:
   */
  println(words.map(_.toList))
  println(words.flatMap(_.toList))

  /**
   * 可以看到，map方法返回的是列表的列表，而flatMap方法返回的是所有元素拼接起来的单个列表。
   *
   * 下面这个表达式也体现了map和flatMap方法的区别与联系，这个表达式构建的是一个满足 1<=j<i<5 的所有对偶(i, j):
   */

  println(List.range(1, 5).flatMap(i => List.range(1, i).map(j => (i, j))))

  /**
   * List. range是一个用来创建某个区间内所有整数的列表的工具方法。
   * 在本例中，我们用到了两次range方法:一次是生成从1(含）到5(不含)的整数列表;
   * 另一次是生成从1到i的整数列表，其中i是来自第一个列表的每个元素。
   * 表达式中的map方法生成的是一个由元组(i. j)组成的列表，其中j<i。
   * 外围的flatMap方法对1到5之间的每个i生成一个列表，并将结果拼接起来。也可以用for表达式来构建同样的列表:
   */
  for i <- List.range(1, 5); j <- List.range(1, i) yield (i, j)

  /**
   * 你可以通过《Scala高级编程》了解到更多关于for表达式和列表操作的内容。
   *
   * 第三个映射类的操作方法是foreach。不同于map和flatMap方法，foreach方法要求右操作元是一个过程(结果类型为Unit的函数)。
   * 它只是简单地将过程应用到列表中的每个元素上。整个操作本身的结果类型也是Unit，
   * 并没有列表类型的结果被组装出来。参考下面这个精简的、将列表中所有数值加和的例子:
   */
  var sum = 0
  List(1, 2, 3, 4, 5).foreach(sum += _)
  println(sum)

  /**
   * 过滤列表:filter、
   *
   * @partition、
   * @find、
   * @takeWhile、
   * @dropWhile、
   * @span方法
   *
   * xs filter p这个操作将类型为List[T]的xs和类型为 T=> Boolean 的前提条件 p作为 操作元，将交出xs中所有p(x)为true的元素x。例如:
   */
  println(List(1, 2, 3, 4, 5).filter(_ % 2 == 0))
  println(words.filter(_.length == 3))

  /**
   * partition方法与filter方法很像，不过它返回的是一对列表。
   * 其中一个列表包含所有前提条件为true的元素，另一个列表包含所有前提条件为false的元素。它满足如下等式:
   *
   * xs.partition(p) = (xs.filter(p),xs.filter(!p(_)))
   * 参考下面例子:
   */
  println(List(1, 2, 3, 4, 5).partition(_ % 2 == 0))

  /**
   * find方法与filter方法也很像，不过它返回满足给定前提条件的第一个元素，而不是所有元素。
   * xs find p这个操作接收列表xs和前提条件p两个操作元，返回一个可选值。如果xs中存在一个元素x满足p(X)为true，就返回Some(x)。
   * 而如果对所有元素而言，前提条件p都为false，就返回None。下面来看一些例子:
   */
  println(List(1, 2, 3, 4, 5).find(_ % 2 == 0))
  println(List(1, 2, 3, 4, 5).find(_ <= 0))

  /**
   * takeWhile和dropWhile方法也将一个前提条件作为右操作元。
   * xs StakeWhile p操作返回 列表xs中连续满足前提条件p的最长前缀。
   * 同理，xs dropWhile p操作将去除列表xs中连续满足前提条件p的最长前缀。下面来看一些例子:
   */
  println(List(1, 2, 3, -4, 5).takeWhile(_ > 0))
  println(words.dropWhile(_.startsWith("t")))

  /**
   * span方法将takeWhile和dropWhile两个方法合二为一，就像splitAt方法将take和drop方法合二为——样。它返回一对列表，满足如下等式:
   * (线性 二者互补原则)
   *
   * xs span p = (xs takeWhile p, xs dropWhile p)
   * 与splitAt方法一样，span方法同样不会重复遍历xs:
   */
  println(List(1, 2, 3, -4, 5).span(_ > 0))


  /**
   * 对列表的前提条件检查:
   *
   * @forall和 (任意一个)
   * @exists方法 (存在一个)
   *
   *           xs forall p这个操作接收一个列表xs和一个前提 条件 p作为入参。
   *           如果列表中所有元素都满足前提条件p，就返回true。
   *           与此相反，xs exists p 操作返回true的要求是xs中存在一个元素满足前提条件p。
   *           例如，要弄清楚一个以列表的列表表示的矩阵中是否存在一行的元素全为零，代码如下:
   */
  def hasZeroRow(m: List[List[Int]]) =
    m.exists(row => row.forall(_ == 0))

  /**
   * 折叠列表:
   *
   * @foldLeft
   * @foldRight方法
   *
   * 对列表的另一种常见操作是用某种操作符 合并元素。例如:
   *
   * sum(List(a, b, c)) = 0 + a + b + c
   * 这是二个折叠操作的特例:
   */
  def sum(xs: List[Int]): Int = xs.foldLeft(0)(_ + _)

  def product(xs: List[Int]): Int = xs.foldLeft(1)(_ * _)

  println(sum(List(1, 2, 3, 4)))
  println(product(List(1, 2, 3, 4)))

  /**
   * (z foldLeft xs)(op)这个操作涉及3个对象: 起始值z、列表xs 和 二元操作op。折叠的结果是以z为前缀，对列表的元素依次连续应用op。例如:
   *
   * List(a, b, c).foldLeft(z)(op) = op(op(op(z, a), b), c)
   * 或者用图形化表示就是: img_3.png
   *
   * 还有一个例子可以说明foldLeft方法的用法。为了把列表中的字符串表示的单词拼接起来,在其中间和最前面加上空格，可以:
   */
  println(words.tail.foldLeft(words.head)(_ + " " + _)) //the quick brown fox

  /**
   * foldLeft方法产生一棵向左靠的操作树。同理，foldRight方法产生—棵向右靠的操作树。例如:
   *
   * List(a, b, c).foldRight(z)(op) = op(a, op(b,op(c, z)))
   * 或者用图形化表示就是: img_5.png
   */

  /**
   * @对满足结合律的操作而言，左折叠和右折叠是等效的，不过可能存在执行效率上的差异。
   * @我们可以设想一下flatten方法对应的操作，这个操作是将一个列表的列表中的所有元素拼接起来。可以用左折叠也可以用右折叠来完成:
   */
  def flattenLeft[T](xss: List[List[T]]) =
    xss.foldLeft(List[T]())(_ ::: _)

  def flattenRight[T](xss: List[List[T]]) =
    xss.foldRight(List[T]())(_ ::: _)

  /**
   * 由于列表拼接xs ::: ys的执行时间与首个入参xs的长度成正比，用右折叠的flattenRight比用左折叠的flattenLeft更高效。
   * 左折叠在这里的问题是flattenLeft(xss)需要复制首个元素列表（xss.head) n-1次，其中n为列表xss的长度。(左折叠前部越拼越多n越来越大)
   * 注意，上述两个flatten版本都需要对表示折叠起始值的空列表做类型注解。
   * 这是由于Scala类型推断程序存在的一个局限:不能自动推断出正确的列表类型。如果我们漏掉了类型注解，不能通过编译
   *
   * 要弄清楚为什么类型推断程序出了问题，需要了解折叠方法的类型，以及它是如何实现的。这个留到14.10节再探讨。
   */

  /**
   * 例子:用fold方法反转列表
   *
   * 在本章前面的部分，我们看到了reverse方法的实现，其名称为rev，
   *
   * @运行时间是待反转列表长度的平方级。现在来看一个reverse方法的不同实现，其运行开销是线性的，原理是基于下面的机制来做左折叠:
   *
   * def reverseLeft[T](xs:List[T]) =
   * xs.foldLeft("startvalue")("operation")
   */

  /**
   * 剩下需要补全的就是startvalue（起始值)和operation(操作)的部分了。事实上，可以用更简单的例子来推导。为了推导出startvalue正确的取值，我们可以用最简单的列表List()开始:
   *
   * 因此,startvalue必须是List()。要推导出第二个操作元，可以以仅次于List() 的最小列表作为样例。我们已经知道startvalue是List(),可以做如下的演算:
   *
   * 因此, operation(List(), x)等于List(x)，而List(x)也可以写作 x :: List()。
   * 这样我们就发现，可以基于::操作符把两个操作元反转一下以得到operation。(这个操作有时被称作“snoc”，即把::的“cons”反过来念。)于是我们得到如下reverseLeft方法的实现:
   */
  def reverseLeft[T](xs: List[T]) =
    xs.foldLeft(List[T]()) { (ys, y) => y :: ys }
  println(reverseLeft(List(1,2,3)))

  /**
   * 同样地，为了让类型推断程序正常工作，这里的类型注解List[T]()是必需的。
   * 如果我们分析reverseLeft方法的时间复杂度，就会发现它执行这个常量时间操作（即“snoc") n次。因此,reverseLeft方法的时间复杂度是线性的。
   */

  /**
   * @列表排序:sortWth方法
   *
   * xs sortWth before这个操作会对列表xs中的元素进行排序，
   * @其中xs是列表，而before是一个用来比较两个元素的函数。
   * 表达式x before y 对于在预期的排序中x应出现在y之前的情况应返回true。例如:
   */
  println(List(1,-3,4,2,6).sortWith(_<_))
  println(words.sortWith(_<_))

  /**
   * 注意,sortWth方法执行的是与前一节的msort函数类似的归并排序。不过sortWith方法是List类的方法，而msort函数定义在列表之外。
   */
}
