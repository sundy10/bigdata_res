package chapter24

import scala.collection.IndexedSeqView

object scala13_view extends App {

  /**
   * 24.13 视图
   *
   * 集合有相当多的方法用来构造新的集合，如map、filter和++。
   *
   * @我们将这些方法称作变换器(transformer)， 因为它们以接收者对象的形式接收至少一个集合入参并生成另一个集合作为结果。
   */

  /**
   * 变换器可以通过两种主要的方式实现:严格的和非严格的（或称为惰性的)。
   * 严格的变换器会构造出带有所有元素的新集合。而非严格的（或称为惰性的）变换器只是构造出结果集合的一个代理，其元素会按需被构造出来。
   *
   * 作为非严格的变换器的示例，考虑下面这个惰性映射操作的实现:
   */
  def lazyMap[T, U](col: Iterable[T], f: T => U) =
    new Iterable[U] :
      def iterator = col.iterator.map(f)

  /**
   * 注意，lazyMap在构造新的Iterable时并不会遍历给定集合col的所有元素。
   * 给出的f涵数只会在新集合的iterator元素被需要时才会被应用。
   */

  /**
   * Scala集合默认在其所有变换器操作中都是严格的，除了LazyList,LazyList将所有变换器方法实现成了惰性求值的版本。
   * 不过，有一种系统化的方式可以将每个集合转换成惰性的版本，或者反过来，这种方式的基础是集合视图。
   * 视图(view）是一种特殊的集合，它代表了某个基础集合，但是是采用惰性的方式实现所有变换器的。
   */

  /**
   * @要从集合得到它的视图，你可以对集合使用view方法。
   * 如果xs是一个集合，xs.view就是同一个集合，但是所有变换器都是按惰性的方式实现的。
   * 而要从视图得到严格版本的集合，你可以调用to方法，传入一个严格的集合工厂方法作为参数即可。
   */
  val v = Vector((1 to 10) *) // *展开成了单个元素
  println(v.map(_ + 1).map(_ * 2))

  /**
   * 在最后这条语句中，表达式v map (_+1)首先构造出一个新的向量，然后通过第二次的map (_* 2)调用变换成第三个向量。
   *
   * @在很多情况下，首次map调用构造出来的中间结果有些浪费。在一个假想的示例中，将两个函数(_＋1)和(_*2)组合在一起执行一次map操作会更快。
   * 如果你能同时访问这两个函数，则可以手动实现。不过，在通常情况下，对某个数据结构的连续变换发生在不同的程序模块中。
   * 将这些变换融合在一起会打破模块化的设计。避免中间结果的更一般的方式是首先将向量转换成一个视图，然后对视图应用所有的变换，最后将视图强制转换为向量:
   */
  println(v.view.map(_ + 1).map(_ * 2).toVector)

  /**
   * 我们将再一次逐个完成这一系列的操作:
   */
  val vv = v.view //scala.collection.IndexedSeqView[Int]

  /**
   * 通过vv.view调用，你将得到一个IndexedSeqView，即一个惰性求值的IndexedSeq。
   * 与LazyList一样，对视图调用toString方法并不会强行计算视图的元素。这就是w的元素被显示为not computed的原因。
   *
   * 对视图应用首个map，将得到:
   */
  val view1: IndexedSeqView[Int] = vv.map(_ + 1)
  println(view1) //IndexedSeqView(<not computed>)

  /**
   * 这次map的结果是另一个IndexedSeqView[Int]值。
   * 从本质上讲，这个值记录了这样一个事实:我们需要对向量v应用一个函数(_+1)。
   * 在视图被强制转换之前，这个函数映射并不会被应用。接下来将对上面的结果应用第二个map。
   */
  val view2: IndexedSeqView[Int] = view1.map(_ * 2)
  println(view2) //IndexedSeqView(<not computed>)

  //最后，对上面的结果进行虽制转换，会给出:
  println(view2.toVector) //Vector(4, 6, 8, 10, 12, 14, 16, 18, 20, 22)

  /**
   * 作为to操作的一部分，两个被保存的函数 (_+1)和(_*2)得以被应用，新的向量被构造出来。
   * @通过这种方式，我们并不需要中间的数据结构。
   *
   * 对视图应用变换操作并不会构建出新的数据结构，只会返回一个lterable，其迭代器是将变换操作应用到底层集合后的那个迭代器。
   */

