package chapter20

import chapter20.scala06_abstract_type.{Food, Grass}


object scala08_Improved_type extends App {

  abstract class Animal:
    type SuitableFood <: Food
    def eat(food: SuitableFood):Unit

  /**
   * 20.8 改良类型
   *
   * 当一个类从另一个类继承时，我们将前者称为后者的名义( nominal）子类型。
   * 之所以是名义子类型，是因为每个类型都有一个名称,而这些名称被显式地声明为存在子类型关系。
   * @除此之外，Scala还额外支持结构(structural)子类型，即只要两个类型有兼容的成员，我们就可以说它们之间存在子类型关系。Scala实现结构子类型的方式是改良类型(refinement type)。
   */

  /**
   * 名义子类型通常更方便，因此，我们应该在任何新的设计中优先尝试名义子类型。
   * 名称是单个简短的标识符，因此比显式地列出成员类型更精简。
   * 不仅如此，结构子类型通常在灵活度方面超出了我们想要的程度。一个控件可以使用draw()，一个西部牛仔也可以使用draw()，不过这两者并不互为替代。
   * 当你(不小心）用牛仔替换了控件时，通常应该更希望得到一个编译错误。
   */

  /**
   * 尽管如此，结构子类型也有其自身的优势。有时候某个类型除其成员之外并没有更多的信息了。
   * 例如，假设我们想定义一个可以包含食草动物的Pasture类，
   * 一种选择是定义一个AnimalThatEatsGrass特质并在适用的类上混入。
   * 不过这样的代码很啰唆。Cow类已经声明了牛是动物，并且食草，现在还需要声明牛是一个“食草的动物”。
   *
   * 除了定义AnimalThatEatsGrass特质，我们还可以使用改良类型。只需要写下基类型Animal，然后加上一系列用花括号括起来的成员即可。
   * 花括号中的成员进一步指定（或者也可以说是改良）了基类中的成员类型。
   */




}
