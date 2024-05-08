package chapter24

object scala09_array extends App {

  /**
   * 24.9 数组
   *
   * 数组在Scala中是一种特殊的集合。一方面，Scala的数组与Java的数组一一对应。也就是说，Scala的数组Array[Int]用Java的int[]表示，
   * Array[Double]用Java的double[]表示，而Array[String]用Java的String表示。
   *
   * @不过，另一方面，Scala的数组与其Java版本相比提供了更多的功能。首先，Scala的数组支持泛型(generic)。
   * 也就是说，你可以拥有Array[T]，其中T是类型参数或抽象类型。
   * @其次，Scala的数组与Scala的序列兼容（你可以在要求Seq[T]的地方传入Array[T])。
   * @最后，Scala的数组还支持所有的序列操作。参考下面的例子:
   */
  val a1 = Array(1, 2, 3)
  val a2 = a1.map(_ * 3)
  val a3 = a2.filter(_ % 2 != 0)
  println(a3.reverse)

  /**
   * 既然Scala的数组是用Java的数组来表示的，那么，Scala是如何支持这些额外功能的呢?
   *
   * 答案是对隐式转换的系统化使用。数组并不能“假装自己是”序列，因为原生数组的数据类型表示并不是Seq的子类型。
   *
   * @每当数组被用作序列时，它都会被隐式地包装成Seq的子类。这个子类的名称是scala.collection.mutable.ArraySeq。参考下面的例子:
   */
  val seq: collection.Seq[Int] = a1
  val a4: Array[Int] = seq.toArray
  println(a1 eq a4)

  /**
   * 从上述交互中可以看到，数组与序列是兼容的，因为有一个从Array到ArraySeq的隐式转换。
   * 如果要反过来，从ArraySeq转换成Array，则可以用lterable特质中定义的toArray方法。
   * 上述编译器交互程序中最后一行显示，先包装再通过toArray方法解包，可以得到与一开始相同的数组。
   */

  /**
   * 可以被应用到数组的还有另一个隐式转换。这个转换只是简单地将所有的序列方法“添加”到数组中，但并不会将数组本身变成序列。
   * "添加”意味着数组被包装成另一个类型为ArrayOps的对象，这个对象支持所有的序列方法。
   * 通常，这个ArrayOps对象的生命周期很短:它通常在调用完序列方法之后就不再被访问了，因此其存储空间可以被回收。目前的VM会完全避免创建这个对象。
   */
  val seq1: collection.Seq[Int] = a1 //ArraySeq(1,2,3)
  seq1.reverse //ArraySeq(3,2,1)
  val ops: collection.ArrayOps[Int] = a1 //Array(1,2,3)
  ops.reverse //Array(3,2,1)

  /**
   * 你可以看到，对seq这个ArraySeq调用reverse方法会再次给出ArraySeq。这合乎逻辑，因为被包装的数组是Seq，而对任何Seq调用reverse方法都会返回Seq。
   * 但是，对ArrayOps类的ops调用reverse方法，则返回的是Array而不是Seq。
   *
   * 上述ArrayOps的例子非常人性化，其目的仅仅是展示与ArraySeq的区别。在通常情况下，你不需要定义一个ArrayOps类的值，只需要对数组调用一个Seq的方法即可:
   */
  a1.reverse
  intArrayOps(a1).reverse

  /**
   * 隐式转换会自动插入ArrayOps对象。因此，上面这一行代码与下面这一行的代码是等效的，其中，intArrayOps就是那个被自动插入的隐式转换:
   */

  /**
   * 这就带来一个问题:编译器是如何选中intArrayOps而不是另一个到ArraySeq的隐式转换的呢?
   * 毕竟，这两个隐式转换都可以将数组映射成一个支持reverse方法的类型(编译器中的输入要求使用这个reverse方法)。
   * 这个问题的答案是:这两个隐式转换之间存在优先级。ArrayOps转换的优先级高于ArraySeq转换的优先级。
   * 前者定义在Predef对象中，而后者定义在scala.LowPriorityImplicits类中，这个类是Predef的超类。
   * 由于子类和子对象中的隐式转换比基类的隐式转换优先级更高，因此如果两个隐式转换同时可用，则编译器会选择Predef中的那一个。
   * 我们在21.7节还讲到了另一个类似的机制，是关于字符串的。
   */

