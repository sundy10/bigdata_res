package chapter03

object scala03_set_map extends App {

  /*
   * 由于Scala想让你同时享有函联数式和指令式编程风格的优势,其集合类库特意对可变和不可变的集合进行了区分。
   * 举例来说，数组永远是可变的;列表永远是不可变的。Scala还提供了集（set）和映射(map）的可变和不可变的不同选择，但使用同样的简称。对集和映射而言，Scala通过不同的类继承关系来区分可变和不可变版本。
   *  例如，Scala的API包含了一个基础的特质来表示集，
   * 这里的特质与Java的接口定义类似。(你将在第11章了解到更多关于特质的内容。)在此基础上，Scala提供了两个子特质(subtrait)，一个用于表示可变集,另一个用于表示不可变集。
   */

  // 默认 scala.collection.immutable.Set
  var jetSet = Set("Boeing", "Airbus")
  jetSet += "Lear"
  val query = jetSet.contains("Cessna") //false

  //要向集中添加新元素，可以对集调用+方法，传入这个新元素。无论是可变的还是不可变的集，+方法都会创建并返回一个新的包含新元素的集
  private val new_jetSet: Set[String] = jetSet + "newElement"

  //在上示例中，处理的是一个不可变的集。可变集提供了一个实际的+=方法，而不可变集并不直接提供这个方法。
  //本例的第14行，即“jetSet += "Linear"在本质上是如下代码的简写:
  jetSet = jetSet + "Lear" //因此，实际上是将jetSet这个var重新赋值成了—个包含"Boeing"、"Airbus'和"Linear"的新集。


  /*
  如果你想要的是一个可变集，则需要做—次引入(import)
   */

  //由于示例中的集是可变的，并不需要对movieSet重新赋值,这就是为什么它可以是val。
  // 与此相对应的是,示例3.5中对不可变集使用+=方法时需要对jetset重新赋值，因此它必须是var。

  import scala.collection.mutable

  val movieSet = mutable.Set("Spotlight", "Moonlight")
  movieSet += "Parasite"


  import scala.collection.mutable

  val treasureMap = mutable.Map.empty[Int, String]
  treasureMap += (1 -> "Go to island.")
  treasureMap += 1 -> "Go to island."
  treasureMap += 1.->("Go to island.") //可以在Scala的任何对象上调用这个>方法 它将返回包含键和值二个元素的元组 这些对象并没有声明这个方法，这个叫方法拓展 (extension method)
  private val tuple: (Int, String) = 1.->("Go to island")

  treasureMap += (2 -> "Find big X on ground.")
  treasureMap += (3 -> "Dig.")
  val step2 = treasureMap(2) //Find big X on ground.
  println(step2)

  //如果你更倾向于使用不可变的映射，则不需要任何引入，因为默认的映射就是不可变的
  val romanNumeral = Map(1 -> "I", 2 -> "II", 3 -> "III", 4 -> "IV", 5 -> "V")
  val four: String = romanNumeral(4) //"IV"




}
