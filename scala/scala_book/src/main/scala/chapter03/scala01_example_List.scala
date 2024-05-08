package chapter03

object scala01_example_List extends App {

  /*
  不需要写new List，因为在scala.List的伴生对象上定义了—个工厂方法，即“List.apply”
  数组元素是可变的，列表永远是不可变的
   */
  //val oneTwoThree =List(1,2,3)


  val oneTwo = List(1, 2)
  val threeFour = List(3, 4)
  // 返回新列表
  val oneTwoThreeFour = oneTwo ::: threeFour

  //也许在列表上用得最多的操作符是“::”，读作“cons”。它在一个已有列表的最前面添加—个新的元素，并返回这个新的列表。例如，如果执行下面这段代码:
  val twoThree = List(2, 3)
  val oneTwoThree = 1 :: twoThree //List(1, 2, 3)
  //val oneTwoThree =  twoThree.::(1)
  println(oneTwoThree)

  //空列表 Nil 或 List.empty
  Nil
  List.empty

  //之所以需要在末尾放—个Nil，是因为::是List类上定义的方法。如果只是写成1 :2 : 3，则编译是不会通过的，因为3的类型是Int，而lnt类并
  val one_TwoThree = 1 :: 2 :: 3 :: Nil //List(1, 2, 3)
  println(one_TwoThree)

  //List类的确提供了“追加”( append)操作，写作:+(在第24章有详细介绍)，但这个操作很少被使用，因为向列表（末尾)追加元素的操作所需要的时间随着列表的大小线性增加，而使用::在列表的前面添加元素只需要常量时间(constant time)。如果想通过追加元素的方式高效地构建列表，则可以依次在头部添加完成后，调用reverse方法。也可以用ListBuffer，这是一个可变的列表，它支持追

  private val thrill = List("1", "2", "3", "hello")

  thrill(2) //返回列表thrill中下标为2（从О开始计数)的元素（返回"until")

  thrill.count(s => s.length == 4) //对thrill中长度为4的字符串元素进行计数

  thrill.drop(2) //返回去掉了thrill的头两个元素的列表 List("3","hello")

  thrill.dropRight(2) //返回去掉了thrill的尾部两个元素的列表 List("1", "2")

  thrill.exists(s => s == "hello") //判断thrill中是否有字符串元素的值为"hello"(返回true )

  thrill.filter(s => s.length == 4) //按顺序返回列表thrill 中所有长度为4的元素列表 List("hello")

  thrill.forall(s => s.endsWith("o")) //表示列表thrill中是否所有元素都以字母"l"结尾 false

  thrill.foreach(s => print(s)) //对列表thrill中的每个字符串执行print操作

  thrill.head //返回列表thrill的首个元素 "1"

  thrill.init //返回列表thrill 除最后一个元素之外的其他元素组成的列表  List("1", "2", "3")

  thrill.isEmpty //表示列表thrill是否是空列表（返回false )

  thrill.last //返回列表thrill的最后一个元素（返回"hello")

  thrill.length //返回列表thrill元素个数 4

  thrill.map(s => s + "y") //返回一个对列表thrill所有字符串元素末尾添加"y"的新字符串的列表(返回List("1y"，"2y" , "3y" ,"helloy") )

  thrill.mkString(",") //返回用列表 thrill的所有元素组合成的字符串 "1,2,3,hello"

  thrill.filterNot(s => s.length == 4) //按顺序返回列表thrill中所有长度不为4的元素列表 List("1", "2", "3")

  thrill.reverse //返回包含列表thrill 的所有元素但顺序反转的列表 List("hello", "3", "2","1")

  thrill.sortWith((s, t) => s.charAt(0).toUpper > t.charAt(0).toUpper) //返回包含列表thrill的所有元素，按照首字母小写的字母反顺序排序的列表（返回List("fi1l" , "until"，
  

}
