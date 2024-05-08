package chapter10

object scala06_define_arg_field extends App {

  abstract class Element {
    def contents: Vector[String]

    def height: Int = contents.length

    def width: Int = if height == 0 then 0 else contents(0).length
  }

//  class VectorElement(conts: Vector[String]) extends Element {
//    val contents: Vector[String] = conts
//  }


  /**
   * 10.6 定义参数化字段
   *
   * 让我们再来看看前一节定义的VectorElement类。
   * 它有一个conts参数，这个参数存在的唯一目的就是被复制到contents字段上。
   * 参数的名称选用conts是为了让它看上去与字段名contents相似但又不至于与它冲突。
   * 这是“代码的坏味道”(code smell)，是你的代码可能存在不必要的冗余和重复的一种信号。
   *
   * 可以通过将参数和字段合并成参数化字段( parametric field）的方式来避免这个“坏味道”，如示例10.5所示。
   */

    class VectorElement(val contents: Vector[String]) extends Element

  /**
   * @需要注意的是，现在contents参数前面放了一个val。
   * @这是同时定义参数和同名字段的简写方式。具体来说,VectorElement类现在具备一个（不能被重新赋值的)contents字段。
   * @该字段可以被外界访问到，并且会被初始化为参数的值。这就好像类定义是如下的样子，其中x123是这个参数的一个任意起的新名:
   */

  class VectorElement_x123(x123: Vector[String]) extends Element{
    val contents: Vector[String] = x123
  }

  /**
   * 也可以在类参数的前面加上var，这样对应的字段就可以被重新赋值。
   * 最后，还可以给这些参数化字段添加修饰符，如private、protected或override，
   * 就像你能够对其他类成员做的那样。例如，下面这些类定义:
   */
  class Cat{
    val dangerous =false
  }
  class Tiger(override val dangerous: Boolean,private var age:Int) extends Cat

  /**
   * 这两个成员都通过对应的参数初始化。我们选择param1和param2这两个名字是非常随意的，重要的是它们并不与当前作用域内的其他名称冲突。
   */
}
