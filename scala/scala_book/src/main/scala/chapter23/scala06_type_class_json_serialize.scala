package chapter23

object scala06_type_class_json_serialize extends App {

  /**
   * 23.6 类型族案例分析: JSON 序列化
   *
   * 在23.1节提到，序列化是一种能被广泛应用于其他不相关类型的行为，因此适用于类型族。
   * 作为本章最后一个示例，我们准备展示一个通过使用类型族来实现JSON序列化的例子。
   * 为了让例子尽量简单，我们只考虑序列化，不考虑反序列化，尽管这两种操作通常可以由同一个类库来处理。
   */

  /**
   * JSON是JavaScript客户端和后端服务器之间常用的数据交换格式。它定义了表示字符串、数字、布尔值、数组和对象的格式。
   *
   * @任何你想要序列化成JSON的内容都必须以上述5种数据类型之一来表达。
   * JSON的字符串看上去就像Scala的字符串字面量，如"tennis"
   * JSON中表示整数的数字看上去就像Scala的Int字面量，如10。
   * JSON的布尔值要么是true要么是false。
   * JSON对象是一个由花括号括起来的、以逗号分隔的 键/值对集合，其中的键是用字符串表示的名称。
   * JSON数组是一个由方括号括起来的、以逗号分隔的JSON数据类型的列表。
   * 最后，JSON还定义了一个null值。这里有一个包含除对象外的其他4种类型各一个成员的JSON对象，外加一个null值:
   */
  /**
   * {
   * "style":"tennis",
   * "size":"10",
   * "inStock": true,
   * "colors":["beige","while","blue"],
   * "humor":null
   * }
   */

  /**
   * 在这个案例分析中，我们将Scala的String序列化成JSON字符串，将Int和Long序列化成JSON数字，
   * 将Boolean序列化成JSON布尔值，将List序列化成JSON数组，将其他一些类型序列化成JSON对象。
   * 我们需要将Scala标准类库中的类型（如Int)序列化的事实凸显了想要通过混入某个特质到每一个想要序列化的类的难度。
   * 你可能可以定义一个名称为JsonSerializable的特质。该特质可以提供一个针对该对象的JSON文本的toJson方法。
   * 虽然你可以将JsonSerializable特质混入自己的类并实现相应的toJson方法，
   * 但是你不能对String、Int、Long、Boolean或List混入这个特质，因为你无法修改那些类型。
   */

  /**
   * 基于类型族的方案可以避免上述问题:你可以定义一个完全聚焦于如何将某个抽象的类型T的对象序列化成JSON的类型关系，
   * 并且不需要对那些需要序列化的类扩展某个公共超特质。
   * 你可以对每个需要序列化成JSON的类型定义一个类型族特质的上下文参数实例。
   * 示例23.13给出了这样一个名称为JsonSerializer的类型族特质。该特质接收一个类型参数T，提供一个接收类型参数T并将它转换成JSON字符串的serialize方法。
   */
  //示例23.13JsonSerializer类型族特质
  //  trait JsonSerializer[T]:
  //    def serialize(o:T):String

  /**
   * 为了让用户能对可序列化的类调用toJson方法，可以定义一个扩展方法。正如我们在22.5节探讨的，一个提供这样的扩展方法的不错的地方是类型族特质本身。
   * 通过这种方式，你就能确保只要作用域内存在JsonSerializer [T]，类型T就有可用的toJson方法。带有这个扩展方法的JsonSerializer特质如示例23.14所示。
   */
  //示例23.14 带有扩展方法的JsonSerializer类型族特质
  trait JsonSerializer[T]:
    def serialize(o: T): String

    extension (a: T)
      def toJson: String = serialize(a)


  /**
   * 合理的下一步是定义针对String、Int、Long和Boolean的类型族上下文参数实例。
   * 这些上下文参数实例最好被放在JsonSerializer伴生对象中，因为编译器在无法从当前作用域找到所需的上下文参数实例时会查找这个位置，
   * 正如我们在21.2节介绍的那样。这些上下文参数实例可以像示例23.15所示的那样被定义。
   */
  //示例23.15带有上下文参数的JsonSerializer伴生对象
  object JsonSerializer:
    given stringSerializer: JsonSerializer[String] with
      override def serialize(s: String): String = s"\"$s\""

    given intSerializer: JsonSerializer[Int] with
      override def serialize(n: Int): String = n.toString

    given longSerializer: JsonSerializer[Long] with
      override def serialize(n: Long): String = n.toString

    given booleanSerializer: JsonSerializer[Boolean] with
      override def serialize(b: Boolean): String = b.toString

    given listSerializer[T](using JsonSerializer[T]):JsonSerializer[List[T]] with
      override def serialize(ts: List[T]): String = s"[${ts.map(t=>t.toJson).mkString(", ")}]"

