package chapter20

object scala07_path_dependence_type extends App {

  /**
   * 20.7 路径依赖类型
   *
   * 再看看最后的这段错误消息。注意，eat方法要求的类型为bessy. SuitableFood。
   * @这个类型包含了对象引用bessy和这个对象的类型字段SuitableFood。bessy.SuitableFood的含义是“作为bessy这个对象的成员的SuitableFood类型”，或者说，适用于bessy的食物类型。
   *
   * @像bessy.SuitableFood这样的类型被称为路径依赖类型 （path-dependent type)。
   * @这里的“路径”指的是对对象的引用。它可以是一个简单名称，如bessy，也可以是更长的访问路径，如farm.barn.bessy，其中farm、barn和bessy都是指向对象的变量（或单例对象名称)。
   *
   * @正如“路径依赖类型”这个表述所隐含的，对于这样的类型依赖路径，一般不同的路径会催生出不同的类型。例如，可以像这样定义DogFood和Dog类:
   */
  import scala06_abstract_type.*

  class DogFood extends Food
  class Dog extends Animal:
    type SuitableFood = DogFood
    override def eat(food: DogFood): Unit = {}

  /**
   * 当我们尝试用适合牛的食物来喂狗的时候，代码将不能通过编译:
   */
  val bessy = new Cow
  val lassie = new Dog
  //lassie.eat(new bessy.SuitableFood)

  /**
   * 这里的问题在于传入eat方法的SuitableFood对象——bessy.SuitableFood的类型与eat方法的参数类型lassie.SuitableFood不兼容。
   *
   * 对两个Dog 实例而言情况就不同了。因为Dog 实例的SuitableFood类型被定义为DogFood类的别名，
   * 因此两个Dog实例的SuitableFood类型事实上是相同的。
   * 这样一来，名称为lassie的Dog实例实际上可以吃(eat)另一个不同Dog实例（我们叫它bootsie)的食物:
   */
  val bootsie = new Dog
  lassie.eat(new bootsie.SuitableFood)

  /**
   * 路径依赖类型的语法与Java的内部类类型相似，
   * @不过有一个重要的区别:路径依赖类型用的是外部“对象”的名称，而内部类用的是外部“类”的名称。
   * @Scala同样可以表达Java风格的内部类，不过写法是不同的。参考如下的两个类——Outer和Inner:
   */
  class Outer:
    class Inner



  /**
   * 在Scala中，内部类的寻址是通过Outer#Inner这样的表达式而不是Java的Outer.Inner实现的。
   * ”.”这个语法只为对象保留。例如，假设我们有两个类型为Outer的对象:
   */
  val o1 = new Outer
  val o2 = new Outer

  /**
   * @这里的o1.Inner和o2.Inner是两个路径依赖的类型（它们是不同的类型)。
   * @这两个类型都符合更一般的类型Outer#Inner(是它的子类型)，
   * @这个一般类型的含义是任意类型为Outer的外部对象。对比而言，类型o1.Inner指的是特定外部对象（即o1引用的那个对象)的Inner类。
   * @同理，类型o2.Inner指的是另一个特定外部对象（即o2引用的那个对象）的Inner类。
   */

  /**
   * 像Java—样，Scala的内部类实例会保存一个到外部类实例的引用。这允许内部类访问其外部类的成员。
   * 因此，我们在实例化内部类的时候必须以某种方式给出外部类实例。一种方式是在外部类的定义体中实例化内部类。
   * 在这种情况下，会使用当前这个外部类实例（用this引用的那一个）。
   *
   * 另一种方式是使用路径依赖类型。例如，o1.Inner这个类型是一个特定外部对象，我们可以将其实例化:
   */
  new o1.Inner

  /**
   * 得到的内部对象将会包含一个指向其外部对象的引用，即由o1引用的对象。
   *   与之相对应，由于Outer#nner类型并没有指明Outer的特定实例，因此我们并不能创建它的实例:
   */




}
