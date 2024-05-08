package chapter24

object scala06_Map extends App {

  /**
   * 24.6 映射
   *
   * 映射是由键/值对组成的lterable特质（也被称为映射关系或关联)。
   *
   * @Scala的Predef类提供了一个隐式转换， 让我们可以用key -> value这样的写法来表示(key, value)这个对偶。
   *                          因此，Map("x" -> 24,"y" ->25, "z" ->26)与Map(("kx",24),("y"25),("z"，26))的含义完全相同，但更易读。
   */

  /**
   * 映射的基本操作（参考表24.8)与集的操作类似。不可变映射支持额外的通过返回新的映射来添加和移除关联的操作（参考表24.9)。
   * 可变映射提供更多额外的操作支持（参考表24.10)。
   *
   * 映射操作归类如下。
   */

  /**
   * @查找操作
   *
   * apply、get、getOrElse、contains和isDefinedAt方法。这些方法将映射转换成从键到值的偏函数。映射基本的查找操作如下:
   *
   * def get(key):Option[Value]
   *
   * “m.get(key)”这个操作首先测试该映射是否包含了指定键的关联，如果是，则以Some的形式返回关联的值;
   * 而如果在映射中并没有定义这个键，则返回None。
   * @映射还定义了一个直接返回指定键关联的值（不包含在Option中）的apply方法。如果指定键在映射中没有定义，则会抛出异常。
   */

  /**
   * @添加和更新操作
   *
   * + (别名updated)  ++ (别 名concat)、updateWith 和 updatedWith方法用于对映射添加新的绑定或改变已有的绑定。
   */

  /**
   * @移除操作
   *
   * -(别名removed）和 --(别名removedAll)方法用于从映射移除绑定。
   */

  /**
   * @产生子集合操作
   *
   * keys、keySet、keyslterator、valueslterator和values方法以不同的形式分别返回映射的键和值。
   */

  /**
   * @变换操作
   *
   * filterKeys 和 mapValues方法通过 过滤或变换已有映射的绑定来产生新的映射。
   *
   * 映射的添加和移除操作与集的对应操作很相似。使用诸如+、-和updated方法可以对不可变映射进行变换。
   * 而使用m(key) = value或m+= (key -> value)这两种不同的操作，一个可变集m通常会被当场更新。
   * @还有另一种操作是m.put(key,value)，这个操作会返回一个包含了之前与这个key关联的值的Option，而如果映射之前并不存在该key,则返回None。
   *
   * getOrElseUpdate方法适用于对用作缓存的映射进行访问。假设你有一个因调用f涵数触发的开销巨大的计算:
   * */

  def f(x: String):String =
    println("take my time")
    Thread.sleep(1)
    x.reverse

    /*
    * 如果f涵数没有副作用，也就是说，用相同的入参再次调用它总是会返回相同的结果，那么在这种情况下，
    * 你可以通过将之前计算过的f函数的入参和结果的绑定保存在映射中来节约时间，
    * 只有在找不到某个入参对应的值的时候才会触发对f结果的计算。你可以说，这个映射是对f函数计算的缓存(cache)。
    *
    * 接下来，就可以创建一个f函数的更高效的缓存版本:
    */
  val cache = collection.mutable.Map[String, String]()
    cache += "hello"-> "789"

  def cachedF(s:String) = cache.getOrElseUpdate(s,f(s))

  println(cachedF("hello"))

  /**
   * @注意，getOrElseUpdate方法的第二个参数是“传名”(by-name)的，因此只有当getOrElseUpdate方法需要第二个参数的值时，f("abc")的计算才会被执行，
   * 也就是首个入参没有出现在cache映射中的时候。你也可以用基本的映射操作来直接实现cachedF方法，不过需要写更多的代码:
   */
  def cachedF1(arg:String) =
    cache.get(arg) match
      case Some(result) =>result
      case None =>
        val result = f(arg)
        cache(arg) = result
        result





}
