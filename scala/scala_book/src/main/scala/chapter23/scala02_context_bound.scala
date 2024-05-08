package chapter23

object scala02_context_bound extends App {

  /**
   * 23.2 上下文界定
   *
   * 由于类型族是Scala中的一个重要模式，因此Scala为它提供了一个名称为上下文界定（context bound）的简写语法。
   * 以示例23.2所示的maxList函数为例，这是一个返回传入列表中最大元素的函数。
   * 这个maxList函数接收一个List[T]作为它的首个入参，并在接下来的参数列表中接收一个额外的类型为Ordering[T]的using入参。
   * 在maxList函数体中，传入的次序（ordering）在两个地方被用到:对maxList函数的递归调用中，
   * 以及检查列表头部元素是否比列表剩余部分的最大元素更大 的if表达式中。
   */
  //带有using参数的函数
  def maxList[T](elements: List[T])(using ordering: Ordering[T]): T =
    elements match {
      case List() => throw new IllegalArgumentException("empty list!")
      case List(x) => x
      case x :: rest =>
        val maxRest = maxList(rest)(using ordering)
        if ordering.gt(x, maxRest) then x else maxRest
    }

  /**
   * 这个maxList函数是使用using参数提供更多关于 某个在更早的参数列表中 被显式提到的类型 的更多信息的一个例子。
   * 具体而言，ordering这个类型为Ordering[T]的using参数提供了更多关于类型T的信息，
   * 就本例而言，即如何对T排序。类型T是在List[T]中被提到的，这是elements参数的类型，出现在更早的参数列表中。
   * 由于elements在任何对maxList函数的调用中都必须被显式地给出，因此编译器在编译期就可以知道T
   * 并确定是否有可用的类型为Ordering[T]的上下文参数定义。如果有，编译器就能（隐式地)传入第二个参数列表。
   */

  /**
   * 在示例给出的maxList函数实现中，我们用using显式地传入了ordering，不过并不需要这样做。
   * 当你对参数标记上using时，编译器不仅会尝试提供（supply）对应上下文参数值的参数，还会在方法体中定义(define）这个参数，
   * 并将其当作一个可用的上下文参数。因此，在方法体中，第一次对ordering参数的使用可以被省去，参考示例。
   */

  def maxList1[T](elements: List[T])(using ordering: Ordering[T]): T =
    elements match {
      case List() => throw new IllegalArgumentException("empty list!")
      case List(x) => x
      case x :: rest =>
        val maxRest = maxList1(rest) //使用上下文参数
        if ordering.gt(x, maxRest) then x //这里的ordering
        else maxRest // 仍是显式给出的
    }

  /**
   * 当编译器检查示例 maxList1 的代码时，它将看到相关类型并不匹配。
   *
   * 表达式maxList(rest)只提供了一个参数列表，而maxList函数要求传入两个参数列表。
   * 由于第二个参数列表被标记为using，因此编译器并不会马上放弃类型检查，而是会查找合适类型的上下文参数，即Ordering[T]。
   * 就本例而言，它会找到这样一个上下文参数并把这个函数调用改写为maxList(rest) (using ordering)。这样一来，代码的类型检查也就得以通过。
   */

  /**
   * 还有一种方式可以避免对ordering参数的第二次使用。这种方式涉及如下被定义在标准类库中的方法:
   */
  //  def summon[T](using t:T)=t

  /**
   * 调用summon[Foo]的效果是，编译器会查找一个类型为Foo的上下文参数定义,然后会用一个对象来调用summon方法，而该方法会直接返回这个对象。
   * 如此一来，你就可以在任何需要在当前作用域内找到类型为Foo的上下文参数实例的地方写上summon[Foo]。
   * 示例23.4给出了使用summon[Ordering[T]]，并以类型获取ordering参数的例子。
   */
  //使用summon方法的函数
  def maxList2[T](elements: List[T])(using ordering: Ordering[T]): T =
    elements match {
      case List() => throw IllegalArgumentException("empty list!")
      case List(x) => x
      case x :: rest =>
        val maxRest = maxList2(rest) //使用上下文参数
        if summon[Ordering[T]].gt(x, maxRest) then x //这里的ordering
        else maxRest // 仍是显式给出的
    }

  /**
   * 仔细看最后这个版本的maxList函数。在方法的代码文本中，没有一次提到ordering参数,且第二个参数完全可以被命名为comparator :
   *
   * def maxList2[T](elements: List[T])(using :comparator Ordering[T]): T = ???
   *
   * 从这个意义上讲，如下版本也是可行的:
   * def maxList2[T](elements: List[T])(using :iceCream Ordering[T]): T = ???
   */

  /**
   * 由于这个模式很常见，因此Scala允许你省去这个参数的名字，用上下文界定来简化方法头部。
   * 通过使用上下文界定，你可以像示例23.5一样来编写maxList函数的签名。
   * @[T:Ordering]这个语法表示上下文界定，
   *    @它会做两件事:首先，它会像往常一样引入类型参数T;
   *    @其次，它会添加一个类型为Ordering[T]的using参数。
   *
   * 在前一个版本的maxList函数中，这个参数是ordering，但是当使用上下文界定时，你并不知道参数的名称是什么。
   * 就像前面所展示的，你通常也不需要知道参数名称是什么。
   */
  //带有上下文界定的函数
  def maxList3[T:Ordering](elements: List[T]): T =
    elements match {
      case List() => throw IllegalArgumentException("empty list!")
      case List(x) => x
      case x :: rest =>
        val maxRest = maxList3(rest) //使用上下文参数
        if summon[Ordering[T]].gt(x, maxRest) then x
        else maxRest
    }

  /**
   * 直观地讲，你可以把上下文界定看作关于类型参数的某种描述。
   * 当你写下[T <: Ordered [T]] 时，你表达的意思是T是一个Ordered[T]。
   * 对比而言，当你写下[T: Ordering]时，你表达的意思并非T是什么，而是对T关联了某种形式的先后次序。
   * 
   * @上下文界定从本质上讲是类型族的语法糖。Scala提供了这样的简写方式的事实也印证了在Scala编程中类型族是多么的有用。
   */

}
