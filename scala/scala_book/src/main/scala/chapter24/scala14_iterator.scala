package chapter24

object scala14_iterator extends App {

  /**
   * 24.14 迭代器
   *
   * @迭代器并不是集合，而是逐个访问集合元素的一种方式。
   * 迭代器it的两个基本方法是next和口hasNext。
   * 对next方法的调用会返回迭代器的下一个元素并将迭代器的状态向前推进一步。
   * 对同一个迭代器再次调用next方法会交出在前一个返回元素的基础上更进一步的元素。
   * 如果没有更多的元素可以返回，则对next方法的调用会抛出NoSuchElemenException。
   * 你可以用迭代器的hasNext方法来获知是否还有更多的元素可以返回。
   *
   * “遍历”迭代器的所有元素的最直接的方式是通过while循环:
   */
  def it: Iterator[Int] = List(1, 2, 3).iterator

  val iterator = it
  while iterator.hasNext do
    println(iterator.next())


  /**
   * Scala的迭代器还提供了lterable和Seq特质中的大部分方法。例如，它提供了foreach方法，用来对迭代器返回的每个元素执行给定的过程。
   * 通过foreach方法，上述的循环可以被简写为:
   */
  it.foreach(println)

  /**
   * 像往常一样,我们也可以用for表达式来表达涉及foreach、map、filter和flatMap方法的表达式，因此打印迭代器返回的所有元素还有一种方式:
   */
  for elem <- it do println(elem)

  /**
   * 迭代器的foreach方法和可迭代集合的同名方法有一个重要的区别:对迭代器调用foreach方法，该方法在执行完之后会将迭代器留在末端。
   * 因此对相同的迭代器再次调用next方法会抛出NoSuchElementException。
   *
   * 而对集合调用foreach方法，该方法会保持集合中的元素数量不变（除非传入的函数会添加或移除元素，不过并不鼓励这样做，因为这样做可能会带来令人意外的结果)。
   */

  /**
   * 迭代器的其他与可迭代集合相同的操作也有这个属性:在执行完成后会将迭代器留在未端。例如，迭代器提供了map方法，用于返回一个新的迭代器:
   * (迭代器的map方法是惰性的)
   */
  val it1 = Iterator("a", "number", "of", "words")
  val lit = it1.map(f => {
    f.length
  })
  println(it1.hasNext) //true
  lit.foreach(println)
  println(it1.hasNext) //false

  /**
   * 正如你所看到的，在map方法调用完成后，it迭代器被推进到了末端。
   * 另一个例子是dropWhile方法，可以用来查找迭代器中首个满足某种条件的元素。例如，为了找到前面那个迭代器中至少有两个字符的单词，可以这样写:
   * (迭代器的dropWhile方法是惰性的)
   */
  val it2 = Iterator("a", "number", "of", "words")
  val dit = it2.dropWhile(_.length < 2)
  println(dit.next())
  println(it2.next())

  /**
   * 再次注意，it2迭代器在dropWhile方法的调用中被修改了:现在指向的是列表中的第二个单词“number”。
   *
   * 只有一个标准操作duplicate允许你重用同一个迭代器:
   */
  val (it3,it4) = it2.duplicate

  /**
   * 对duplicate的调用会涉及两个迭代器，每一个都会返回与it迭代器完全相同的元素。
   * 这两个迭代器相互独立:推进其中一个迭代器并不会影响另一个迭代器。
   * @而原始的it迭代器在duplicate调用后被推进到了未端，因此不再可用了。
   */

  /**
   * 总的来说，如果你在调用了迭代器的方法后就不再访问它的话，迭代器的行为与集合很像。
   * Scala集合类库将这个属性显式地表示为一个名称为lterableOnce的抽象对象，这是lterable和lterator的公共超特质。
   * 正如其名称所示，lterableOnce对象可以用foreach方法来遍历，不过在遍历后并没有规定该对象的状态。
   * 如果lterableOnce对象事实上是一个迭代器，则在遍历完成后，它将位于未端，而如果它是可迭代集合，则在遍历完成后，它将保持原样。
   * lterableOnce的一个常见用例是作为既可以接收迭代器也可以接收可遍历集合的方法的入参类型声明。
   * 比如，lterable特质的++方法。它接收一个lterableOnce参数，因此可以追加来自迭代器或者可遍历集合的元素。
   *
   * Iterator特质的所有操作汇总在表Iterator[1-4].png中。
   */


  /**
   * @带缓冲的迭代器
   *
   * 有时候,你想要一个可以“向前看”的迭代器，这样就可以检查下一个要返回的元素但并不向前推进。
   * 例如，考虑这样一个场景，你需要从一个返回字符串序列的迭代器中跳过前面的空字符串。可能会尝试这样来实现:
   */
  //这并不可行
  def skipEmptyWordsNOT(it:Iterator[String]) =
    while it.next().isEmpty do{}

  /**
   * 不过更仔细地看这段代码，它的逻辑是有问题的:它的确会跳过前面的空字符串,不过同时也跳过了第一个非空的字符串。
   * 这个问题的解决方案是使用带缓冲的迭代器，即Bufferedlterator特质的实例。Bufferedlterator特质是lterator特质的子特质，
   * 提供了一个额外的head方法。对一个带缓冲的迭代器调用head方法将返回它的第一个元素，不过并不会推进迭代器到下一步。
   * 使用带缓冲的迭代器，跳过空字符串的逻辑可以这样写:
   */
  def skipEmptyWords(it:BufferedIterator[String]) =
    while it.head.isEmpty do it.next()

  /**
   * 每个迭代器都可以被转换成带缓冲的迭代器，方法是调用其buffered方法。参考下面的例子:
   */
  val iterator1 = Iterator(1,2,3,4)
  val bit: BufferedIterator[Int] = iterator1.buffered
  println(bit.head)
  println(bit.head)
  println(bit.next())
  println(bit.head)
  println(bit.head)






}
