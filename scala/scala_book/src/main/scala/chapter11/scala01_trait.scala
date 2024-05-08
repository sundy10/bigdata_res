package chapter11

object scala01_trait extends App {

  /**
   * 第11章 特质
   *
   * @特质是Scala代码复用的基础单元。
   * 特质将方法和字段定义封装起来，然后通过将它们混入（mix in)类的方式来实现复用。
   * 它不同于类继承，类继承要求每个类都继承自一个（明确的）超类，而类可以同时混入任意数量的特质。
   * 本章将展示特质的工作原理并给出两种最常见的适用场景:将“瘦”接口拓展为“富”接口，以及定义可叠加的修改。
   * 本章还会展示如何使用Ordered特质，以及特质和其他语言中的多重继承的对比。
   */

  /**
   * 11.1 特质的定义与类的定义很像，除了关键字trait。参考示例11.1
   */
  trait Philosophical {
    def philosophize = "I consume memory, therefore I am!"
  }

  /**
   * 该特质的名称为Philosophical。它并没有声明一个超类，因此与类―样，有一个默认的超类AnyRef。
   * 它定义了一个名称为philosophize的方法，这个方法是具体的。这是一个简单的特质，只是为了展示特质的工作原理。
   */

  /**
   * 一旦特质被定义好，我们就可以用extends或with关键字将它混入类中。
   * Scala程序员混入特质，而不是从特质继承，因为混入特质与其他许多编程语言中的多重继承有重要的区别。
   * 这个问题在11.4节还会详细探讨。举例来说，示例11.2展示了一个用extends关键字混入Philosophical特质的类。
   */
//  class Frog extends Philosophical {
//    override def toString: String = "green"
//  }

  /**
   * 可以用extends关键字来混入特质，
   * 在这种情况下，隐式地继承了特质的超类。
   * 例如，在示例11.2中，Frog类是AnyRef类的子类（因为AnyRef类是Philosophical特质的超类)，并且混入了Philosophical特质。
   * 从特质继承的方法与从超类继承的方法用起来一样。参考如下的例子:
   */
  val frog:Frog = new Frog
  println(frog.philosophize)

  /**
   * 特质也定义了一个类型。下面是一个Philosophical被用作类型的例子:
   */
  val phol:Philosophical = frog
  println(phol.philosophize)

  /**
   * 这里变量phil的类型是Philosophical，这是一个特质。因此,phil可以由任何混入了Philosophical特质的类的对象初始化。
   */

  /**
   * 如果你想要将特质混入一个显式继承自某个超类的类，则可以用extends关键字来给出这个超类，并用逗号（或with关键字）来混入特质。示例11.3给出了一个例子。
   * 如果你想混入多个特质，则可以用逗号(或with关键字）进行添加。
   * 例如，如果有一个HasLegs特质，则可以像示例11.4所展示的那样同时混入Philosophical和HasLegs特质。
   */
  class Animal
  trait HasLegs
  class Frog extends Animal,Philosophical,HasLegs{
    override def toString: String = "green"
  }

  /**
   * 在目前为止的示例中，Frog类从Philosophical特质继承了philosophize方法的实现。
   * Frog类也可以重写philosophize方法。重写的语法与重写超类中声明的方法看上去一样。参考下面这个例子:
   */
  class Frog1 extends Animal,Philosophical{
    override def toString: String = "green"

    override def philosophize: String = s"It ain't easy being $this!"//当绿色(的动物)太难了！
  }

  /**
   * 由于这个新的Frog类定义仍然混入了Philosophical特质，因此仍然可以用同一个该类型的变量使用它。
   * 不过由于Frog类重写了Philosophical特质的philosophize方法，因此当你调用这个方法时，将得到新的行为:
   */
  val phrog:Philosophical = new Frog1
  println(phrog.philosophize)

  /**
   * 至此，你可能会总结出，特质很像是拥有具体方法的Java接口，不过其能做的实际上远不止这些。
   * 比如，特质可以声明字段并保持状态。事实上，在特质定义中可以做任何在类定义中做的事，语法也完全相同。
   */

  /**
   * @类和特质的关键区别在于，类中的super调用是静态绑定的，而特质中的super调用是动态绑定的。
   * @如果你在类中编写"super.toString”这样的代码，则会确切地知道实际调用的是哪一个实现。
   * @而如果在特质中编写同样的代码，在定义特质的时候，想要通过super调用的方法实现并没有被定义。
   * @被调用的实现在每次该特质被混入某个具体类时都会重新判定。
   * 这里的super调用看上去有些奇怪的行为是特质能实现可叠加修改(stackable modification)的关键，
   * 我们将在11.3节介绍这个概念。解析super调用的规则将在11.4节给出。
   */
}
