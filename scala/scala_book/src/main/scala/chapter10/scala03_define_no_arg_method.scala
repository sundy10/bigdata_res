package chapter10

object scala03_define_no_arg_method extends App {

  /**
   * 10.3 定义无参方法
   *
   * 接下来，我们将给Element类添加方法来获取它的宽度和高度，如示例10.2所示。
   * height方法用于返回contents中的行数;而width方法用于返回第一行的长度，如果完全没有内容，则返回0。(这意味着你不能定义一个高度为0但宽度不为0的元素。)
   *
   * 需要注意的是,Element芙的3个方法无一例外都没有参数列表，连空参数列表都没有。举例来说，我们并没有写:
   */

  abstract class Element{
    def contents:Vector[String]
    def height:Int =contents.length
    def width :Int = if height ==0 then 0 else contents(0).length
  }

  /**
   * 这样的无参方法（ parameterless method）在Scala中很常见。与此对应，那些用空的圆括号定义的方法，如def height(: Int，被称作空圆括号方法（empty-paren method)。
   * 推荐的做法是对于没有参数且只通过读取所在对象字段的方式访问其状态（确切地说，并不改变状态)的情况，尽量使用无参方法。
   * 这样的做法支持所谓的统一访问原则（uniform access principle):
   * @使用方代码不应受到某个属性是用字段还是用方法实现的影响。
   */

  /**
   * 举例来说,我们完全可以把width和height实现成字段，而不是方法，只要将定义中的def替换成val即可:
   *
   * 从使用方代码来看，这组定义完全是等价的。
   * @唯一的区别是字段访问可能比方法调用的速度快一些，因为字段值在类初始化时就被预先计算好，而不是在每次方法调用时都重新计算。
   * @另一方面，字段需要每个Element对象为其分配额外的内存空间。因此属性实现为字段好还是方法好，这个问题取决于类的用法，而用法是可以随着时间变化而变化的。
   * @核心点在于Element类的使用方不应该被内部实现的变化所影响。
   */

  /**
   * 具体来说，当Element类的某个字段被改写成访问函数时，Element类的使用方代码并不需要被重新编写，
   * 只要这个访问函数是纯粹的（即它并没有副作用也不依赖于可变状态)。使用方代码并不需要关心究竟是哪一种实现。
   */

  /**
   * 到目前为止都还好。不过仍然有一个小麻烦，这与Java和Scala2的处理细节有关。
   * 问题在于Java并没有实现统一访问原则，而Scala 2也没有完整地推行这个原则。
   * 因此，在Java中，对于字符串，要写string.length()而不是string.length;
   * 而对于数组，要写array. length而不是array. length()。无须赘言，这很让人困扰。
   */

  /**
   * 为了更好地桥接这两种写法，Scala3对于混用无参方法和空括号方法的处理非常灵活。
   * 具体来说，可以用空括号方法重写无参方法，也可以反过来，只要父类是Java或Scala 2编写的就行。
   * 还可以在调用某个由Java或Scala 2定义的不需要入参的方法时省去空括号。例如，如下两行代码在Scala 3中都是合法的:
   */
  Array(1,2,3).toString
  "abc".length

  /**
   * 从原理上讲，可以对Java或Scala 2的所有无参函数调用都去掉空括号。
   * 不过，我们仍建议在被调用的方法不仅只代表接收该调用对象的某个属性时加上空括号。
   * @举例来说，空括号的适用场景包括该方法执行l/O、写入可重新赋值的变量(var)、读取接收该调用对象字段之外的var（无论是直接还是间接地使用了可变对象)。
   * 这样一来，参数列表就可以作为一个视觉上的线索，告诉我们该调用触发了某个有趣的计算。例如:
   */
  "hello".length //不写(), 因为没有副作用
  println() //最好不要省去()

  /**
   * 总结下来就是，Scala鼓励我们将那些不接收参数也没有副作用的方法定义为无参方法（即省去空括号)。
   * 同时，对于有副作用的方法，不应该省去空括号，因为在省去括号以后，这个方法调用看上去就像是字段选择，因此你的使用方可能会对其副作用感到意外。
   */

  /**
   * 同理，当你调用某个有副作用的函数时，就算编译器没有强制要求，也请确保在写下调用代码时加上空括号。
   * 换一个角度来思考这个问题，如果你调用的这个函数用于执行某个操作，就加上括号，而如果它仅用于访问某个属性，就去掉括号。
   */
}
