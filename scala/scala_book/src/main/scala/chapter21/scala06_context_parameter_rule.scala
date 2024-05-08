package chapter21

object scala06_context_parameter_rule extends App {


  class PreferredPrompt(val preferredPrompt: String)


  /**
   * 21.6 上下文参数的规则
   *
   * @上下文参数是那些用using子句定义的参数。编译器可以选择通过插入上下文参数的方式来解决因缺少参数列表产生的任何错误。
   * 例如，如果someCall(a)不能通过类型检查，则编译器可能会把它变更为someCal(a)(b)，
   * 前提是缺失的参数列表被标记为using 且 b是一个上下文参数。(当编译器内部执行这个重写动作时，并不需要对显式传入的参数加上using)
   * 这个变更可能可以修正某个程序，让它能够通过类型检查并正确地运行。如果显式地传入b会形成样板代码，则从源码中省去这个传入能让代码更清晰。
   */

  /**
   * 上下文参数受如下的一般规则管控。
   */

  /**
   * 标记规则:只有被标记为given的定义可用。given关键字用来标记那些编译器会被用作上下文参数的声明。这里有一个上下文参数定义的例子:
   */
  given amysPrompt:PreferredPrompt = PreferredPrompt("hi> ")

  /**
   * 编译器只有在amysPrompt被标记为given时才将greet("Amy")变更为greet("Amy")(amysPrompt)。
   * 这样一来，就避免了编译器随机地挑选某个碰巧出现在作用域内的值，
   * 并且避免隐式地将它插入代码中所引发的困惑。编译器只会在被显式地标记为given的那些定义中选取(上下文参数)。
   */

  /**
   * 可见性规则:被插入的上下文参数实例必须以单个标识符的形式出现在作用域内，或者与参数类型的相关类型关联。
   * Scala编译器只会考虑那些可见的上下文参数。因此，要让某个上下文参数可见，必须以某种方式让它可见。
   * 不仅如此，除了一个例外情况，上下文参数必须以单个标识符的形式出现在语法作用域内。
   * 编译器并不会插入以prefslib.AmysPrefs.amysPrompt形式存在的上下文参数。
   * 举例来说，编译器不会将greet("Amy")改写为greet("Amy")(prefslib.AmysPrefs.amysPrompt)。
   * 如果你想让prefslib.AmysPrefs.amysPrompt作为上下文参数可见，则需要引入这个上下文参数，这样就会让它以单个标识符的形式可见。
   * 一旦引入了这个上下文参数，编译器就可以选择使用单个标识符的形式来应用它，
   * 即greet("Amy")(amysPrompt)。事实上，对类库而言，提供一个包含了若干个有用的上下文参数的Preamble对象是很常见的做法。
   * 这样一来，使用类库的代码就可以用一行代码“import Preamble.given”来访问这个类库的上下文参数了。
   * (对Preamble.given的引入同时会将那些在Preamble对象中声明的匿名上下文参数的合成名称以单个标识符的形式带入语法作用域内。)
   */

  /**
   * 上述单个标识符规则有一个例外。如果编译器在当前的语法作用域内找不到可用的上下文参数时，
   * 就会立即在与上下文参数类型相关的类型的伴生对象中继续查找。
   * 举例来说，如果你尝试在不显式地给出类型为Ordering[Rational]的上下文参数的情况下调用某个（需要Ordering [Rational]的）方法时，
   * @则编译器将会在Ordering和Rational伴生对象，以及它们的超类型的伴生对象中查找。
   * 因此，你可以在Ordering或Rational伴生对象中任选一个，将上下文参数打包在其相应的伴生对象中。
   * 由于Ordering伴生对象是标准类库的一部分，因此Rational伴生对象就是最好的选择:
   */
  import scala04_type_family_context_parameter.Rational
  object Rational:
    given rationalOrdring: Ordering[Rational] with
      def compare(x: Rational, y: Rational) =
        if x.numer * y.denom < x.denom * y.numer then -1
        else if x.numer * y.denom > x.denom * y.numer then 1
        else 0

  /**
   * 对于本例中的rationalOrdering上下文参数，我们可以说它与Rational类型有关联（associated)。
   * 每当编译器需要合成类型为Ordering[Rational]的上下文参数时，它都能找到这个有关联的上下文参数。因此，我们并不需要在程序中单独引入这个上下文参数。
   *
   * 可见性规则有助于对代码进行模块化的推理。当你阅读某个文件中的代码时，
   * 唯一需要参考其他文件的地方是那些要么被引入，要么通过完整的名称被显式引用的标识。
   * 对上下文参数而言，这个好处至少与显式编写的代码一样重要。
   * 如果上下文参数在整个系统范围内生效，则为了理解某个文件，就必须知道程序中任意位置的所有上下文参数。
   *
   * 显式优先规则:只要代码能够按原始状态通过类型检查，编译器就不会尝试使用上下文参数。
   * 编译器不会改写已经工作的代码。对这个原则稍加推演，就总是可以通过使用using显式地给出上下文参数来替换那些被隐式提供的上下文参数，
   * 从而在代码变长的同时减少明显的歧义。你可以根据不同场景的实际情况在这些选择中取舍。
   * 每当你看到那些看起来重复而冗长的代码时，上下文参数都有机会帮助你减少这些烦人的冗余;
   * 而每当你发现代码很干甚至晦涩难懂时，也可以显式地用using传入上下文参数(来减少歧义)。
   * @留多少上下文参数给编译器帮你插入，说到底是一个（代码）风格问题。
   */

  /**
   * @给上下文参数起名
   *
   * 我们可以给上下文参数起任何名字。上下文参数的名字只在两种情况下是重要的:
   *    当你想要显式地用using关键字传参时，
   *    以及判断在程序中的任意位置有哪些上下文参数可用时。
   *
   *  为了更好地理解第二种情况，假设你想要使用示例21.6的TomsPrefs 单例对象中的prefPromptDrd上下文参数，但又不想使用prefDrinkOrd上下文参数，
   *  则可以通过只引入一个上下文参数而不引入另一个上下文参数来达成:
   */
  import scala05_context_parameter_import.TomsPrefs.prefPromptOrd as prefPrompt

  /**
   * 在本例中，带名字的上下文参数是很有用的，因为这样一来,你就可以用名字来有选择地引入其中一个而不是另一个。
   */
}
