package chapter20

object scala03_abstract_val extends App {

  /**
   * 20.3 抽象的val
   *
   * 抽象的val声明形式如下:
   *
   * val initial:String
   *
   * 该声明给出了val的名称和类型，但没有给出值。
   *
   * @这个值必须由子类中具体的val定义提供。例如，Concrete类用下面代码实现了这个val:
   * val initial = "hi"
   * @我们可以在不知道某个变量正确的值，但是明确地知道在当前类的每个实例中该变量都会有一个不可变更的值时，使用这样的抽象的val声明。
   * def initial:String
   */


  /**
   * 使用方代码可以用完全相同的方式（也就是obj.initial）来引用val和方法。
   *
   * @不过，如果initial是一个抽象的val，则使用方可以得到如下的保证:每次对obj.initia的引用都会交出相同的值。
   * @如果initial是一个抽象方法，则这个保证无法成立，因为这样一来，initial就可以被某个具体的每次都返回不同值的方法实现。
   */

  /**
   * @换句话说，抽象的val限制了它的合法实现:任何实现都必须是一个val定义，而不能是一个var或def定义。
   * @从另一方面讲，抽象方法声明可以用具体的方法定义或具体的val定义实现。假设有示例20.1的抽象类Fruit，则Apple是一个合法的子类实现，而BadApple则不是。
   */
  //重写抽象val和无参方法
  abstract class Fruit:
    val v: String //v表示值

    def m: String //m表示方法

  abstract class Apple extends Fruit :
    val v: String
    val m: String //用val 重写(覆盖)def 是可行的

  abstract class BadApple extends Fruit :
    //def v:String //错误: 不能用def重写(覆盖) val
    def m: String
}
