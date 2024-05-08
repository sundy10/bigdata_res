package chapter18

object scala06_contravariant extends App {

  /**
   *  18.6 逆变
   *
   *  本章到目前为止的例子不是协变的就是不变的。不过在有的场景下，逆变是自然的。参考示例18.7所示的输出通道。
   */
  //逆变的输出通道
  trait OutputChannel[-T]:
    def write(x:T):Unit

  /**
   * 这里的OutputChannel被定义为以T逆变。因此，一个AnyRef的输出通道就是一个String的输出通道的子类。
   * 虽然看上去有违直觉，但是实际上是讲得通的。我们能对一个OutputChannel[String]做什么呢?唯一支持的操作是向它写一个String。
   * 对于同样的操作，一个OutputChannel[AnyRef]也能够完成。
   * 因此，我们可以安全地用一个OutputChannel[AnyRef]来替换OutputChannel[String]。
   * 与之相对应，在要求OutputChannel[AnyRef]的地方用OutputChannel[String]替换则是不安全的。
   * 毕竟，我们可以向OutputChannel[AnyRef]传递任何对象，而OutputChannel[String]要求所有被写的值都是字符串。
   *
   * 上述推理指向类型系统设计的一个通用原则∶
   * @如果在任何要求类型U的值的地方都能用类型T的值替换，我们就可以安全地假设类型T是类型U的子类型。这被称作里氏替换原则 （Liskov SubstitutionPrinciple)。
   * @如果类型T支持与类型U一样的操作，而类型T的所有操作与类型U中相对应的操作相比，要求更少且提供的功能更多，该原则就是成立的。
   * @在输出通道的例子中，OutputChannel[AnyRef]可以是OutputChannel[String]的子类型，因为这两个类型都支持相同的write操作，
   * @而这个操作在OutputChannel[AnyRef]中的要求比在OutputChannel[String]中的要求更少。"更少”的意思是前者只要求入参是AnyRef，而后者要求入参是String。
   *
   * @有时候，协变和逆变会同时出现在同一个类型中。一个显著的例子是Scala的函数特质。
   * @举例来说，当我们写下函数类型A=>B，Scala会将它展开成Function1[A,B]。
   * @标准类库中的Function1同时使用了协变和逆变:Function1特质在函数入参类型S上逆变，而在结果类型T上协变，如示例img_3.png所示。
   * @这一点满足里氏替换原则，因为入参是函数对外的要求，而结果是函数向外提供的返回值。
   */

  /**
   * 示例img_4.png 给出了函数参数型变的展示。
   * 在这里，Publication类包含了一个参数化的字段title，类型为String。
   * Book类扩展了Publication类并将它的字符串参数String转发给超类的构造方法。
   * Library单例对象定义了一个书的集和一个printBookList方法。
   * 该方法接收一个名称为info的函数，函数的类型为 Book => AnyRef。换句话说，printBookList方法的唯一参数是一个接收Book入参并返回AnyRef的函数。
   * Customer对象定义了一个getTitle方法。这个方法接收一个Publication作为其唯一参数并返回一个 String，也就是传入的Publication的标题。
   *
   * 现在我们来看一下Customer对象的最后一行。这一行调用了Library对象的printBookList方法，并将getTitle方法打包在一个函数值中传入:
   *
   * 这一行能够通过类型检查，尽管函数的结果类型String是printBookList方法的info参数的结果类型AnyRef的子类型。
   * 这段代码能够通过编译是因为函数的结果类型被声明为协变的（示例18.8中的+T)。如果我们看一下printBookList方法的实现，就能明白为什么这是讲得通的。
   *
   * 现在我们来考查传入printBookList方法的函数的参数类型。
   * 虽然printBookList的参数类型声明为Book，但是我们传入的getTitle函数接收Publication，这是Book的一个超类型( supertype)。
   * 之所以这样是可行的，背后的原因在于: printBookList方法的参数类型是Book，因此printBookList方法的方法体只能给这个函数传入Book，
   * 而getTitle函数的参数类型是Publication，这个函数的函数体只能访问其参数p，也就是那些定义在Publication中的成员。
   * 由于Publication中声明的所有方法都在子类Book中可用，一切都应该可以工作，这就是函数参数类型逆变的意义，可参考图 img_5.png。
   */

  /**
   * 示例中 的代码之所以能通过编译，是因为Publication =>String是Book => AnyRef的子类型。
   * 由于Function1的结果类型定义为协变的，图中右部显示的两个结果类型的继承关系与中部的函数的继承关系的方向是相同的。
   * 而由于Function1的参数类型定义为逆变的，图中左部显示的两个参数类型的继承关系与函数的继承关系的方向是相反的。
   */

}
