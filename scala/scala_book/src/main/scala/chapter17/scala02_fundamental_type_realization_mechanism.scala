package chapter17

object scala02_fundamental_type_realization_mechanism extends App {

  /**
   * 17.2 基本类型的实现机制
   *
   * 所有这些是如何实现的呢?事实上，Scala存放 整数的方式与Java一样，都是32位的词(word)。
   * 这对于JVM的效率及与Java类库的互操作都很重要。标准操作，如加法和乘法被实现为基本操作。
   *
   * @不过，Scala在任何需要将整数当作（Java）对象的情况下，都会启用“备选”的java.lang.Integer类。
   * @例如，当我们对整数调用toString方法或者将整数赋值给一个类型为Any的变量时，都会发生这种情况。类型为Int的整数在必要时都会被透明地转换成类型为java.lang.Integer的“装箱整数”。
   */

  /**
   * 所有这些听上去都很像Java的自动装箱(auto-boxing）机制，
   * 也的确非常相似。不过有一个重要区别: Scala中的装箱与Java相比要透明得多。参考下面的Java代码:
   *
   * boolean isEqual(Integer x, Integer y){
   * return x == y
   * }
   * println(isEqual(421, 421));
   *
   * 你会发现你得到了false，发生了什么呢?这里的数字421被装箱了两次。
   * 因此，x和y这两个参数实际上是两个不同的对象。由于==方法对引用类型而言意味着引用相等性，而Integer是一个引用类型，因此结果就是false。
   * 这一点也显示出Java并不是一个纯粹的面向对象语言。基本类型和引用类型之间有一个清晰的、可被观察到的区别。
   *
   * 现在，我们用Scala来做相同的试验:
   */
  def isEqual(x: Int, y: Int) = x == y

  def isEqual1(x: Any, y: Any) = x == y

  def isEqual2(x: AnyVal, y: AnyVal) = x == y

  println(isEqual(421, 421))
  println(isEqual1(421, 421))
  println(isEqual2(421, 421))

  /**
   * Scala的相等性操作==被设计为对于类型的实际呈现是透明的。对值类型而言，它表示的是自然（数值或布尔值）相等性;
   *
   * @而对除Java装箱数值类型之外的引用类型而言，它被处理成从Object继承的equals方法的别名。
   * 这个方法原本用于定义引用相等性，但很多子类都重写了这个方法来实现它们对于相等性更自然的理解和表示。
   * 这也意味着在Scala中不会陷入Java那个与字符串对比相关的“陷阱”。Scala的字符串对比是它应该有的样子:
   */
  val x = "abcd".substring(2)
  val y = "abcd".substring(2)
  println(x == y)

  /**
   * 在Java中，对x和y的对比，结果会返回false。因为程序员在这里应该使用equals方法，但是很容易忘。
   *
   * 不过，在有些场景下需要引用相等性而不是用户定义的相等性。例如，有些场景对于效率的要求超高，可能会对某些类使用hashCode并用引用相等性来对比其实例。
   * @对于这些情况，AnyRef类定义了一个额外的eq方法，该方法不能被重写，其实现为引用相等性(即它的行为与Java中==对于引用类型的行为是一致的)。还有一个eq方法的反义方法ne。例如:
   */
  val x1 = new String("abc")
  val y1 = new String("abc")
  println(x1==y1)
  println(x1 eq y1)
  println(x1 ne y1)


  //我们在第8章对Scala对象相等性进行了探讨。





}
