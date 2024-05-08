package chapter22

object scala02_generalization_extend_method extends App {

  /**
   * 22.2 泛化的扩展方法
   *
   * 你可以定义泛化的扩展方法。作为示例，我们来看看List类的head方法。这个方法用于返回列表中的首个元素，不过，当列表为空时会抛出异常:
   */
  println(List(1,2,3).head)
  //List.empty.head

  /**
   * @如果你不确定列表是不是非空的，则可以使用headOption方法。这个方法在列表非空时返回由Some包装起来的首个元素，否则返回None:
   */
  println(List(1,2,3).headOption)
  println(List.empty.headOption)

  /**
   * List还提供了一个tail方法，用于返回除首个元素之外的所有元素。与head方法一样，当列表为空时，tail方法也会抛出异常:
   *
   * @不过，List类并没有提供一个返回用Option包装起来的除首个元素之外的所有元素的安全的备选方法。
   * 如果你想要这样一个方法，则可以 以泛化的扩展(generic extension)的形式提供。
   *
   * 要想让一个扩展方法成为泛化的扩展方法，
   * 需要在extension关键字之后，在包含接收者的圆括号之前，添加用方括号括起来的一个或多个类型参数，如示例22.3所示。
   */
  //一个泛化的扩展方法
  extension [T](xs:List[T])
    def tailOption:Option[List[T]] =
      if xs.nonEmpty then Some(xs.tail) else None

  /**
   * 这里的扩展方法tailOption只针对一个类型T做了泛化。这里有一些使用tailOption方法的例子，其中，T被实例化成Int或String:
   */
  println(List(1,2,3).tailOption)
  println(List.empty[Int].tailOption)
  println(List("A","B","C").tailOption)
  println(List.empty[String].tailOption)

  /**
   * 虽然你通常会让这样的类型参数被（编译器自动)推断出来，就像前面的例子那样，但是你也可以显式地给出类型参数。
   * 为了做到这一点，必须直接调用这个方法，也就是说，不使用扩展方法的形式:
   */
  println(tailOption[Int](List(1,2,3,4)))

}
