package chapter07

object scala07_var_scope extends App {

  /**
   * 变量作用域
   *
   *
   * Scala程序的变量在声明时附带了一个用于规定在哪里能使用这个名称的作用域（(scope)。
   * 关于作用域，最常见的例子是代码缩进一般都会引入一个新的作用域，因此在某一层缩进中定义的任何元素都会在代码退回上一层缩进后离开作用域。
   * 我们可以看一下示例7.18中的函数。
   *
   */

  def printMultiTable() =
    var i=1
    //  只有 i 在作用域内
    while i<=10 do
      var j=1
      //i 和 j在作用域内
      while j<=10 do
        val prod =(i*j).toString
        //i, j, prod在作用域内
        var k=prod.length
        //i, j, prod 和 k在作用域内
        while k <4 do
          print(" ")
          k+=1
        print(prod)
        j+=1
        //i 和j仍在作用域内；prod和k超出了作用域
      println()
      i+=1
      // i 仍在作用域内； j, prod 和k超出了作用域

  printMultiTable()


  /**
   * 不过，可以在一个内嵌的作用域内定义一个与外部作用域内相同名称的变量。比如，下面的脚本可以正常编译和运行:
   *
   * 需要注意的一个 Scala与Java的区别是，Java不允许在内嵌的作用域内使用一个与外部作用域内相同名称的变量。
   * 在Scala程序中，内嵌作用域中的变量会“遮挡”(shadow)外部作用域内相同名称的变量，
   * 因为外部作用域内的同名变量在内嵌作用域内将不可见。
   */
  val a=1
  if a==1 then
    val a=2
    println(a)
  println(a)

  //在编译器中，可以随心地使用变量名。其他的先不谈，单这一点，就可以让你在不小心定义错了某个变量之后改变主意。之所以能这样做，是因为从概念上讲，编译器会对你录入的每一条语句创建一个新的嵌套作用域。

  // -> -> ->
}
