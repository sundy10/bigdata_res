package chapter24

object scala01_mutable_immutable extends App {

  /**
   *  24.1 可变和不可变集合
   *
   *  现在你应该已经知道，Scala集合系统化地对可变和不可变集合进行了区分。
   *  @可变集合可以被当场更新或扩展，这意味着你可以以副作用的形式修改、添加或移除集合中的元素。
   *  @而不可变集合永远都不会变。虽然你仍然可以模拟添加、移除或更新集合中的元素，但这些操作每次都返回新的集合，从而保持老的集合不变。
   */

  /**
   * 所有的集合类都可以在scala.collection包或它的子包mutable.immutable和generic中找到。
   * 大多数使用方需要用到的集合类都有3个变种，分别对应不同的可变性特征。
   * 这3个变种分别位于scala.collection包、scala.collection.immutable包和scala.collection.mutable包中。
   */

  /**
   * scala.collection.immutable包中的集合对所有人都是不可变的。
   * 这样的集合在创建后就不会改变。因此，你可以放心地在不同的地方反复地访问同一个集合值，它都会交出相同元素的集合。
   */

  /**
   * scala.collection.mutable包中的集合有一些操作可以当场修改集合。这些操作允许你自己编写改变集合的代码。
   * 不过，你必须很小心，要理解并防止代码中其他部分对集合的修改。
   */

  /**
   * @而scala.collection包中的集合既可以是可变的，也可以是不可变的。
   * 举例来说, scala.collection.IndexedSeq[T]
   * 是scala.collection.immutable.IndexedSeq[T] 和 scala.collection.mutable.IndexedSeq[T] 的超类型。
   * 一般而言，scala.collection包中的根（ root)集合支持那些会影响整个集合的操作，如map和filter。
   * 位于scala.collection.immutable包的不可变集合通常会加上用于添加和移除单个值的操作，
   * 而位于scala.collection.mutable包的可变集合通常会在根集合的基础上添加一些有副作用的修改操作。
   */

  /**
   * @根集合与不可变集合的另一个区别在于，不可变集合的使用方可以确定没有人能修改这个集合，
   * @而根集合的使用方只知道他们自己不能修改这个集合。虽然这样的集合的静态类型没有提供修改集合的操作，
   * @但是它的运行时类型仍然有可能是一个可变集合，能够被使用方修改。
   */

  /**
   * Scala默认选择不可变集合。例如，如果你只写Set，不带任何前缀也没有引入任何类，则得到的是一个不可变的Set;
   * 而如果你只写lterable，则得到的是一个不可变的lterable，因为这些是scala包引入的默认绑定。
   * 要想获取可变的版本，需要显式地写出scala.collection. mutable.Set或scala.collection.mutable.Iterable。
   */

  /**
   * @集合类继承关系中最后一个包是scala.collection.generic。
   * @这个包包含了那些用于实现集合的构建单元。集合类通常会选择将部分操作交给scala.collection.generic包中的类的实现来完成。
   * @集合框架的日常使用并不需要引用scala.collection.generic包中的类,极端情兄下除外。
   */
}