  /**
   * @引入拓展方法
   *
   * 引入扩展方法对任何具备可用JsonSerializer[T]的类型T添加toJson方法是很有用的。
   * 示例23.14中定义的扩展方法没有做到这一点，是因为它只对作用域内存在JsonSerializer[T]的类型T提供可用的toJson方法。
   * 如果作用域内没有JsonSerializer[T]，就不可行，即使类型T的伴生对象中存在JsonSerializer[T]也不行。
   * 你可以在单例对象中放置如示例23.16所示的扩展方法，让它易于引入。这个扩展方法包含了一个using子句，用于要求应用该扩展方法的类型T必须有一个可用的JsonSerializer[T]。
   */
  //示例23.16为了引入方便的扩展方法
  object ToJsonMethods:
    extension[T] (a: T)(using jser: JsonSerializer[T])
      def toJson: String = jser.serialize(a)

  /**
   * 在这里的ToJsonMethods对象准备就绪后，就可以在REPL中尝试这些序列化器。下面是使用它们的一些例子:
   */

  import ToJsonMethods.*

  println("tennis".toJson)
  println(10.toJson)
  println(true.toJson)

  /**
   * 对比示例23.16的ToJsonMethods对象中的扩展方法和示例23.14的JsonSerializer 特质中的扩展方法会给我们一些启发。
   * ToJsonMethods对象中的扩展方法接收一个JsonSerializer[T]作为using参数，但JsonSerializer特质中的扩展方法没有这样做，
   * 因为按照定义，这已经是JsonSerializer[T]的“成员”了。
   * 因此，ToJsonMethods对象的toJson方法对传入的名称为jser的JsonSerializer引用调用serialize方法，而JsonSerializer特质的toJson方法调用的是this的serialize方法。
   */

  /**
   * @序列化领域对象
   *
   * 接下来，假设你需要从领域模型中将某些特定的类序列化成JSON格式，
   * 包括示例23.17给出的用于表示地址簿的样例类。该地址簿包含一个联系人列表，且每个联系人都可以有零个或更多地址，以及零个或更多电话号码。
   * (更好的做法是对这些类的属性定义细微类型，如17.4节描述的那样。但为了让这个示例更简单，我们会直接使用String和Int类型。)
   */

  /**
   * 用于表示地址簿的JSON字符串由其嵌套对象的JSON字符串组成。
   * 因此，生成用于表示地址簿的JSON字符串要求其每一个嵌套对象都能被变换成JSON格式。
   * 例如，contacts字段中的每一个联系人(Contact)都必须被变换成表示相应联系人的JSON格式。
   * 联系人的每一个地址(Address）都必须被变换成表示相应地址的JSON格式。
   * 因此，若要序列化地址簿(AddressBook)，则我们需要将这个地址簿的所有构成对象都序列化成JSON格式。于是，为所有的领域对象定义JSON序列化器就是合理的。
   */
  //示例23.17用于表示地址簿的样例类
  case class Address(
                      street: String,
                      city: String,
                      state: String,
                      zip: Int
                    )

  case class Phone(
                    countryCode: Int,
                    phoneNumber: Long
                  )

  case class Contact(
                      name: String,
                      addresses: List[Address],
                      phone: List[Phone]
                    )

  case class AddressBook(contacts: List[Contact])

  /**
   * 为领域对象定义JsonSerializer上下文参数实例，一个不错的选择是将它定义在伴生对象中。
   * 示例23.18给出了你可能会为Address和Phone对象定义JSON序列化器的方式。
   * 在这些serialize方法中，我们引入并使用了来自示例23.16中的ToJsonMethods对象的toJson扩展方法，但将其重命名为asJson。
   * 这个重命名是必要的，这是为了避免与继承自示例23.14的JsonSerializer特质的同样名称的toJson扩展方法产生冲突。
   */
  //示例23.18 针对Address和Phone对象的JSON序列化器
  object Address:
    given addressSerializer:JsonSerializer[Address] with
      override def serialize(a: Address): String =
        import ToJsonMethods.{toJson as asJson}
        s"""|{
            |  "street": ${a.street.asJson},
            |  "city": ${a.city.asJson},
            |  "state": ${a.state.asJson},
            |  "zip": ${a.zip.asJson}
            |}""".stripMargin
  object Phone:
    given phoneSerializer:JsonSerializer[Phone] with
      override def serialize(a: Phone): String =
        import ToJsonMethods.{toJson as asJson}
        s"""|{
            |  "countryCode": ${a.countryCode.asJson},
            |  "phoneNumber": ${a.phoneNumber.asJson},
            |}""".stripMargin

