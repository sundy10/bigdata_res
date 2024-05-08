package chapter03

object scala02_tuple extends App {


  /*
   * 另一个有用的容器对象是元组(tuple)。与列表类似，元组也是不可变的，不过与列表不同的是，
   * 元组可以容纳不同类型的元素。列表可以是List[Int]或List[String]，而元组可以同时包含整数和数组。
   * 当需要从方法返回多个对象时，元组非常有用。在Java中遇到类似情况时，
   * 你通常会创建一个类似JavaBean那样的类来承载多个返回值，而用Scala可以简单地返回一个元组。
   *
   *  Scala 3允许创建任意长度的元组。
   */

  val pair = (99, "Luftballons")
  val num = pair(0) //类型为 Int ,值为99
  val what:String = pair(1) //类型为String，值为 "Luftballons"
  val n = pair._1 //类型为 Int ,值为99
  private val tuple: (Char, Char, String, Int, Int, String) = ('u', 'r', "the", 1, 4, "me")
  


}
