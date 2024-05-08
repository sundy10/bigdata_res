package chapter20

object scala06_abstract_type extends App {

  /**
   * 20.6 抽象类型
   *
   * 在本章的最开始，你看到了“type T”这个抽象类型的声明。本章剩余的部分将讨论这样的抽象类型声明的含义及它的用途。
   * @与所有其他抽象声明一样，抽象类型声明是某种将会在子类中具体定义的内容的'占位符'。
   *
   * 在本例中，这是一个将会在类继承关系下游中被定义的类型。因此上面的T指的是一个在声明时还未知的类型。不同的子类可以提供不同的T的实现。
   */

  /**
   * 参考这样一个例子，其中抽象类是很自然地出现的。假设你被指派了一个对动物饮食习惯建模的任务。你可能会从一个Food类和一个带有eat方法的Animal类开始:
   * 接下来你可能会试着将这两个类具体化，做出一个吃Grass (草）的Cow(牛）类:
   */
//  class Food
//  abstract class Animal:
//    def eat(food: Food):Unit
//
//  class Grass extends Food
//  class Cow extends Animal:
//    def eat(food: Grass): Unit = {} //这段代码不能编译

  //不过，如果你去编译这两个新类，就会得到 23.1.png 的编译错误:

  /**
   * 发生了什么?Cow类的eat方法并没有重写Animal类的eat方法，因为它们的参数类型不同:Cow类的参数类型是Grass，而Animal类的参数类型是Food。
   *
   * 有人会认为，类型系统在拒绝这些类这一点上有些不必要的严格了。
   * 他们认为，在子类中对方法参数做特殊化处理是可行的。然而，如果我们真的允许这样的写法，则很快就会处于不安全的境地。
   * 举例来说，如下脚本可能就会通过类型检查:
   */
//  class Food1
//  abstract class Animal1:
//    def eat(food: Food):Unit
//
//  class Grass1 extends Food1
//  class Cow1 extends Animal1:
//    override def eat(food: Grass1): Unit = {}
//



  /**
   * 如果取消前面的限制，则这段程序能够正常编译，因为牛是动物，而Animal类的确有一个可以接收任何食物（包括鱼）的eat方法。不过显然让牛吃鱼是不合理的。
   *
   * 你需要做的是采用某种更精确的建模方式。动物的确吃食物，但每种动物吃哪种食物取决于其本身。这个意思可以很清晰地通过抽象类型表达，如示例20.10所示。
   */
  class Food

  abstract class Animal:
    type SuitableFood <: Food
    def eat(food: SuitableFood):Unit



  /**
   * 有了这个新的类定义，某种动物只能吃那些适合它吃的食物。至于什么食物是合适的，并不能在Animal类这个层次确定。
   * 这就是SuitableFood被建模成一个抽象类型的原因。这个类型有一个上界，即Food，以“ <:Food”子句表示。
   * 这意味着Animal子类中任何对SuitableFood的具体实例化都必须是Food类的子类。举例来说，你并不能用IOException类来实例化SuitableFood.
   *
   * 有了Animal类的定义，就可以继续定义Cow类了，如示例20.11所示。Cow类将其SuitableFood固定在Grass上，并且定义了一个具体的eat方法来处理这一类食物。
   * 这些新的类定义能够被正确编译。如果你试着对新的类定义运行“牛吃鱼”的例子，将得到如下的编译错误:
   */
  class Grass extends Food
  class Cow extends Animal:
    type SuitableFood =Grass
    override def eat(food: Grass): Unit = {}

  val bessy:Animal =new Cow
  class Fish extends Food
  //bessy.eat(new Fish())



}