  /**
   * @序列化列表
   *
   * 领域模型中的另外两个对象  Contact和AddressBook都包含列表。
   * 因此，为了序列化这些类型，拥有某种通用的将Scala的List序列化成JSON数组的方式就很有帮助。
   * 由于JSON数组是以方括号括起来的、以逗号分隔的JSON数据类型列表，因此对于任何类型T，只要存在JsonSerializer[T]，我们就能序列化List[T]。
   * 示例23.19给出了针对列表的JsonSerializer，只要存在对应列表元素类型的JsonSerializer，就会生成这个列表形式的JSON数组。
   */

  /**
   * 为了表示对列表元素类型的序列化器的依赖，这里的listSerializer上下文参数接收一个可以生成该元素类型的JSON序列化器作为using参数。
   * 举例来说，要将一个List[Address]序列化成JSON数组，就必须有一个可用的针对Address对象自身的上下文参数序列化器。
   * 如果找不到Address对象的序列化器，程序就无法编译。
   * 例如，由于JsonSerializer伴生对象中存在一个JsonSerializer[Int]上下文参数实例，因此将List[Int]序列化成JSON格式是可行的。这里有一个例子:
   */
  import ToJsonMethods.*
  println(List(1,2,3).toJson)

  /**
   * 另一方面，由于我们还没有定义JsonSerializer[Double]，因此尝试将List[Double]序列化成JSON格式会产生编译器错误:
   */
 // println(List(1.0,2.0,3.0).toJson)

  /**
   * 这个例子展示了使用类型族将Scala对象序列化成JSON格式的重大好处:
   *    类型族让编译器能够确保所有构成AddressBook对象的类都能被序列化成JSON格式。
   *    如果你未能提供比如Address对象的上下文参数实例，则你的程序将无法通过编译。
   *    而如果在Java中某个嵌套很深的对象没有实现Serializable，则你会在运行期得到异常。
   *    在这两种情况下，我们都有可能犯相同的错，但就Java序列化而言，我们得到的是运行期错误，
   *    而使用Scala类型族会让我们在编译期得到这个错误提示。
   *
   * @最后还需要注意的一点是，toJson方法可以在传入map的函数体中被调用（即t=>t.toJson的“toJson")的原因是作用域内存在一个JsonSerializer[T]的上下文参数实例。
   * 因此，本例中使用的扩展方法是声明在示例23.14的JsonSerializer特质自身中的那一个。
   */



  /**
   * @把这些都放在一起
   *
   *
   * 既然有了序列化列表的方式，就可以在Contact和AddressBook对象的序列化器中使用它。
   * 参考示例23.20。与之前一样，在引入时，我们需要将toJson重命名为asJson，以避免名称冲突。
   */
  //针对Contact和AddressBook对象的JSON序列化器
  object Contact:
    given contactSerializer:JsonSerializer[Contact] with
      override def serialize(c: Contact): String =
        import ToJsonMethods.{toJson as asJson}
        s"""|{
            |  "name":${c.name.asJson}
            |  "addresses":${c.addresses.asJson}
            |  "name":${c.name.asJson}
            |}""".stripMargin

  object AddressBook:
    given addressBookSerializer:JsonSerializer[AddressBook] with
      override def serialize(a: AddressBook): String =
        import ToJsonMethods.{toJson as asJson}
        s"""|{
            |  "contacts":${a.contacts.asJson}
            |}""".stripMargin

  /**
   * 现在我们已经完成了将地址簿序列化成JSON格式的所有准备工作。
   * 作为例子，考虑示例23.21中给出的AddressBook实例。只要从ToJsonMethods对象中引入了toJson扩展方法，就可以用如下代码序列化这个地址簿:
   */
  import ToJsonMethods.toJson
  //示例23.21 AddressBook实例
  val addressBook = AddressBook(
    List(
      Contact(
        "Bob Smith",
        List(
          Address(
            "12345 Main Street",
            "San Francisco",
            "CA",
            94105
          ),Address(
            "500 State Street",
            "Los Angeles",
            "CA",
            90007
          )
        ),
        List(
          Phone(
            1,
            5558881234
          ),
          Phone(
            49,
            5558413323
          )
        )
      )
    )
  )
  println(addressBook.toJson)

  /**
   * @当然，真实世界的JSON类库远比你在这里看到的复杂。可能你最想要做的一件事是利用Scala的元编程来通过类型族派生自动生成JsonSerializer类型族实例。我们将在《Scala高级编程》中涵盖这个主题。
   */


  /**
   * 23.7 结语
   *
   * 通过本章，你了解了类型族的概念并且看到了若干个示例。
   * @类型族是Scala中实现特定目的多态的一种基础方式。Scala针对类型族的语法糖、上下文界定，都体现了类型族作为Scala设计方案的重要性。
   * 你还看到了若干个类型族的实际应用:主方法、安全的相等性比较、隐式转换和JSON序列化。
   * 希望这些示例能够带给你适合使用类型族作为设计选择的那些用例场景的直观感受。在下一章，我们将更细致地介绍Scala的集合类库。
   */





}
