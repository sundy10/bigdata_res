package chapter04

object scala04_case_class extends App {

  /**
   * 4.3 样例类
   *
   * 在通常情况下，当你编写一个类的时候，需要实现诸如equals 、hashCode、toString、字段访问器(getter setter)、工厂方法等。
   * 这些可能非常耗时且容易出错。
   * Scala提供了“样例类”，可以基于传递给它的主构造方法的值来生成若干方法的实现。
   * 可以通过在class关键字之前加上case修饰符来声明样例类，
   */
  case class Person(name: String, age: Int) {
    def appendToName(suffix: String): Person = Person(s"$name$suffix", age)
  }

  object Person {
    //确保非空name值为(首字母)大写的
    def apply(name: String, age: Int): Person =
      val capitalizedName =
        if !name.isEmpty then
          val firstChar = name.charAt(0).toUpper
          val restOfName = name.substring(1)
          s"$firstChar$restOfName"
        else throw IllegalArgumentException("Empty name")
      new Person(capitalizedName, age)
  }

  //加上case修饰符以后，编译器将会生成若干有用的方法。首先,编译器将会创建一个伴生对象并放入名称为apply的工厂方法中。于是你就可以像这样构造一个新的Person对象:
  //编译器会把这一行代码重写为对生成的工厂方法apply的调用:Person.apply("Sally", 39)
  val p: Person = Person("sally", 39)

  println(p.toString) //Person(Sally,39)

  @main def m(string: String)=println("Hello, world")

  Predef.println()

  /**
   * Scala和Java的区别之—是，Java要求将公共的类放入与类同名的文件中（例如，需要将SpeedRacer类放到SpeedRacer.java文件中)，
   * 而Scala允许任意命名.scala文件，无论你将什么类或代码放到这个文件中。
   * 不过，通常对于那些非脚本的场景，把类放入以类名命名的文件中是推荐的做法，就像Java那样，以便程序员能够更容易地根据类名定位到对应的文件。
   */
}
