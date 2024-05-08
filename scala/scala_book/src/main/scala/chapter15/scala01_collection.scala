package chapter15

object scala01_collection extends App {

  /**
   * 第15章
   * 使用其他集合类
   *
   * @Scala拥有功能丰富的集合类库。
   * 本章带你领略常用的集合类型和操作，并介绍那些最常使用的部分。《Scala高级编程》将会给出更全面的讲解，并介绍Scala如何利用其组合语法结构来提供这样丰富的API。
   */

  /**
   * 15.1 序列
   *
   * 序列类型可以用来处理依次排列分组的数据。
   * 由于元素是有次序的，因此我们可以从序列中获取第1个元素、第2个元素、第103个元素，等等。本节我们将带你了解那些最重要的序列类型。
   */

  /**
   * @列表(List)
   *
   * 也许我们需要知道的最重要的序列类型是List类，也就是我们在前一章介绍的不可变链表。
   * 列表支持在头部快速添加和移除条目，不过并不提供快速地根据下标访问的功能，因为实现这个功能需要线性地遍历列表。
   *
   * 这样的特性组合听上去可能有些奇怪，但其实对很多算法而言都非常适合。快速的头部添加和移除意味着模式匹配很顺畅（参考第13章)。
   * 而列表的不可变属性可以帮助我们开发正确、高效的算法，因为我们不需要（为了防止意外）复制列表。
   *
   * 下面是一个简短的例子，展示了如何初始化列表，并访问其头部和尾部:
   */
  val colors = List("red", "blue", "green")
  println(colors.head)
  println(colors.tail)


  /**
   * @数组(Array)
   *
   * 数组允许我们保存一个序列的元素，并使用从零开始的下标高效地访问（获取或更新)指定位置的元素值。下面是演示如何创建一个已知长度但不知道元素值的数组的例子:
   */
  val fiveInts = new Array[Int](5)
  println(fiveInts.toBuffer)

  val fiveToOne = Array(5, 4, 3, 2, 1)

  /**
   * 前面提到过，在Scala中以下标访问数组的方式是把下标放在圆括号里，而不是像Java那样放在方括号里。下面的例子同时展示了获取数组元素和更新数组元素的写法:
   */
  fiveInts(0) = fiveToOne(4)

  /**
   * Scala数组的表现形式与Java数组一致。因此，我们可以无缝地使用那些返回数组的Java方法。
   *
   * @(关于Scala和Java数组在型变上的区别，即Array[String]是否为Array[AnyRef]的子类型，会在18.3节探讨。)
   *
   * 在前面的章节中，我们已经多次看到数组在实际使用中的样子。数组的基本用法可以参考第3章的第7步。7.3节还展示了若干使用for表达式遍历数组的例子。
   */

  /**
   * @列表缓冲ListBuffer
   *  @List类支持对列表头部的快速访问，而对尾部访问则没那么高效。
   *  @因此，当我们需要向列表尾部追加元素来构建列表时，通常需要考虑反过来向头部追加元素，并在追加完成以后，调用reverse方法来获得我们想要的顺序。
   *  @另一种避免反转操作的可选方案是使用ListBuffer。ListBuffer是一个可变对象（包含在scala.collection.mutable包中)，可以帮助我们更高效地通过追加元素来构建列表。
   *  @ListBuffer提供了常量时间的向后追加和向前追加的操作。我们可以用+=操作符向后追加元素，用+=:操作符向前追加元素。
   * (＋=和+=:操作符分别是append 和 prepend的别名。)
   * 在完成列表构建以后，可以调用ListBuffer的toList方法来获取最终的列表。参考下面的例子:
   */
  import scala.collection.mutable.ListBuffer

  val buf =new ListBuffer[Int]
  println(buf+=1)
  println(buf+=2)
  println(3 +=: buf)
  println(buf.toList)

  /**
   *  @使用ListBuffer而不是List的另一个原因是防止可能出现的栈溢出。
   *  @如果我们可以通过向前追加元素来构建出预期顺序的列表，但需要的递归算法并不是尾递归的，则可以用for表达式或while循环加上ListBuffer来实现。
   * 我们将在《Scala高级编程》中介绍ListBuffer的这种用法。
   */

  /**
   * @数组缓冲ArrayBuffer
   *    @ArrayBuffer与Array很像，除了可以额外地从序列头部或尾部添加或移除元素。
   *    @所有的Array操作对于ArrayBuffer都可用，不过由于实现的包装，速度会稍慢一些。新的添加和移除操作一般而言是常量时间的，
   *    @不过偶尔会需要线性的时间，这是因为其实现需要不时地分配新的数组来保存缓冲的内容。
   *
   *    要使用ArrayBuffer，必须首先从可变集合的包引入它:
   *    在创建ArrayBuffer时，必须给出类型参数，不过并不需要指定其长度。ArrayBuffer会在需要时自动调整分配的空间:
   *    可以使用+=方法向ArrayBuffer追加元素:
   */
  import scala.collection.mutable.ArrayBuffer
  val arrayBuffer = new ArrayBuffer[Int]()
  println(arrayBuffer += 12)
  println(arrayBuffer += 15)

  /**
   * 所有常规的数组操作都是可用的。例如，可以询问ArrayBuffer的长度，或者通过下标获取元素:
   */
  println(arrayBuffer.length)
  println(arrayBuffer(0))

  /**
   * @字符串(通过StringOps)
   *    @我们需要了解的另一个序列是StringOps，它实现了很多序列方法。
   *    @Predef有一个从String到StringOps的隐式转换，可以将任何字符串当作序列来处理。参考下面的例子:
   *
   * 在本例中的hasUpperCase方法体里，我们对名称为 s 的字符串调用了exists方法。
   * 由于String类本身并没有声明任何名称为exists的方法，因此Scala编译器会隐式地将 s 转换成StringOps，而StringOps有这样一个方法。
   * exists方法将字符串当作字符的序列，当序列中存在大写字符时，这个方法将返回true。
   */
  def hasUpperCase(s:String) =s.exists(_.isUpper)
  println(hasUpperCase("Robert Frost"))
  println(hasUpperCase("e e cummings"))



}
