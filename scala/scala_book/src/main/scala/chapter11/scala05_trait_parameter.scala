package chapter11



object scala05_trait_parameter extends App {

  class Animal
  /**
   * 11.5 特质参数
   *
   * Scala 3允许特质接收值参数。
   * 定义接收参数的特质与定义接收参数的类并没有什么不同:只需要在特质名称后放置一个以逗号隔开的参数列表即可。
   * 例如，可以向示例11.10中的Philosophical特质传入一段关于哲学的总结陈述(philosophical statement)作为参数:
   */
  trait Philosophical(message:String):
    def philosophical =message

  /**
   * 既然Philosophical特质接收一个参数，那么每个子类都必须将自己的总结陈述以参数的形式传递给特质，就像这样:
   */
  class Frog extends Animal,
    Philosophical("I croak, therefore I am!")

  class Duck extends Animal,
    Philosophical("I quack, therefore I am!")

  /**
   *   简言之，在定义一个混入特质的类时，必须给出特质需要的参数值。
   *   这样一来，每个Philosophical特质的哲学都会由传入的message参数值决定:
   */
  val frog = new Frog
  println(frog.philosophical)
  val duck = new Duck
  println(duck.philosophical)

  /**
   * 简言之，在定义一个混入特质的类时，必须给出特质需要的参数值。这样一来，每个Philosophica特质的哲学都会由传入的message参数值决定:
   *
   * 特质参数紧跟在特质初始化之前求值。与类参数一样，特质参数默认只对特质体可见。
   * 因此，要在实现该特质的类中使用message参数，可以通过字段来捕获它并使它可见。
   * @Scala3用特质参数替换了Scala2中的提前初始化器(early initializer) 。
   * 这样一来，这个字段就一定会在实现类初始化的过程中完成初始化并在类中可用。
   * (与类参数一样,可以用参数化的字段来定义公共字段，并由传入的特质参数初始化。我们将在20.5节对其做更多介绍。)
   *
   * 在使用参数化特质的过程中，你可能会注意到,
   * @特质参数的规则与类参数的规则有一些细微的差异。
   * 在这两种情况下，都只能初始化一次，不过，虽然在同一个继承关系中的每个类都只能被一个（明确的）子类继承，但是特质可以被多个子类混入。
   * 在这种情况下，当定义类继承关系中所有混入该特质的最上层的类时，必须初始化这个特质。为了说明这一点，可以考虑示例11.11给出的用于描述任何有思想的动物的超类:
   *
   * 如果某个类的超类自身并不扩展该特质，则必须在定义该类时给出特质参数。例如，ProfoundAnimal类的超类是Animal，而Animal类并不扩展Philosophical特质。
   * 因此，必须在定义ProfoundAnimal类时给出特质参数。
   */

  class ProfoundAnimal extends Animal,Philosophical("In the beginning was the deed.")

  /**
   * 而如果某个类的超类扩展了该特质，就不能在定义该类时提供特质参数了。参考示例11.12。
   */

  class Frog1 extends ProfoundAnimal,Philosophical

  /**
   * Frog1类的超类ProfoundAnimal扩展了Philosophica特质并给出了message参数。
   * 在定义Frog1类时，就不能以参数的形式指定message了，因为这个参数已经被ProfoundAnimal类填充了。
 *
   * @因此，这个Frog1类将展现出源自ProfoundAnimal类对Philosophical特质初始化的行为:
   */

  val frog1 = new Frog1
  println(frog1.philosophical)

  /**
   * 最后我们要说的是，特质不能向其父特质传参。例如，下面这个扩展了Philosophical特质的PhilosophicalAnimal特质:
   */
  trait PhilosophicalAnimal extends Animal with Philosophical

  /**
   * 你可能会以为像这样来定义一只有思想的青蛙是可行的:
   *
   * class Forg2 extends PhilosophicalAnimal("I croak, therefore I am")
   *
   * 但这是行不通的。你必须在定义Frog2类时显式地将消息文本提供给Philosophical特质，就像这样:
   */
  class Forg2 extends PhilosophicalAnimal,Philosophical("I croak, therefore I am")

  //或者是这样
  //class Forg2 extends Philosophical("I croak, therefore I am"),PhilosophicalAnimal

  /**
   * 11.6 本章展示了特质的工作原理，以及如何在常见的几种场景下使用它。
   * 你看到了特质与多重继承很相似。但特质用线性化解读super ,这样做既避免了传统多重继承的某些问题，又允许你将行为叠加起来。
   * 你还看到了Ordered特质并了解了如何编写自己的增强特质。
   *
   * 既然你已经掌握了特质的这些不同的方面，那么我们有必要退一步，重新把特质当作一个整体来看。
   * 特质并不仅仅支持本章中提到的这些惯用法;它是通过继承实现复用的基础代码单元。
   * 因此，许多有经验的Scala程序员都在实现的初期阶段采用特质。每个特质都可以描述整个概念的一部分。
   * 随着设计逐步固化和稳定，这些部分可以通过特质混入，被组合成更完整的概念。
   */


}
