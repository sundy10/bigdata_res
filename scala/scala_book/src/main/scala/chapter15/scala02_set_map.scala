package chapter15

import scala.collection.mutable.ListBuffer

object scala02_set_map extends App {

  /**
   * 15.2 集和映射
   *
   * 我们在前面的章节（从第3章的第10步开始)已经了解了集和映射的基础。本节将提供更多关于集和映射用法的内容，并给出更多的示例。
   *
   * 像前面提到的，Scala集合类库同时提供了可变和不可变两个版本的集和映射。
   * 图(23.1.png)给出了集的类继承关系，图(Iterable1.png)给出了映射的类继承关系。
   * 如这两张图所示，Set和口Map这样的名称各作为特质出现了3次，分别在不同的包中。
   *
   * 当我们写下Set或Map时，默认得到的是一个不可变的对象。如果我们想要的是可变的版本，则需要显式地做一次引入。
   * Scala让我们更容易访问到不可变的版本，这是鼓励我们尽量使用不可变的集合。
   * 这样的访问便利是通过Predef对象完成的，这个对象的内容在每个Scala源文件中都会被隐式地引入。示例15.1给出了相关的定义。
   *
   * Predef利用type关键字定义了Set和Map这两个别名，分别对应不可变的集和不可变的映射的完整名称。(关于type关键字的更多细节 将在20.6节详细介绍。)
   * 名称为Set 和 Map 的 val被初始化成指向不可变Set口Map的单例对象。
   * 因此Map等同于Predef.Map,而Predef.Map又等同于scala.collection.immutable.Map。这一点对于Map类型和Map对象都成立。
   * (示例15.1 Predef中的默认映射和集定义   img_2.png)
   * Map == Predef.Map == scala.collection.immutable.Map
   */

  /**
   * 可以继续用Set来表示不可变集，就像以前一样，不过现在可以用mutable.Set来表示可变集。参考下面的例子:
   */

  import scala.collection.*

  val mutaSet = mutable.Set(1, 2, 3)

  /**
   * @使用集Set
   * @集的关键特征是它会确保同一时刻，以==为标准，集里的每个对象都最多出现一次。作为示例，我们将用一个集来统计某个字符串中不同的单词的个数
   * @如果我们将空格和标点符号作为分隔符给出，则String的split方法可以帮我们将字符串切分成单词。
   * @“[!,.]+”这样的正则表达式就够了:它表示给定的字符串需要在有一个或多个空格、标点符号的地方切开。
   */
  val text = "See.Spot.run.Run,Spot.Run!"
  val wordsArray = text.split("[!,.]+")
  println(wordsArray.toBuffer)

  /**
   * 要统计不同单词的个数，可以将它们统一转换成大写或小写形式，然后将它们添加到一个集中。由于集会自动排除重复项，因此每个不同的单词都会在集里仅出现一次。
   * 首先，可以用Set伴生对象的empty方法创建一个空集:
   */
  val words = mutable.Set.empty[String]

  /**
   * 然后，只需要用for表达式遍历单词，将每个单词转换成小写形式，并用+=操作符将它添加到可变集即可:
   * 这样就能得出结论:给定文本包含3个不同的单词，即spot、run和see。
   */
  for word <- wordsArray do
    words += word.toLowerCase
  println(words)

  /**
   * @常用的可变集和不可变集操作如img_3所示。
   */
  val nums = Set(1, 2, 3)
  println(nums + 5) //添加一个元素  Set(1, 2, 3, 5)
  println(nums - 3) //移除一个元素  Set(1, 2)
  println(nums ++ List(5, 6)) //添加多个元素  HashSet(5, 1, 6, 2, 3)
  println(nums -- List(1, 2)) //移除多个元素  Set(3)
  println(nums & Set(1, 3, 5, 7)) //获取二个集的交集 Set(1, 3)
  println(nums.size) //返回集的大小  3
  println(nums.contains(3)) //检查是否被包含   true

  import scala.collection.mutable //引入可变集合

  val words1 = mutable.Set.empty[String] //创建一个空的可变集   HashSet()
  words1 += "the" //添加一个元素
  words1 -= "the2" //移除一个元素,如果这个元素存在
  words1 ++= List("do", "re", "mi") //添加多个元素
  words1 --= List("do", "re") //移除多个元素
  words.clear //移除所有元素


  /**
   * @使用映射Map
   *
   * 映射让我们可以对某个集的每个元素都关联一个值。使用映射看上去与使用数组很像，只不过我们不再使用从0开始的整数下标来索引它，而是使用任何键来索引它。
   * 如果我们引入了mutable这个包名，就可以像这样创建一个空的可变映射:
   */
  val map = mutable.Map.empty[String, Int]

  /**
   * 注意，在创建映射时，必须给出两个类型。第一个类型是针对映射的键（key),而第二个类型是针对映射的值(value)。
   * 在本例中，键是字符串，而值是整数。在映射中设置条目看上去与在数组中设置条目类似:
   */
  map("hello") = 1
  map("there") = 2
  println(map) //HashMap(there -> 2, hello -> 1)

