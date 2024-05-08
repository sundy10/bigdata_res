package chapter05

object scala04_object_equals extends App {

  /**
   * 如果想要比较两个对象是否相等，则可以用==或与之相反的!=。
   */

  1 == 2 //false
  1 != 2 //true
  2 == 2 //true

  /**
   * 这些操作实际上可以被应用于所有的对象，并不仅仅是基础类型。比如，可以用==来比较列表:
   */

  println(List(1, 2, 3) == List(1, 2, 3)) //true
  println(List(1, 2, 3) == List(2, 3, 1)) //false

  //还可以比较不同类型的两个对象:
  println(1 == 1.0) //true

  //甚至可以拿对象与null做比较，或者与可能为null的对象做比较,并且不会抛出异常:
  println(List(1, 2, 3) == null) //false
  println(null == List(1, 2, 3)) //false

  /**
   * 如你所见，==的实现很用心，大部分场合都能返回给你需要的相等性比较的结果。这背后的规则很简单:首先检查左侧是否为null ,
   * 如果不为null，则调用equals方法。由于equals是一个方法，因此得到的确切比较逻辑取决于左侧参数的类型。由于有自动的null检查，
   * 因此我们不必亲自做这个检查。
   */
  println(("he" + "llo") == "hello") //true


  /**
   *  Scala的==与Java的==的不同
   *  在Java中，可以用==来比较基本类型和引用类型。对基本类型而言，Java的==比较的是值的相等性，就像Scala的一样。
   *  但是对引用类型而言，Java的==比较的是引用相等性(reference equality) ,意思是两个变量指向JM的堆上的同一个对象。
   *  Scala也提供了用于比较引用相等性的机制，即名称为eq的方法。不过，eq和与它对应的ne只对那些直接映射到Java对象的对象有效。
   *  关于eq和ne的完整细节会在17.1节和17.2节给出。关于如何编写一个好的equals方法，请参考第8章。
   */
}
