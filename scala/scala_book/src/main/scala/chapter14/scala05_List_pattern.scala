package chapter14

object scala05_List_pattern extends App {
  val fruit = "apples" :: ("oranges" :: ("pears" :: Nil))

  /**
   * 14.5 列表模式
   *
   * 列表也可以用模式匹配解开。列表模式可以逐一对应到列表表达式。
   * 既可以用List(...)这样的模式来匹配列表的所有元素，也可以用::操作符和Nil常量一点一点地将列表解开。
   *
   * 下面是第一种模式的例子:
   */
  val List(a, b, c) = fruit
  println(a)
  println(b)
  println(c)

  /**
   * List(a, b, c)这个模式匹配长度为3的列表，并将3个元素分别绑定到模式变量a、b和c上。
   * 如果我们事先并不知道列表中元素的个数，则更好的做法是用::来匹配。举例来说,a :: b :: rest匹配的是长度大于或等于2的列表:
   */

  val aa :: bb :: rest = fruit

  println(rest)

  /**
   * 关于List的模式匹配
   *
   * 如果你回顾第13章介绍过的可能出现的模式的形式，则会发现无论是List(...)还是::都不满足那些定义。事实上，List(...)是一个由类库定义的提取器模式的实例。
   * 我们将在《Scala高级编程》介绍提取器模式。而x ::xs这样的“cons”模式是中缀操作模式的一个特例。
   *
   * @作为表达式，中缀操作等同于一次方法调用。 对模式而言，规则是不同的:作为模式，p op q这样的中缀操作等同于op(p, q)。也就是说，中缀操作符op是被当作模式构造方法处理的。
   *                       具体来说，x ::xs这个表达式相当于::(x, xS)。
   *
   *
   *                       这透露出一个细节，应该有一个名称为::的类与这个模式构造方法相对应。的确有这么一个类，它的名称为::，并且就是用来构建非空列表的。
   *                       因此，::在Scala中出现了两次，一次是作为scala包中的一个类的名称，一次是作为List类的方法名。::方法的作用是生成一个scala.::类的实例。
   *                       在《Scala高级编程》中将会有更多关于List类的实现细节介绍。
   *
   *                       使用模式来解开列表是使用基本方法head、tail和isEmpty来解开列表的变通方式。例如，再次实现插入排序，不过这一次，我们使用模式匹配:
   */

//  def isort(xs: List[String]): List[Int] =
//    xs match {
//      case Nil => Nil
//      case x :: xs1 => insert(x, isort(xs))
//    }
//
//  def insert(x: Int, xs: List[Int]): List[Int] =
//    xs match {
//      case Nil => List(x)
//      case y::ys => if x<=y then x::xs else y:: insert(x,ys)
//    }

  /**
   * 通常来说，对列表做模式匹配比用方法来解构更清晰，因此模式匹配应该成为处理列表的工具箱的一部分。
   *
   * 以上是在正确使用Scala列表之前你需要知道的全部内容。
   * @，Scala还提供了大量方法，捕获了列表操作的通用模式。这些方法让列表处理程序更为精简，也更为清晰。接下来的两节将介绍List类中最为重要的方法。
   */

}
