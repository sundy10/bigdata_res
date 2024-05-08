package chapter20

import com.sun.istack.internal.NotNull

object scala05_init_abstract_val extends App {

  /**
   * 20.5 初始化抽象的val
   *
   * @抽象的val有时会承担超类参数的职能:它们允许我们在子类中提供那些在超类中缺失的细节。
   * @这对特质而言尤其重要，因为特质并没有让我们传入参数的构造方法。因此，通常来说对于特质的参数化是通过在子类中实现抽象的val来完成的。
   */
  //作为例子，我们接下来对第6章的Rational类做一些重构，改成特质:
//  trait RationalTrait:
//    val numerArg: Int
//    val denomArg: Int

  /**
   * 第6章的Rational类有两个参数:表示有理数分子的n和表示有理数分母的d。
   * 这里的RationalTrait特质则定义了两个抽象的val:numerArg和denomArg。
   * 要实例化一个该特质的具体实例，需要实现抽象的val定义。例如:
   */
//  new RationalTrait :
//    val numerArg: Int = 1
//    val denomArg: Int = 2

  /**
   * 这里的new关键字出现在特质名称RationalTrait之前，然后是冒号和缩进的类定义体。
   * 这个表达式交出的是一个混入了特质并由定义体定义的匿名类(anonymous class)的实例。
   * 这个特定匿名类的实例化的作用与new Rational(1，2)创建实例的作用相似。
   *
   * 不过这种相似性并不完美。表达式初始化的顺序有一些细微的差异。当我们写下:
   * new Rational(expr1, expr2)
   *
   * @expr1和expr2这两个表达式会在Rational类初始化之前被求值，这样expr1和expr2的值对于Rational类的初始化过程可见。
   *
   * 对特质而言，情况正好相反。当我们写下:
   * new RationalTrait :
   * val numerArg: Int = expr1
   * val denomArg: Int = expr2
   * @expr1和expr2这两个表达式是作为匿名类初始化过程的一部分被求值的，但是匿名类是在RationalTrait特质之后被初始化的。
   * @因此，在RationalTrait特质的初始化过程中，numerArg和denomArg的值并不可用（更确切地说，对两个值中任何一个的选用都会交出类型为Int的默认值，0)。
   * @对于前面给出的RationalTrait特质来说，这并不是一个问题，因为特质的初始化过程并不会用到numerArg和denomArg这两个值。
   * @不过，在示例20.4的RationalTrait变种中，定义了正规化的分子和分母，这就成了问题。
   */

  /**
   * 如果用简单字面量之外的表达式作为分子和分母来实例化这个特质，将得到一个异常:
   * val x=2
   * new RationalTrait :
   * val numerArg: Int = 1 * x
   * val denomArg: Int = 2 * x
   */

  //  trait RationalTrait:
  //    val numerArg:Int
  //    val denomArg:Int
  //    require(denomArg!=0)
  //    private val g =gcd(numerArg,denomArg)
  //    val number =numerArg /g
  //    val denom = denomArg /g
  //    private def gcd(a:Int,b:Int):Int=
  //      if(b==0) a else gcd(b,a%b)
  //    override def toString: String = s"$number/$denom"

  /**
   * @本例抛出异常是因为denomArg在RationalTrait特质初始化的时候还是默认值0，这让require的调用失败了。
   *
   * @这个例子展示类参数和抽象字段的初始化顺序并不相同。类参数在传入类构造方法之前被求值（传名参数除外)。
   * @而在子类中实现的val定义则是在超类初始化之后被求值的。
   *
   * 既然你已经理解了为何抽象的val与参数行为不同，那么最好也知道一下如何应对这个问题。
   * 有没有可能定义一个能被健壮地初始化的RationalTrait特质，而不用担心未初始化字段带来的错误呢?
   * @事实上，Scala提供了两种可选方案来应对这个问题:特质参数化字段(trait parametric field）和惰性(lazy)的val。
   */

  /**
   * @特质参数化字段
   *
   * 第一种方案为特质参数化字段，
   * @让我们在特质自身被初始化之前计算字段的值。只需要将字段定义为参数化字段即可，也就是给特质参数加上val，参考示例20.5。
   * 可以像示例20.6这样来创建特质参数化字段:
   */

    trait RationalTrait(val numerArg:Int,val denomArg:Int):
      require(denomArg!=0)
      private val g =gcd(numerArg,denomArg)
      val numer =numerArg /g
      val denom = denomArg /g
      private def gcd(a:Int,b:Int):Int=
        if(b==0) a else gcd(b,a%b)
      override def toString: String = s"$numer/$denom"

    val x= 2
    new RationalTrait (1*x,2*x){}


  /**
   * 特质参数化字段并不局限于匿名类，它们也可以被用在对象或带名称的子类上，参考示例20.7和示例20.8。
   * 示例20.8给出的RationalClass类展示了类参数如何在其超特质初始化过程中可见的通常做法。
   */
  object TwoThirds extends RationalTrait(2,3)

//  class RationalClass(n:Int, d:Int) extends RationalTrait(n,d):
//    def + (that:RationalClass) =new RationalClass(
//      //...
//    )


