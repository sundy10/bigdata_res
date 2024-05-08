package chapter07

import java.io.File
import java.util.regex.Pattern


object scala03_for extends App {

  /**
   * Scala的for表达式是用于迭代的“瑞士军刀”，可以让你以不同的方式组合一些简单的因子来表达各式各样的迭代。
   * 它可以帮助我们处理诸如遍历整数序列的常见任务，也可以通过更高级的表达式来遍历多个不同种类的集合，
   *
   *      并根据任意条件过滤元素，生成新的集合。
   */

  val filesHere: Array[File] = (new File(".")).listFiles()
  for file <- filesHere do
    println(file)

  for ii <- 0 to  filesHere.length do
    println(ii)


  /**
   * 通过“file <- filesHere”这样的生成器(generator）语法，我们将遍历filesH ere的元素。每进行━次迭代，一个新的名称为file的val都会被初始化成一个元素的值。
   * 编译器推断出文件的类型为File，这是因为filesH ere是一个Array[File]。
   * 每进行━次迭代, for表达式的代码体———printIn(file)，就被执行一次。
   * 由于File的toString方法会返回文件或目录的名称，因此这段代码将会打印出当前目录的所有文件和子目录。
   *
   * 准确地说，在for表达式的<-符号右侧的表达式可以是任何拥有某些特定的带有正确签名的方法（如本例中的foreach)的类型。
   */

  for i <- 1 to 5 do
    println(s"Iteration ${i}")


  /**
   * 有时你并不想完整地遍历集合，但你想把它过滤成一个子集。
   * 这时你可以给for表达式添加过滤器（filter)。过滤器是for表达式的圆括号中的一个if子句。
   * 举例来说，示例7.6的代码仅列出当前目录中以".xml”结尾的那些文件:
   */

  for file <- filesHere if file.getName.endsWith(".xml") do
    println(file)


  /**
   * 若想随意包含更多的过滤器，则直接添加if子句即可。
   * 例如，为了让代码具备额外的防御性，
   * 示例7.7的代码只输出文件名，不输出目录名。实现方式是添加一个检查文件的isFile方法的过滤器。
   */

  for
    file <- filesHere
    if file.isFile
    if file.getName.endsWith(".xml")
  do println(file)

  def fileLines(file: java.io.File)=
    scala.io.Source.fromFile(file).getLines().toArray

  def grep(pattern: String) =
    for
      file<- (new File("C:\\Users\\yosuke\\Desktop\\代码1")).listFiles()
      if file.getName.endsWith(".scala")
      line <- fileLines(file)
      if line.trim.matches(pattern)
    do println(s"${file.getName}:${line.trim}")

  grep(".*gcd.*")

  def grep_ex(pattern:String) =
    for
      file<- (new File("C:\\Users\\yosuke\\Desktop\\代码1")).listFiles()
      if file.getName.endsWith(".scala")
      line<- fileLines(file)
      trimmed = line.trim
      if trimmed.matches(pattern)
    do println(s"ex---${file.getName}:${line.trim}")

  grep_ex(".*gcd.*")


  /**
   * 交出一个新的集合
   *
   * 虽然目前为止所有示例都是对遍历到的值进行操作然后忽略它，但是完全可以在每次迭代中生成一个可以被记住的值。
   * 具体做法就像我们在第3章的第12步介绍的，在for表达式的代码体之前加上关键字yield而不是do。\
   * 例如，如下函数识别出.scala文件并将它保存在数组中:
   *
   * 在本例中，结果是Array[File]，因为filesHere是一个数组，而交出的表达式类型为File。
   */

  def scalaFiles:Array[File] =
    for
      file <- (new File("C:\\Users\\yosuke\\Desktop\\代码1")).listFiles()
      if file.getName.endsWith(".scala")
    yield file

  println(scalaFiles.toBuffer)


  val forLineLengths =
    for
      file<- (new File("C:\\Users\\yosuke\\Desktop\\代码1")).listFiles()
      if file.getName.endsWith(".scala")
      line <- fileLines(file)
      trimmed = line.trim
      if trimmed.matches(".*for.*")
    yield trimmed.length

  println(forLineLengths.toBuffer)
  

}
