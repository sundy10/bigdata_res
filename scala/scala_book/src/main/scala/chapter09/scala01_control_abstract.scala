package chapter09

import java.io.File

object scala01_control_abstract extends App {

  /**
   * 第九章  控制抽象
   *
   * 第7章指出，Scala并没有很多内建的控制抽象，因为它提供了让用户自己创建控制抽象的功能。
   * 第8章介绍了函数值。本章将向你展示如何应用函数值来创建新的控制抽象。
   * 在这个过程中，你还将学习到柯里化和传名参数。
   */

  /**
   * 9.1 减少代码重复
   *
   * @所有的函数都能被分解成在每次函数调用时都一样的公共部分和在每次调用时都不一样的非公共部分。
   * 公共部分是函数体，而非公共部分必须通过实参传入。
   * 当你把函数值当作入参时，这段算法的非公共部分本身又是另一个算法。
   * 每当这样的函数被调用时，你都可以传入不同的函数值作为实参，被调用的函数会（在由它选择的时机)调用传入的函数值。
   * 这些高阶函数(higher-order function)，即那些 接收函数作为参数 的函数，让你有额外的机会来进一步压缩和简化代码
   */

  /**
   * 高阶函数的好处之一是可以用来 创建减少代码重复的控制抽象。
   * 例如，假设你在编写一个文件浏览器，并且你打算提供API给用户来查找匹配某个条件的文件。
   * 首先，添加一个机制用来查找文件名是以指定字符串结尾的文件。
   * 比如，这将允许用户查找所有扩展名为".scala”的文件。
   * 你可以通过在单例对象中定义一个公共的filesEnding方法的方式来提供这样的API，就像这样:
   */
  object FileMatcher {
    private def filesHere = (new File("C:\\Users\\yosuke\\Desktop\\代码1")).listFiles()

    //    def filesEnding(query:String)=
    //      (for file <- filesHere if file.getName.endsWith(query) yield file).toBuffer


    /**
     * 这个filesEnding方法用私有的助手方法filesHere来获取当前目录下的所有文件，然后基于文件名是否以用户给定的查询条件结尾来过滤这些文件。
     * 由于filesHere方法是私有的， filesEnding方法是FileMatcher(也就是你提供给用户的API)中定义的唯—一个能被访问到的方法。
     */

    /**
     * 到目前为止，一切都很完美，暂时还没有重复的代码。
     * 不过到了后来，你决定让人们可以基于文件名的任意部分进行搜索。因为有时候用户记不住他们到底是将文件命名成了phb-important.doc、stupid-phb-report.doc、may2003salesdoc. phb，
     * 还是其他完全不一样的名字，他们只知道名字中某个地方出现了“phb”，这时这样的功能就很有用。
     * 于是，你回去给FileMatcher API添加了这个函数:
     */
    //  def  filesContaining(query:String) =
    //    (for file <- filesHere if file.getName.contains(query) yield file).toBuffer

    /**
     * 这个函数与filesEnding的运行机制没什么两样:搜索filesHere,检查文件名，如果名字匹配，则返回文件。唯一的区别是，这个函数用的是contains而不是endsWith。
     */

    /**
     * 几个月过去了，这个程序变得更成功了。
     * 终于，面对某些高级用户提出的想要基于正则表达式搜索文件的需求，你屈服了。
     * 这些喜欢"偷懒”的用户有着大量拥有上千个文件的巨大目录，他们想实现类似找出所有标题中带有“oopsla”字样的PDF文件的操作。
     * 为了支持他们，你编写了下面这个函数:
     */
    //  def filesRegex(query:String) =
    //    (for file <- filesHere if file.getName.matches(query) yield file).toBuffer

    /**
     * 有经验的程序员会注意到这些函数中不断重复的代码，那么，有没有办法将它们重构成公共的助手函数呢?
     * 按显而易见的方式来实现并不可行。你会想要做到这样的效果:
     *
     * 这种方式在某些动态语言中可以做到，但Scala并不允许像这样在运行时将代码黏在一起的操作。那怎么办呢?
     */
    //  def fileMatching(query:String,method) =
    //    for file <- filesHere if file.getName.method(query) yield file

    /**
     * 函数值提供了一种答案。
     * 虽然不能将方法名像值一样传来传去，但是可以通过传递某个帮你调用方法的函数值来达到同样的效果。
     * 在本例中，可以给方法添加一个matcher参数，该参数的唯一目的就是检查文件名是否满足某个查询条件:
     */
    def fileMatching(matcher: (String) => Boolean) =
      for file <- filesHere if matcher(file.getName) yield file

    /**
     * 在这个版本的方法中，if子句用matcher来检查文件名是否满足查询条件。
     * 但这个检查具体做什么，取决于给定的matcher。
     * 现在，我们来看matcher这个类型本身。
     * 它首先是一个函数，因此在类型声明中有一个=>符号。
     * 这个函数接收两个字符串类型的参数（分别是文件名和查询条件)，返回一个布尔值，因此这个函数的完整类型是(String, String) => Boolean。
     *
     * 有了这个新的filesMatching助手方法，就可以对前面3个搜索方法进行简化，调用助手方法，传入合适的函数:
     */
    def filesEnding(query: String) =
      fileMatching(_.endsWith(query))

    def filesContaining(query: String) =
      fileMatching(_.contains(query))

    def filesRegex(query: String) =
      fileMatching(_.matches(query))

    /**
     * 本例中展示的函数字面量用的是占位符语法，这个语法在前一章介绍过，但可能对你来说还不是非常自然。
     * 所以我们来澄清一下占位符是怎么用的:filesEnding方法中的函数字面量 _.endsWth(_) 的含义与下面这段代码是一样的:
     *
     * (fileName:String,query:String)=>fileName.endsWith(query)
     */

    //这段代码已经很简化了，不过实际上还能更短。注意，这里的查询字符串被传入filesMatching方法后，
    // filesMatching方法并不对它做任何处理，只是将它传入matcher函数。这样的来回传递是不必要的，

  }

  /**
   * 这个示例展示了一等函数是如何帮助我们消除代码重复的，没有一等函数，我们很难做到这样。
   * 不仅如此，这个示例还展示了一等函数是如何帮助我们减少代码重复的。在前面的例子中用到的函数字面量，如_.endsWith(_)和_.contains(_)，
   * 都是在运行时被实例化成函数值的，它们并不是闭包，因为它们并不捕获任何自由变量。
   * 举例来说，在表达式_.endsVith(_)中用到的两个变量都是由下画线表示的，这意味着它们取自该函数的入参。
   * 因此，_.endsWith(_)使用了两个绑定变量，并没有使用任何自由变量。相
   * 反地，在示例9.1中，函数字面量_.endsWith(query)包含了一个绑定变量，即用下画线表示的那一个，以及一个名称为query的自由变量。
   * 正因为Scala支持闭包，我们才能在最新的这个例子中将query参数从filesMatching方法中移除，从而进一步简化代码。
   */

  println(FileMatcher.filesEnding(".scala").toBuffer)
}
