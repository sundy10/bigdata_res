package chapter23

object scala03_main {

  /**
   * 23.3 主方法
   *
   * 在第2章的第2步中，我们曾提到可以用@main注解来定义Scala的主方法。这里有一个例子:
   * 如第2章所示，可以通过运行scala命令并给出源文件的名称( echoargs. scala）来执行这个脚本:
   */
  @main def echo(args: String*) =
    println(args.mkString(" "))

  /**
   * 或者,也可以通过编译这个源文件并再次运行scala命令来将它作为应用程序执行，不过这次给出主方法的名称:
   * $ scalac echoargs.scala
   *
   * $ scala echo Running as an application
   * Running as an application
   */

  /**
   * 虽然到目前为止展示的所有方法都接收一个String* 的重复参数，但是这并不是必需的。
   * 在Scala中，主方法可以接收任意数量和类型的参数。例如，如下的主方法接收一个字符串和一个整数:
   */
  @main def repeat(word: String, count: Int) =
    val msg =
      if count > 0 then
        val words = List.fill(count)(word)
        words.mkString(" ")
      else
        "Please enter a word and a positive integer count."
    println(msg)

  /**
   * 有了这个主方法声明，当运行repeat主方法时，就必须在命令行中给出一个字符串和一个整数:
   * $ scala repeat hello 3
   * //hello hello hello
   */

  /**
   * Scala是如何知道要将命令行中的字符串"3"转换成Int 3的呢?
   * 它使用的是名称为FromString 的类型族，这是scala.util.CommandLineParser的成员，其定义如示例23.6所示。
   *
   * //FromString类型族
   * trait FromString[T]:
   * def fromString(s:String):T
   */

  /**
   * Scala标准类库针对常见类型(包括String和Int)都定义了FromString上下文参数实例。
   * 这些上下文参数实例定义在FromString的伴生对象中。
   * 如果你想要编写一个接收自定义类型的主方法，则可以通过为这个自定义类型声明一个FromString上下文参数实例来实现。
   */

  /**
   * 例如，假设你想用第三个代表“情绪”的命令行参数来增强repeat主方法，
   * 其可选值是惊讶（surprised)、愤怒（ angry)和中立(neutral）中的一个。
   * 那么，你可能倾向于定义一个代表情绪的Mood枚举，如示例23.7所示。
   */
  enum Mood:
    case Surprised, Angry, Neutral

  /**
   * 有了这个枚举定义，就可以把repeat增强为接收Mood枚举作为第三个参数的主方法，如示例23.8所示。
   */

  /**
   * 唯一还剩下的一步是，通过定义Mood枚举的FromString上下文参数实例来告诉编译器如何将命令行参数字符串转换成Mood枚举。
   * 放置这个实例的一个不错的选择是Mood伴生对象，因为编译器会在查找FromString[Mood]时自动检索这个位置。示例给出了一个可能的实现。
   */
  val errmsg =
    "Please enter a word, a positive integer count, and \n" +
      "a mood (one of 'Angry' , 'Surprised' , or 'Neutral')"

  //接收定制类型的主方法
  @main def repeat1(word: String, count: Int, mood: Mood) =
    val msg =
      if count > 0 then
        val words = List.fill(count)(word.trim)
        val punc =
          mood match {
            case Mood.Angry => "!"
            case Mood.Surprised => "?"
            case Mood.Neutral => " "
          }
        val sep = punc + " "
        words.mkString(sep) + punc
      else errmsg
    println(msg)

  //针对Mood伴生对象的FromString上下文参数实例
  object Mood:

    import scala.util.CommandLineParser.FromString

    given moodFromString: FromString[Mood] with
      def fromString(s: String): Mood =
        s.trim.toLowerCase match {
          case "angry" => Mood.Angry
          case "surprised" => Mood.Surprised
          case "neutral" => Mood.Neutral
          case _ => throw new IllegalArgumentException(errmsg)
        }

  /**
   * 有了这个FromString[Mood]定义,就可以运行这个带情绪的repeat主方法了:
   * $ scala repeat hello 3 angry
   * $ hello hello hello
   */

  /**
   * 基于类型族的实现是为主方法解析命令行参数的不错的选择，因为该服务仅对特定的类型有用，除这个用途之外，
   * 它们之间并不存在任何关联。
   *
   * @除了Sting和Int，FromString伴生对象还定义了针对Byte、Short、Long、Boolean、Float和Double的FromString上下文参数实例。
   * 再加上示例23.9的针对Mood伴生对象的FromString上下文参数实例，组成FromString类型族的类型集合如图23.4所示。
   */
}