  /**
   * 考虑采用视图的主要原因是性能。你已经看到，将集合切换成视图可以避免中间结果的产生。
   * 这样节约下来的开销可能非常重要。我们再来看一个例子，从一个单词列表中找到第一个回文( palindrome)。
   * 所谓的回文，指的是正读和反读都一样的单词。回文必要的定义如下:
   */
  def isPalindrome(x:String) = x == x.reverse
  def findPalindrome(s:Iterable[String]) = s.find(isPalindrome)

  /**
   * 接下来，假设你有一个非常长的序列words，而你想从该序列的前100万个单词中找到一个回文。
   * 你能重用findPalindrome方法的定义吗?当然，你可以这样写:
   *
   *    findPalindrome(words.take(1000000))
   */

  /**
   * 这很好地分离了获取序列中前100万个单词和找到其中的回文这两件事。
   * 不过这种做法的缺点是，它总是会构造出一个中间的、由100万个单词组成的序列，哪怕这个序列的首个单词就已经是回文了。
   * 因此，可能有999 999个单词被复制到中间结果中，但在这之后又完全不会被用到。
   * 许多程序员在这一步可能就放弃了，转而编写他们自己的、特殊化的、从某个给定的入参序列的前缀中查找回文的版本。
   * 不过，如果你使用视图，则并不需要费那么大的劲，只需要简单地写:
   *
   *    findPalindrome(words.view.take(1000000))
   */

  /**
   * 这个写法有着相同的对不同问题的分离属性，不过它并不会构造100万个元素的序列，而是会构造一个轻量的视图对象。这样一来，你并不需要在性能和模块化之间做取舍。
   *
   * 了解这么多视图的使用方法后，你可能会好奇，(既然视图那么好)为什么还要有严格求值的集合呢?
   *    @原因之一是，性能的比较结果并非总是偏向惰性求值的集合。对小型的集合而言，组织视图和应用闭包的额外开销通常大于免去中间数据结构的收益。
   *    @或许更重要的一个原因是，如果延迟的操作有副作用，则对视图的求值可能会变得非常令人困惑。
   *
   * 这里有一个例子，可能让Scala 2.8之前版本的一些用户吃到了一些“苦头”。
   * 在之前的版本中，Range类型是惰性的，因此其行为从效果上讲与视图很像。人们试着像这样创建 actor:
   * (Scala的actor类库被废弃了，不过这个经典的例子依然值得参考。)
   *
   *  val actors = for i <- 1 to 10 yield actor{ ??? }
   *
   *  让他们倍感意外的是，在这之后并没有actor被执行，尽管actor应该从后面花括号中的代码创建并启动。
   *  那么，为什么没有actor被执行呢?回顾一下，for表达式等效于map方法的应用:
   *
   *  val actors = (1 to 10).map(i=> actor{ ??? })
   *
   * 由于在之前版本中，(1 to 10)产生的区间从行为上类似于视图，因此map的结果依然是视图。
   * 也就是说，并没有元素被计算出来，因此也就没有actor被创建。
   * 如果我们对整个表达式的区间做一次强制转换，actor应该就能被创建出来，不过这个要求相当不直观。
   *
   * 为了避免类似的“惊喜”，Scala类库从2.8版本开始采纳了更常规的规则。除流之外的所有集合都是严格求值的。
   * 从严格求值的集合到惰性求值的集合的唯一方式是使用view方法。反向的唯一方式是使用to方法。
   * 因此在Scala 2.8中，上述代码中的actors定义的行为会按照预期的那样创建并启动10个actor。
   * 如果你想重新得到之前那个令人意外的行为，则可以显式地添加一个view方法的调用来模拟:
   *
   *   val actors = for i <- (1 to 10).view yield actor{ ??? }
   */

  /**
   * 总的来说，视图是一个用于调和效率与模块化之间的矛盾的强大工具。
   * @不过，为了避免被延迟求值的各种细节纠缠，你应该将视图的使用局限在两种场景。
   *    @要么在集合变换没有副作用的纯函数式的代码中应用视图，
   *    @要么对所有修改都是显式执行的可变集合使用视图。最好避免在既创建新的集合又有副作用的场景下混用视图和各种集合操作。
   */







}