  /**
   * 下面是一个统计每个单词在字符串中出现的次数的方法:
   */
  def countWords(text: String) =
    val counts = mutable.Map.empty[String, Int]
    for rawWord <- text.split("[ ,!.]") do
      val word = rawWord.toLowerCase()
      val oldCount = if counts.contains(word) then counts(word) else 0
      counts += (word -> (oldCount + 1))
    counts

  println(countWords("See Spot run!Run,Spot.Run!"))

  /**
   * 这段代码的主要逻辑是:一个名称为counts的可变映射将每个单词映射到它在文本中出现的次数，
   * 并且对于给定文本的每一个单词,这个单词对应的原次数被查出，然后加1，新的次数又再次被存回counts。
   * 注意，这里我们用contains来检查某个单词是否已经出现过。如果counts.contains (word)不为true，则这个单词就还没有出现过，我们在后续计算中采用的次数就是0。
   */

  /**
   * @常用的映射操作 img_4.png
   */
  val nums1 = Map("i" -> 1, "ii" -> 2) //创建一个不可变映射
  nums1 + ("vi" -> 6) //添加一个条目
  nums1 - "ii" //移除一个条目
  nums1 ++ List("iii" -> 3, "v" -> 5) //添加多个条目
  nums1 -- List("i", "ii") //移除多个条目
  nums1.size //返回映射的大小
  nums1.contains("ii") //检查是否包含
  nums1("ii") //获取指定键的值，如果不存在 则报ava.util.NoSuchElementException: key not found
  nums1.keys //获取所有的键 的迭代器
  nums1.keySet //以集的形式返回所有的键
  nums1.values //返回所有的值 的迭代器
  nums1.isEmpty //标识映射是否为空

  val words2 = mutable.Map.empty[String, Int]
  words2 += ("one" -> 1) //添加一个条目
  words2 -= ("one") //添加一个条目
  words2 ++= List("one" -> 1, "two" -> 2, "three" -> 3) //添加多个条目
  words2 --= List("one", "two")

  /**
   * @默认的集和映射
   *
   * 对于大部分使用场景，由Set() scala.collection.mutable.Map()等工厂方法提供的可变和不可变的集或映射的实现通常都够用了。
   * 这些工厂方法提供的实现使用快速的查找算法，通常会用到哈希表，因此可以很快判断出某个对象是否在集中。
   *
   * 举例来说, scala.collection.mutable.Set()工厂方法返回一个scala.collection.mutable.HashSet，并在内部使用了哈希表。
   * 同理，scala. collection.mutable.Map()工厂方法返回的是一个scala.collection.mutable.HashMap.
   *
   * 对不可变的集和映射而言，情况要稍微复杂一些。
   * @举例来说,scala.collection.immutable.Set()工厂方法返回的类取决于我们传入了多少元素，
   * @如(img_5.png)所示。对于少于5个元素的集，有专门的、特定大小的类与之对应，以此来达到最佳性能。
   * @一旦我们要求一个大于或等于5个元素的集，这个工厂方法将返回一个使用哈希字典树的实现。
   *
   * 同理,scala.collection.immutable.Map()工厂方法会根据我们传给它多少键/值对来决定返回什么类的实现，如表 (img_6.png) 所示。
   * 与集类似，对于少于5个元素的不可变映射，会有一个特定的、固定大小的映射与之对应，以此来达到最佳性能。
   * 而一旦映射中的键/值对个数达到或超过5个，则会使用不可变的HashMap。
   *
   * @默认不可变实现类能够带给我们最佳的性能举例来说，如果我们添加一个元素到EmptySet中，将会得到一个Set1;如果我们添加一个元素到Set1中，将会得到一个Set2。
   * @如果这时我们再从Set2移除一个元素，将会得到另一个Set1。
   */

  /**
   * @排好序的集和映射
   *
   * 有时我们可能需要一个迭代器按照特定顺序返回元素的集或映射。
   * 基于此,Scala集合类库提供了SortedSet和SortedMap特质。
   * 这些特质被TreeSet和TreeMap类实现，这些实现用红黑树来保持元素(对TreeSet类而言)或键(对TreeMap类而言）的顺序。
   * 具体顺序由Ordered特质决定，集的元素类型或映射的键的类型都必须混入或被隐式转换成Ordered特质。这两个类只有不可变的版本。下面是TreeSet类的例子:
   */
  import scala.collection,immutable.TreeSet
  val ts = TreeSet(9,3,1,8,0,2,7,4,6,5)
  val cs = TreeSet('f','u','u')
  println(ts)
  println(cs)

  //下面是TreeMap类的例子
  import scala.collection.immutable.TreeMap
  var tm  = TreeMap(3->'x',1->'x',4->'x')
  println(tm)


}
