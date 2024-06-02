package chapter20

object scala02_type_member extends App {

  /**
   * 20.2 类型成员
   *
   * 从上一节的例子中不难看出，
   * @Scala的抽象类型(abstract type)指的是用type关键字声明为某个类或特质的成员（但并不给出定义)的类型。
   * 类本身可以是抽象的，而特质本来从定义上讲就是抽象的，不过类和特质在Scala中都不是抽象类型。
   * @Scala的抽象类型永远都是某个类或特质的成员，如Abstract特质中的T。
   */

  /**
   * 你可以把非抽象（或者说“具体"）的类型成员，如Concrete类中的类型T，当作一种给某个类型定义新的名称，或者说别名(alias)的方式。
   * 以Concrete类为例，我们给类型String设置了一个别名T。这样一来，在Concrete类中任何地方出现T时，它的含义都是String。
   * 这包括了transform的参数和结果类型、initial和current等，它们都在超特质Abstract的声明中提到了T。因此，当Concrete类实现这些方法时，这些T都被解读为String。
   *
   * @使用类型成员的原因之一是给真名冗长或含义不明显的类型定义一个短小且描述性强的别名。这样的类型成员有助于澄清类或特质的代码。
   * @类型成员的另一个主要用途是声明子类必须"去定义"抽象类型。上一节展示的就是后一种用途，我们将在本章稍后部分做详细讲解。
   */

}