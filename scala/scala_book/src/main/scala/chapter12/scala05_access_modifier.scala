package chapter12

object scala05_access_modifier extends App {

  /**
   * 12.5 访问修饰符
   *
   * 包、类或对象的成员可以标记上private和protected这样的访问修饰符。
   * 这些修饰符将对成员的访问限定在特定的代码区域内。Scala对访问修饰符的处理大致上与Java保持一致，不过也有些重要的区别，在本节会讲到。
   */

  /**
   * @私有成员
   *
   * Scala对私有成员的处理与Java类似。被标记为private的成员只在包含该定义的类或对象内部可见。
   * 在Scala中，这个规则同样适用于内部类。Scala在一致性方面做得比Java更好，但做法不一样。参考示例12.10。
   */

  class Outer:
    class Inner:
      private def f="f"
      class InnerMost:
        f       //可行

    //(new Inner).f     //错误: 无法访问f


  /**
   * 在Scala中，像(new Inner).f这样的访问方式是非法的，因为f在Inner类中被声明为private且对f的调用并没有发生在Inner类内部。
   * 而第一次在InnerMost类中访问f是可行的，因为这个调用发生在lnner类内部。
   * @Java则对这两种访问都允许，因为在Java中可以从外部类访问其内部类的私有成员。
   */

  /**
   * @受保护的成员
   *
   * Java相比，Scala对protected成员的访问也更严格。
   * @在Scala中，只能从定义Protected成员的子类访问该成员。而Java允许同一个包内的其他类访问这个类的受保护成员。
   * (可以用限定词(qualifier)，参考“保护的范围”(239页)。)
   * Scala提供了另一种方式来实现这个效果R，因此protected不需要为此放宽限制。示例12.11展示了对受保护成员的访问。
   *
   * 在示例12.11中，Sub类对f的访问是可行的，因为在Super类中f被标记为protected，而Sub类是Super类的子类。
   * 与之对应地，Other类对f的访问是被禁止的，因为Other类并不继承自Super类。在Java中，后者依然被允许，因为Other类与Sub类在同一个包中。
   */
  class Super:
    protected def f="f"

  class Sub extends Super:
    f
  class Other{}
    //new Super.f   //错误: 无法访问f


  /**
   * @公共成员
   *
   * Scala并没有专门的修饰符用来标记公共成员:任何没有被标记为private或protected的成员都是公共的。公共成员可以从任何位置被访问。
   */

  /**
   * @保护的范围
   *
   * 可以用限定词对Scala中的访问修饰符机制进行增强。形如private[X]或protected[X]的修饰符的含义是，
   * “直到X”对此成员的访问都是私有的或受保护的，其中X表示某个包含该定义的包、类或单例对象。
   *
   * @带有限定词的访问修饰符允许我们对成员的可见性做非常细粒度的控制。尤其是它允许我们表达Java中访问限制的语义，如包内私有、包内受保护或到最外层嵌套类范围内私有等。
   * 而这些用Scala中简单的修饰符是无法直接表达出来的。这种机制还允许我们表达那些无法在Java中表达的访问规则。
   *
   * 示例12.12给出了使用多种访问限定词的用法。在示例12.12中，Navigator类被标记为private[bobsrocket]，
   * 其含义是这个类对bobsrockets包内的所有类和对象都可见。具体来说,Vehicle对象中对Navigator类的访问是允许的，
   * 因为Vehicle对象位于launch包，而launch包是bobsrockets包的子包。另一方面，所有bobsrockets包之外的代码都不能访问Navigator类。
   *
   * 这种机制在那些跨多个包的大工程中非常有用。可以定义对工程中某些子包可见但对外部不可见的实体。(通过JDK 9的模块系统,Java现在也能支持这个机制了。)
   *
   * 当然，private的限定词也可以是直接包含该定义的包。比如，示例12.12中Vehicle对象的guide成员变量的访问修饰符，这样的访问修饰符与Java的包内私有访问是等效的。
   *
   * 所有的限定词也可以应用在protected上，作用与private上的限定词一样。
   * 也就是说，如果我们在C类中使用protected[X]这个修饰符，则c类的所有子类，以及X表示的包、类或对象中，都能访问这个被标记的定义。
   * 例如，对于示例12.12中的useStarChart方法，Navigator类的所有子类及navigation包中的代码都可以访问。这样一来，这里的protected含义就与Java的protected含义是完全一样的。
   */

  /**
   * private的限定词也可以引用包含它的类或对象。例如，示例12.12中LegOfJourney类的distance变量被标记为private[Navigator],
   * 因此它在整个Navigator类中都可以被访问。这就实现了与Java中内部类的私有成员一样的访问功能。当C是最外层的嵌套时,private[C]与Java的private所实现的效果是一样的。
   *
   * 总结一下，表12.1列出了private的限定词的作用。每一行都给出了一个带限定词的私有修饰符，以及如果将这样的修饰符加到示例12.12中LegOfJourney类的distance变量上代表什么意思。
   */

  /**
   * @可见性和伴生对象
   *
   * @在Java中，静态成员和实例成员同属一个类，因此访问修饰符对它们的应用方式是统一的。你已经知道Scala没有静态成员，它是用伴生对象来承载那些只存在一次的成员的。
   * 例如，示例12.13中的Rocket对象就是Rocket类的伴生对象。
   */

  class Rocket:
    import Rocket.fuel
    private def canGoHomeAgain = fuel >20
  object Rocket:
    private def fuel =10
    def chooseStrategy(rocket: Rocket) =
      if rocket.canGoHomeAgain then
        goHome()
      else
        pickAStar()
    def  goHome() ={}
    def  pickAStar() ={}

  /**
   * @Scala的访问规则在对private和protected的处理上给伴生对象和类保留了特权。
   * @一个类会将它的所有访问权与它的伴生对象共享，反过来也一样。具体来说，一个对象可以访问它的伴生类的所有私有成员，同样地，一个类也可以访问它的伴生对象的所有私有成员。
   *
   * 举例来说，示例中的Rocket类可以访问fuel方法，而该方法在Rocket对象中被标记为private。同理，Rocket对象也能访问Rocket类中的私有方法canGoHomeAgain。
   *
   * Scala和Java在修饰符方面的确很相似，不过有一个重要的例外:protected static。
   * 在Java中，C类的protected static成员可以被C类的所有子类访问。而对Scala的伴生对象而言，protected的成员没有意义，因为单例对象没有子类。
   */



}