  /**
   * 现在你已经知道了数组与序列是兼容的，它们支持所有的序列操作。不过泛型呢?在Java中，你无法写出T[]，其中的T是类型参数。
   * 那么，Scala的Array[T]又是如何表示的呢?事实上，像Array[T]这样的泛型数组在运行时
   * 可以是任何Java支持的8种基本类型的数组 byte、short、char、int、long、float、double、boolean，也可以是对象的数组。
   * 唯一能横跨所有这些类型的公共运行时类型是AnyRef(或者与其等同的java.lang.Object)，因此这就是Scala将Array[T]映射到的类型。
   * 在运行时，当类型为Array[T]的数组的元素被访问或更新时，首先由一系列的类型检查来决定实际的数组类型，然后才会进行对Java数组的正确操作。
   *
   * @这些类型检查在一定程度上减缓了数组操作。你可以预期对泛型数组的访问速度约为对基本类型或对象数组的访问速度的25%~33%。
   * @这意味着如果你需要最大限度的性能，则应该考虑具体的类型确定的数组，而不是泛型数组。
   */

  /**
   * 仅仅能够表示泛型的数组类型是不够的，我们还需要通过某种方式来“创建”泛型数组。这个问题解决起来更加困难，需要你的帮助。
   * 为了说明问题，考虑下面这个尝试创建泛型数组的方法:
   */
  //这是错误的
  //  def evenElems[T](xs: Vector[T]): Array[T] =
  //    val arr = new Array[T]((xs.length + 1) / 2)
  //    for i <- 0 until xs.length by 2 do
  //      arr(i / 2) = xs(i)
  //    arr


  /**
   * evenElems方法返回一个新的由入参向量xs的所有在向量中偶数位置的元素组成的数组。
   * evenElems方法体的第一行创建了结果数组，其元素类型与入参一样。基于类型参数T的实际类型，
   * 这可能是Array[Int]，可能是Array[Boolean]，可能是某种Java其他基本类型的数组，也可能是某种引用类型的数组。
   * 不过这些类型在运行时的表现形式各不相同，那么，Scala运行时应该如何选取正确的那一个呢?
   * 事实上，基于给出的信息，Scala运行时做不到，因为与类型参数T相对应的实际类型信息在运行时被擦除了。
   * 这就是如果你尝试编译上面的代码，会得到如下错误提示的原因:
   * No ClassTag available for T
   */

  /**
   * 编译器在这里需要你的帮助——帮忙提供关于evenElems方法实际的类型参数是什么的运行时线索。
   * 这个线索的表现形式是类型为scala.reflect.ClassTag的类标签(class tag)。
   * 类标签描述的是给定类型的运行期类（runtime class)，这也是构造该类型的数组所需要的全部信息。
   *
   * 在许多情况下，编译器都可以自行生成类标签。对于具体类型Int或 String就是如此。对于某些泛型类型，如List[T]也是如此。
   * 如果有足够多的信息已知，就可以预测运行期类。在本例中，这个运行期类就是List.
   *
   * 对于完全泛化的场景，通常的做法是用上下文界定传入类型标签，就像我们在23.2节探讨的那样。我们可以像下面这样用上下文界定来修改前面的定义:
   */
  //这样可行
  import scala.reflect.ClassTag
  def evenElems[T:ClassTag](xs: Vector[T]): Array[T] =
    val arr = new Array[T]((xs.length + 1) / 2)
    for i <- 0 until xs.length by 2 do
      arr(i / 2) = xs(i)
    arr

  /**
   * 在新的定义中，当Array[T]被创建时，编译器会查找类型参数T的类标签，也就是说，它会查找一个类型为ClassTag[T]的隐式值。
   * 如果找到这样的值，类标签就被用于构造正确类型的数组。不然，你就会看到前面那样的错误提示。
   *
   * 下面是使用evenElems方法的编译器交互程序:
   */
  evenElems(Vector(1,2,3,4,5)) //Array(1,3,5)
  evenElems(Vector("this","is","a","test","run")) //Array(this, a, run)

  /**
   * 在两种情况下，Scala编译器都会自动地为元素类型构建类标签(首先是Int，然后是String）并将它传入evenElems方法的隐式参数中。
   * 对于所有具体类型，编译器都可以帮助我们完成，但是，如果入参本身是另一个类型参数且不带类标签，它就无能为力了。
   * 比如，下面这段代码就不可行:
   */
  //  def wrap[U](xs:Vector[U])=evenElems(xs)

  /**
   * 为什么会这样?原因是evenElems方法要求类型参数u的类标签,但没有找到。
   * 当然，这种情况的解决方案是要求另一个针对u的隐式类标签。因此，下面这段代码是可行的:
   */
  def wrap[U:ClassTag](xs:Vector[U]) = evenElems(xs)

  /**
   * 这个例子同时告诉我们:U定义中的上下文界定只不过是此处名称为evidence$1、类型为ClassTag[U]的隐式参数的简写而已。
   */

}
