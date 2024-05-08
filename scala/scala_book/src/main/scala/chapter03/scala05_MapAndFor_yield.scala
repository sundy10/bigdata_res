package chapter03

object scala05_MapAndFor_yield extends App {
  /**
   * 在指令式编程风格中，可以当场改变数据结构直到达成算法的目标。而在函数式编程风格中，需要把不可变的数据结构变换成新的数据结洁构来达成目标。
   * 不可变集合的一个重要的用于实现函数式变换的方法是map。与foreach方法相同,map方法也接收一个函数作为参数。
   * 与foreach方法不同的是，foreach方法用传入的函数对每个元素执行副作用，而map方法用传入的函数将每个元素变换成新的值。 返回相应的类型
   */

  val adjectives = List("One", "Two", "Red", "Blue")

  //可以像这样将它变换或由新的字符串组成的新列表:
  val nouns: List[String] = adjectives.map(adj => adj + " Fish") //List("One Fish", "Two Fish", "Red Fish", "Blue Fish")

  //另—种执行这个变换的方式是使用for表达式。可以通过yield关键字（而不是do）来引入需要执行的代码体:
  //for-yield生成的结果与map方法生成的结果完全一样，因为编译器会把for-yield表达式变换成map方法调用。
  val yield_nouns =
  for adj <- adjectives yield
    adj + "Fish" //List("One Fish", "Two Fish", "Red Fish", "Blue Fish")

  /**
   * 请注意，当你对List执行map操作时，得到的返回值是—一个新的list; 而当你对Vector执行map操作时，得到的返回值是一个新的vector。你会发现绝大多数定义了map方法的类型都具备这个模式。
   * 请注意，当你对列表执行映射操作时，得到的返回值是-一个新的列表而当你对向量执行映射操作时，得到的返回值是一个新的向量。你会发现绝大多数定义了地图方法的类型都具备这个模式.
   *
   * 最后再看一个例子,Scala的Option类型。Scala用Option表示可选的值，而不使用像Java—样用null表达此含义的传统技法。
   * Option要么是一个Some，表示值存在;要么是一个None，表示没有值。
   *
   * 作为一个展示Option实际使用的案例，我们可以考查—下find方法。
   * 所有的Scala集合类型，包括List和Vector，都具备find方法，其作用是查找满足给定前提的元素，这个前提是一个接收元素类型的参数并返回布尔值的函赛。
   * find方法的结果类型是Option[E]，其中，E是集合的元素类型。find方法会逐个遍历集合的元素，将元素传递给前提。
   * 如果前提返回了true，find就停止遍历，并将当前元素包装在Some中返回。如果find遍历了所有元素都没有找到能通过前提判断的元素，就会返回None。下面是一些结果类型均为Option[String]的示例
   */
  val ques = Vector("Who", "What", "When", "Where", "Why")
  val startsW: Option[String] = ques.find(q => q.startsWith("W"))  //Some(Who)
  val hasLen4: Option[String] = ques.find(q => q.length==4)        //Some(What)
  val hasLen5: Option[String] = ques.find(q => q.length==5)        //Some(Where)
  val startsH: Option[String] = ques.find(q => q.startsWith("H"))  //None

  //尽管Option不是一个集合，它也提供了map方法。@如果Option是一个Some，可被称为“已定义”的可选值，则map方法将返回—个新的包含了将原始Some元素传入map方法后得到返回值的新Option。下面的示例对startsW进行了变换,而它本来是一个包含字符串"Who"的Some:
  startsW.map(word=>word.toUpperCase()) //Some(WHO)
  for word <- startsW yield word.toUpperCase()  //Some(WHO)

  //如果对None执行map，None意味着这是一个“未定义”的可选值，将得到一个None。
  startsH.map(word=>word.toUpperCase()) //None
  for word <- startsH yield word.toUpperCase()  //None

  /**
   * 还可以用map方法和for-yield对其他许多类型进行变换，但就目前而言足够了。
   *      这一步的主要目的是让你如何编写典型的Scala代码有一个直观的认识: 对 不可变数据结构 进行 函数式变换。
   */
}
