package chapter18

object scala03_type_annotation extends App {

  /**
   * 18.3 类型注解
   *
   * 示例18.2定义的Queue是一个特质，而不是一个类型。之所以Queue不是类型，是因为它接收一个类型参数。
   * (Queue可以被看作一种“高阶类型”(higher kinded type)，相关内容将在《Scala高级编程》中详细探讨。)
   */

  import chapter18.scala02_information_hiding.Queue
  //def doesNotCompile(q:Queue)={}

  /**
   * 然而，Queue特质允许我们指定参数化的类型，如Queue[String]、Queue[Int]、Queue[AnyRef]等:
   */
  def doesCompile(q: Queue[AnyRef]) = {}

  /**
   * @所以，Queue是一个特质，而Queue[String]是一个类型。Queue也被称作类型构造器 (type constructor)，
   * @因为我们可以通过指定类型参数来构造一个类型。(这与通过指定值参数来构造对象实例的普通构造方法的道理是一样的。)类型构造器Queue能够“生成”成组的类型，包括Queue[Int]、Queue[String]和Queue[AnyRef]。
   * @也可以说,Queue是一个泛型(generic)的特质。(接收类型参数的类和特质是“泛型”的，但它们生成的类型是“参数化”的，而不是“泛型”的。)
   * @“泛型”的意思是用一个泛化的类或特质来定义许许多多具体的类型。举例来说，示例18.4中的Queue特质就定义了一个泛型的队列。Queue[Int]和Queue[String]等就是那些具体的类型。
   */

  /**
   * @将类型参数和子类型这两个概念放在一起，会产生一些有趣的问题。例如，通过Queue[T]生成的类型之间,有没有特殊的子类型关系?
   * @更确切地说，Queue[String]应不应该被当作Queue[AnyRef]的子类型?或者更通俗地说，如果S是类型T的子类型，那么Queue[S]应不应该被当作Queue[T]的子类型?
   * @如果应该，则可以说Queue特质在类型参数T上是协变的(covariant)(或者说“灵活的")。
   * @由于它只有一个类型参数，因此我们也可以简单地说Queue是协变的。
   * @协变的Queue意味着我们可以传入一个Queue[String]到前面的doesCompile方法中，这个方法接收的是类型为Queue[AnyRef]的值参数。
   */

  /**
   * 直观地讲，所有这些看上去都可行，因为一个String的队列看上去就像是一个AnyRef的队列的特例。
   *
   * @不过在Scala中，泛型默认的子类型规则是不变的(nonvariant)(或者说“刻板的")。
   * 也就是说，像示例18.4那样定义的Queue，不同元素类型的队列之间永远不会存在子类型关系。
   * Queue[String]不能被当作Queue[AnyRef]来使用。
   * @不过，我们可以修改Queue类定义的第一行来要求队列的子类型关系是协变的（灵活的):
   */

  trait Queue[+T] {}

  /**
   * 在类型形参前面加上+表示子类型关系在这个参数上是协变的(灵活的)。
   *
   * @通过这个字符，可以告诉Scala我们要的效果是，Queue[String]是Queue[AnyRef]的子类型。
   * @编译器会检查Queue的定义是否符合这种子类型关系的要求。
   * @除了+，还有-可以作为前缀，表示逆变的(contravariance)子类型关系。如果Queue的定义是下面这样的:
   */
  trait Queue1[-T] {}

  /**
   * @那么如果T是类型S的子类型，则表示Queue[S]是Queue[T]的子类型(这对队列的例子而言很出人意料)。
   * @类型参数是协变的、逆变的还是不变的，被称作类型参数的型变(variance)。可以放在类型参数旁边的+和-被称作型变注解 (variance annotation)。
   *
   *                                                               在纯函数式的世界中，许多类型都自然而然是协变的（灵活的)。不过，当引入可变数据之后，情况就会发生变化。
   *                                                               要搞清楚为什么，可以考虑这样一个简单的、可被读/写的单元格，如示例所示。
   */
  //一个不变的（刻板的）Cell类
  class Cell[T](init: T):
    private var current = init

    def get = current

    def set(x: T) = current = x

