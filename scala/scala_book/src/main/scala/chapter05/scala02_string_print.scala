package chapter05

object scala02_string_print extends App {

  val name = "reader"
  println(s"Hello, $name")

  println(s"The answer is ${6 * 7}") //42


  /**
   * Scala默认还提供了两种字符串插值器:raw和f。
   * raw字符串插值器的行为与s字符串插值器的类似，不过它并不识别字符转义序列(比如，表5.2给出的那些)。
   * 举例来说，如下语句将打印出4个反斜杠，而不是2个反斜杠:
   */

  println(raw"No\\\\escape!") //No\\\\escape!

  println(f"${math.Pi}%.5f") //3.14159


  /**
   * 在Scala中，字符串插值是通过在编译期重写代码来实现的。
   * 编译器会将任何由某个标识符紧接着字符串字面量的(左)双引号这样的表达式当作字符串插值器表达式处理。
   */


}
