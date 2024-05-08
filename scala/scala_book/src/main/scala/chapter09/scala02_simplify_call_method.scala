package chapter09

object scala02_simplify_call_method extends App {

  /**
   * 9.2 简化 调用方 代码
   *
   * 示例9.1展示了高阶函数如何帮助我们在实现API时减少代码重复。
   * 高阶函数的另一个重要用途是将高阶函数本身放在API中以让调用方代码更加精简。
   * Scala集合类型提供的特殊用途的循环方法是很好的例子。
   * (这些特殊用途的循环方法是在lterable特质中定义的，List、Set和Map都扩展自这个特质。第15章将会对此做更深入的讨论。)
   * 其中很多都在第3章的表3.1中被列出过，不过现在让我们再看一个例子来弄清楚为什么这些方法是很有用的。
   */

  /**
   * 我们来看exists方法，这个方法用于判定某个集合是否包含传入的值。
   * 当然，可以通过如下方式来查找元素:初始化一个var为false,用循环遍历整个集合来检查每一项，
   * 如果发现要找的内容，就把var设为true。参考下面这段代码，判定传入的List是否包含负值:
   */

  def containsNeg(nums: List[Int]): Boolean =
    var exists = false
    for num <- nums do
      if num < 0 then
        exists = true
    exists

  println(containsNeg(List(1, 2, 3, 4)))
  println(containsNeg(List(1, 2, -3, 4)))

  //不过更精简的定义方式是对传入的List调用高阶函数exists
  def containsNeg1(nums: List[Int]): Boolean = nums.exists(_ < 0)

  /**
   * 这里的exists方法代表了一种控制抽象。
   * 这是Scala类库是供的一个特殊用途的循环结构，并不是像while或for那样是语言内建的。
   * 在9.1节，高阶函数filesMatching帮助我们在FileMatcher对象的实现中减少了代码重复。
   * 这里的exists方法也带来了相似的好处，不过由于exists方法是Scala集合API中的公共函数，它减少的是API使用方的代码重复。
   *
   * 编写一个containsOdd方法来检查某个列表是否包含奇数
   */

  def containsOdd(nums: List[Int]) = nums.exists(_ % 2 == 1)

  /**
   * Scala类库中还有许多其他循环方法。与exists方法一样，如果你能找到机会使用它们，它们通常能帮助你宿短代码。
   */
}