  /**
   * 惰性的val
   *
   * @我们可以用特质参数化字段来精确模拟类构造方法入参的初始化行为。
   * @不过有时候我们可能更希望系统自己就能确定应有的初始化顺序。可以通过将val定义做成惰性的来实现。
   * @如果我们在val定义之前加上lazy修饰符，则右侧的初始化表达式只会在val第一次被使用时求值。
   *
   * 例如，可以像下面这样用val定义一个Demo对象:
   */
  object Demo{
    val x={println("initializing x");"done"}
  }


  //先引用Demo，再引用Demo.x:

  /**
   * @正如你所看到的，一旦引用Demo，字段就被初始化了。对x的初始化是Demo初始化的一部分。不过，如果我们将x定义为惰性的，情况就不同了 只有第一次使用时被初始化:
   */
  object Demo1{
    lazy val x1={println("initializing x");"done"}
  }


  /**
   * 现在，Demo的初始化并不涉及对x的初始化。对x的初始化被延迟到第一次访问x的时候。
   * @这与对x用def定义成无参方法的情况类似。不过，不同于def，惰性的val永远不会被求值多次。事实上，在对惰性的val首次求值之后，其结果会被保存起来，并且在后续的使用中，都会复用这个相同的val。
   *
   * 从这个例子看，像Demo这样的对象本身的行为也似乎与惰性的val类似，也是在第一次被使用时按需初始化的。这是对的。
   * @事实上，对象定义可以被看作惰性的val的一种简写，即用匿名类来描述对象内容。
   *
   * 通过使用惰性的val，我们可以重新编写RationalTrait特质，如示例20.9所示。在这个新的特质定义中，所有具体字段都被定义为惰性的。
   * @与示例20.4中的RationalTrait特质定义相比还有一个不同，就是require子句从特质的定义体中移到了私有字段g的初始化代码中，而这个字段计算的是numerArg和denomArg的最大公约数。
   * @有了这些改动，在LazyRationalTrait特质被初始化的时候，已经没有什么需要做的了，所有的初始化代码现在都已经是惰性的val的右侧的一部分了。因此，在类定义之后初始化LazyRationalTrait特质的抽象字段是安全的。
   */
  trait LazyRationalTrait1{
    val numerArg:Int
    val denomArg:Int
    lazy val numer =numerArg /g
    lazy val denom = denomArg /g

    override def toString: String = s"$numer/$denom"

    private val g =
      require(denomArg!=0)
      gcd(numerArg,denomArg)

    private def gcd(a:Int,b:Int):Int=
      if(b==0) a else gcd(b,a%b)
  }

  /**
   * 我们并不需要预初始化任何内容，
   * @但是有必要跟踪一下上述代码最终输出1/2这个字符串的初始化过程。
   *
   *
   *  1. LazyRationalTrait特质的一个全新示例被创建，LazyRationalTrait特质的初始化代码被执行。这段初始化代码是空的，这时LazyRationalTrait特质还没有任何字段被初始化。
   *
   *  2. 由new表达式定义的匿名子类的主构造方法被执行。这包括用2初始化numerArg，以及用4初始化denomArg.
   *
   *  3. 编译器调用了被构造对象的toString方法，以便打印出结果值。
   *
   *  4. 在LazyRationalTrait特质的toString方法中,numer被首次访问，因此，其初始化代码被执行。
   *
   *  5.numer的初始化代码访问了私有字段g，因此g随之被求值。求值过程中会访问numerArg和denomArg，这两个变量已经在第2步被定义。
   *
   *  6. toString方法访问denom的值，这将引发denom的求值。对denom的求值会访问denomArg和g的值。g的初始化代码并不会被重新求值，因为它已经在第5步完成了求值。
   *
   *  7.结果字符串"1/2"被构造并打印出来。
   */

  /**
   * 注意，g的定义在LazyRationalTrait特质中出现在numer和denom的定义之后。尽管如此，由于所有3个值都是惰性的，g将在numer和denom的初始化完成之前被初始化。
   *
   * @这显示出惰性的val的一个重要属性:它的定义在代码中的文本顺序并不重要，因为它的值会按需初始化。因此，惰性的val可以让程序员从如何组织val定义来确保所有内容都在需要时被定义的思考中解脱出来。
   *
   * 不过，这个优势仅在惰性的val的初始化既不产生副作用也不依赖副作用的时候有效。
   * 在有副作用参与时，初始化顺序就开始变得重要了。在这种情况下，要跟踪初始化代码运行的顺序，可能会变得非常困难，
   * 就像前一例所展示的那样。因此，惰性的val是对函数式对象的完美补充。对函数式对象而言，初始化顺序并不重要，只要最终所有内容都被正常初始化即可。
   * 而对那些以指令式风格为主的代码而言，惰性的val就没那么适用了。
   */

  /**
   * @惰性函数式编程语言
   */
  /**
   * Scala并不是首个利用惰性定义和函数式代码的完美结合的编程语言。事实上，存在这样整个类目的“惰性函数式编程语言”，
   * 其中所有的值和参数都是被惰性初始化的。这一类编程语言中最有名的是Haskell [SPJ02]。
   */










}
