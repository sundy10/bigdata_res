package chapter11

object scala02_thin_fat_interface extends App {

  /**
   * 11.2 瘦接口与富接口
   *
   * @特质的一个主要用途是自动给类添加基于已有方法的新方法。也就是说，特质可以丰富一个瘦接口，让它成为富接口。
   */

  /**
   * 瘦接口和富接口代表了我们在面向对象设计中经常面临的取舍，以及在接口实现者和使用者之间的权衡。
   * 富接口有很多方法，对调用方而言十分方便。使用者可以选择完全匹配他们需求的方法。
   * 而瘦接口的方法较少，因此实现起来更容易。不过瘦接口的使用方需要编写更多的代码。
   * 由于可供选择的方法较少，他们可能被迫选择一个不那么匹配他们需求的方法，然后编写额外的代码来使用它。
   */

  /**
   * 给特质添加具体方法会让瘦接口和富接口之间的取舍变得严重倾向于富接口，因为同样的工作只用做一次。
   * 只需要在特质中实现这些方法一次，而并不需要在每个混入该特质的类中重新实现一遍。因此，与其他没有特质的语言相比，Scala中实现的富接口的代价更小。
   */

  /**
   * 要用特质来丰富某个接口，只需要定义一个拥有为数不多的抽象方法（接口中瘦的部分）和可能数量很多的具体方法（这些具体方法基于那些抽象方法编写)的特质。
   * 然后，就可以将这个增值(enrichment)特质混入某个类，在类中实现接口中瘦的部分，最终得到一个拥有完整富接口实现的类。
   */

  /**
   * 富接口能给我们带来便利的一个很典型的应用领域是对象之间的比较。
   * 当你需要通过比较两个对象来对它们排序时，如果有这样一个方法可以调用以明确需要的比较，就会很方便。
   * 如果你需要的是“小于”，则希望调用<;而如果你需要的是“小于或等于”，则希望调用<=。
   * 如果用一个瘦的比较接口，则可能只能用<，但有时可能需要编写类似“(x< y)||(x == y)”这样的代码。而一个富接口可以提供所有常用的比较操作，这样就可以直接写下如同“x <=y”这样的代码。
   */

  /**
   * 假设你用使第6章的Rationa类，然后给它添加比较操作，则可能最终会写出类似这样的代码:
   */
  class Rational(n: Int, d: Int) {
    require(d != 0)

    private val g = gcd(n.abs, d.abs)
    val numer = n / g
    val denom = d / g

    def this(n: Int) = this(n, 1)

    override def toString: String = s"$numer/$denom"

    private def gcd(a: Int, b: Int): Int = {
      if b == 0 then a else gcd(b, a % b)
    }

    def <(that: Rational) =
      this.numer * that.denom < that.numer * this.denom

    def >(that: Rational) =
      that < this

    def <=(that: Rational) = (this < that) || (this == that)

    def >=(that: Rational) = (this > that) || (this == that)

  }

  /**
   * 这个类定义了4个比较操作符（<、>、<=和>=)，这是一个经典的展示出定义富接口代价的例子。
   * 首先，注意其中的3个比较操作符都是基于第一个比较操作符来定义的。例如，>被定义为 < 的取反操作，而<=按字面意思被定义为“小于或等于”。
   * 接下来，注意所有的这3个比较操作符对于任何其他可以被比较的类来说都是一样的。有理数在<=的语义方面并没有任何特殊之处。在比较的上下文中，<= 总是被用来表示“小于或等于”。
   * 总体来说，这个类中有相当多的样板代码，并且它们在其他实现了比较操作的类中不会有什么不同。
   */

  /**
   * 由于这个问题如此普遍，因此Scala提供了专门的特质来解决。
   * 这个特质叫作Ordered。其使用方式是将所有单独的比较方法替换成compare方法。
   * Ordered特质定义了<、>、=和>=方法，而这些方法都是基于你提供的compare方法来实现的。
   * 因此，Ordered特质允许你只实现一个compare方法来增强某个类，让它拥有完整的比较操作。
   */


   class Rational_Ordered(n: Int, d: Int) extends Ordered[Rational_Ordered] {
    require(d != 0)

    private val g = gcd(n.abs, d.abs)
    val numer: Int = n / g
    val denom: Int = d / g

    def this(n: Int) = this(n, 1)

    override def toString: String = s"$numer/$denom"

    private def gcd(a: Int, b: Int): Int = {
      if b == 0 then a else gcd(b, a % b)
    }

     def compare(that: Rational_Ordered): Int = this.numer * that.denom - (that.numer * this.denom)
  }

  /**
   * 你只需要做两件事。首先，这个版本的Rational类混入了Ordered特质。
   * 与其他你看到过的特质不同，Ordered特质要求在混入时传入一个类型参数。
   * 我们在第18章之前并不会详细地探讨类型参数，不过现在你需要知道，当混入Ordered特质的时候，必须确保混入Ordered[C]，
   * 其中，C是你要比较的元素的类。在本例中，Rational类混入的是Ordered[Rational]。
   */

  /**
   * 你需要做的第二件事是定义一个用来比较两个对象的compare方法。
   * 该方法应该比较接收者（即this）和作为参数传入该方法的对象。
   * 如果两个对象相同，则该方法应该返回0;
   *
   * @如果接收者比入参小，则该方法应该返回负值;如果接收者比入参大，则该方法应该返回正值
   */

  /**
   * 每当你需要实现一个按某种比较排序的类时，都应该考虑混入Ordered特质。如果你这样做了，将会提供给类的使用方一组丰富的比较方法。
   *
   * 要小心Ordered特质并不会帮助你定义equals方法，因为它做不到。
   * 其中的问题在于，用compare方法来实现equals方法需要检查传入对象的类型，而由于（Java的）类型擦除机制，Ordered特质自己无法完成这个检查。
   * 因此，你需要自己定义equals方法，即使已经继承了Ordered特质。你可以在《Scala高级编程》中找到更多关于此话题的内容。
   */

}
