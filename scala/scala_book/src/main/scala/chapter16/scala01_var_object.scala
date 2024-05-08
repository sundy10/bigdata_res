package chapter16

object scala01_var_object extends App {

  /**
   * 第16章
   * 可变对象
   *
   * 在前面的章节中，我们将注意力集中在了函数式（不可变）的对象上。这是因为没有任何可变状态的对象这个理念值得人们更多的关注。
   * 不过，在Scala里定义带有可变状态的对象也完全可行。当我们想要对真实世界中那些随着时间变化的对象进行建模时，自然而然就会想到这样的可变对象。
   *
   * 本章将介绍什么是可变对象，以及Scala提供了怎样的语法来编写可变对象。
   * 我们还将引入一个大型的、会涉及可变对象的、关于离散事件模拟的案例分析，并构建一个用来定义数字电路模拟的内部DSL。
   */

  /**
   * 16.1 什么样的对象是可变的
   *
   * 我们甚至不需要查看对象的实现就能见察到纯函数式对象和可变对象的主要区别。
   *
   * @当我们调用某个纯函数式对象的方法或获取它的字段时，总是能得到相同的结果。
   * 举例来说，给定下面这个字符列表:
   */
  val cs = List('a', 'b', 'c')

  /**
   * 对cs.head的调用总是返回'a'。即使从列表被定义到发起cs.head调用之前发生了任意数量的操作，这一点也不会改变。
   *
   * 另一方面，对可变对象而言，方法调用或字段访问的结果可能取决于之前这个对象被执行了哪些操作。
   * 可变对象的一个不错的例子是银行账户。示例16.1给出了银行账号的一个简单实现。
   */
  class BankAccount:
    private var bal: Int = 0
    def balance:Int =bal
    def deposit(amount:Int):Unit =
      require(amount>0)
      bal+=amount

    def withdraw(amount:Int):Boolean =
      if amount > bal then false
      else
        bal -= amount
        true

  /**
   * BankAccount类定义了一个私有变量bal，以及3个公有方法:
   *    balance方法，用于返回当前的余额;
   *    deposit方法，用于向bal(余额)添加给定的amount(金额);
   *    withdraw方法，用于尝试从bal扣除给定的amount，同时确保余额不为负值。
   *    withdraw方法的返回值是一个Boolean值，用来表示资金是否成功被提取。
   *
   * 即使并不知道任何BankAccount类的细节，我们也能分辨出它是可变对象，例如:
   */
  val account = new BankAccount
  account.deposit(100)
  account.withdraw(80)//true
  account.withdraw(80)//false
  println(account.balance)

  /**
   * 即使并不知道任何BankAccount类的细节，我们也能分辨出它是可变对象，
   * 例如:操作与前一次操作没有区别，但是返回的结果是false，这是因为账户的余额已经减少，不能再支持第二次提现。
   * 显然，银行账户带有可变状态，因为同样的操作在不同的时间会返回不同的结果。
   *
   * 你可能会觉得BankAccount类包含一个var定义已经很明显地说明它是可变的。
   * 虽然可变和var通常结对出现，不过事情并非总是那样泾渭分明。
   * 举例来说，一个类可能并没有定义或继承任何var变量，但它依然是可变的，因为它将方法调用转发到了其他带有可变状态的对象上。
   * 反过来也是有可能的:一个类可能包含了var却是纯函数式的。例如，某个类可能为了优化性能将开销巨大的操作结果缓存在字段中。
   * 参考下面这个例子，一个没有经过优化的Keyed类，其computeKey操作开销很大:
   *
   * class Keyed:
   *    def computeKey: Int =...
   *    ...
   *
   *    假设computeKey操作既不读也不写任何var，那么可以通过添加缓存来让Keyed类变得更高效:
   */
  class MemoKeyed :
    private var keyCache:Option[Int]=None
    def computeKey:Int =
      if !keyCache.isDefined then
        keyCache = Some(1)
      keyCache.get


  /**
   * 使用MemoKeyed类而不是Keyed类可以提速，因为computeKey
   * 操作在第二次被请求时，可以直接返回保存在keyCache字段中的值，而不是再次执行computeKey操作。
   * 不过除了速度上的提升，Keyed和MemoKeyed类的行为完全一致。
   * 因此，如果说Keyed类是纯函数式的，则MemoKeyed类同样也是，尽管它有一个可被重新赋值的变量。
   */


}