  /**
   * 示例18.5中的Cell类被声明为不变的（刻板的)。为了讨论的需要，我们暂时假设Cell类被定义成了协变的（即class Cell[+T])，
   * 且通过了Scala编译器的检查。(实际上并不会，稍后我们会讲到原因。)那么，我们可以构建出如下这组有问题的语句:
   *
   * val c1 = new Cell[String]("abc")
   * val c2:Cell[Any] =c1
   * c2.set(1)
   * val s:String =c1.get
   */

  /**
   * 单独看每一句，这4行代码都是没问题的。第一行创建了一个字符串的单元格，并将它保存在名称为c1的val中。
   * 第二行定义了一个新的val c2，类型为CelI[Any]，并采用c1初始化。这是可行的，因为Cell类被认为是协变的。
   * 第三行将c2这个单元格的值设置为1。这也是可行的，因为被赋的值1是c2的元素类型Any的实例。
   * 最后一行将c1的元素值赋值给一个字符串。
   *
   * @不过将这4行代码放在一起，所产生的效果是将整数1赋值给了字符串s。这显然有悖于类型约束。
   */

  /**
   * 我们应该将运行时的错误归咎于哪一步操作呢?
   *
   * @一定是第二行，因为这一行用到了协变的子类型关系。而其他的语句都太简单和基础了。
   * @因此，String的Cell并不同时是Any的Cell，因为有些我们能对Any的Cell做的事情并不能对String的cell做。举例来说，我们并不能对String的Cell使用参数为Int的set。
   *
   * 事实上，如果我们将Cell的协变版本传递给Scala编译器，将得到下面的编译器错误: (img.png)
   */

  /**
   * @型变和数组
   *
   * 将型变这个行为与Java的数组相比较会很有趣。从原理上讲，数组与单元格很像，只不过数组的元素可以多于一个。
   * @尽管如此，数组在Java中是被当作协变的来处理的。
   *
   * 我们可以仿照前面的单元格交互来尝试Java数组的例子:
   * //这是Java
   * String[] a1 = {"abc"};
   * Object[] a2 = a1;
   * a2[0] = new Integer
   *
   *
   * 如果执行这段代码，你会发现它能够编译成功，不过在运行时，当a2[0]被赋值成一个Integer时，程序会抛出ArrayStoreException:
   * @发生了什么?Java在运行时会保存数组的元素类型。每当数组元素被更新时，都会检查新元素值是否满足保存下来的类型要求。如果新元素值不是这个类型的实例，就会抛出ArrayStoreException。
   *
   * 你可能会问，Java为什么会采纳这样的设计，这样看上去既不安全，运行开销也不低。
   * @当被问及这个问题时，Java语言的主要发明人James-Gosling是这样回答的:他们想要一种简单的手段来泛化地处理数组。
   * @举例来说，他们想要用下面这样一个接收Object数组的sort方法来对数组的所有元素排序:
   *
   * void sort(Object[] a, Comparator cmp){...}
   * @然而，只有协变的数组才能让任意引用类型的数组得以传入这个sort方法。
   * @当然，随着Java泛型的引入，这样的sort方法可以用类型参数来编写，这样一来，就不再需要协变的数组了。不过由于兼容性，直到今天Java还保留了这样的做法。
   */

  /**
   * @Scala在这一点上比Java做得更纯粹，它并不把数组当作协变的。如果我们尝试将数组的例子的前两行翻译成Scala，就像这样:
   */
  val a1 = Array("abc")
//  val a2:Array[Any] =a1

  /**
   * @发生了什么?Scala将数组处理成不变的（刻板的)，因此Array[String]并不会被当作Array[Any]处理。
   * @不过，有时我们需要与Java的历史方法交互，这些方法会用Object数组来仿真泛型数组。
   * @举例来说，你可能会想以一个String数组为入参调用前面描述的那个sort方法。
   *
   * @Scala允许我们将元素类型为T的数组类型转换成T的任意超类型，例如:
   */
  val a2: Array[Object] = a1.asInstanceOf[Array[Object]]

  /**
   * 这个类型转换在编译时永远合法，且在运行时也永远会成功，
   * @因为JVM的底层运行时模型 对数组的处理都是协变的，就像Java语言一样。
   * 不过你可能在这之后得到ArrayStoreException，这也是与Java—样的。
   */






}
