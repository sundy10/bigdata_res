package chapter08

import javax.swing.JButton

object scala09_SAM extends App {

  /**
   * 8.9 SAM类型
   *
   *
   * 在Java中， lambda表达式可以被应用到任何需要只包含单个抽象方法（single abstract method，即SAM)的类或接口实例的地方。
   * Java的ActionListener就是这样的一个接口，因为它只包含单个抽象方法，即actionPerformed。
   * 因此,lambda表达式可以被用在这里来给Swing的按钮注册动作监视器。例如:
   *
   * JButton button = new JButton();
   * button.addActionListener(
   * event -> System.out.println( "pressed ! ")
   * );
   *
   */

  /**
   * 在Scala中，也可以在同样的场景下使用匿名内部类的实例,但你可能更希望使用函数字面量，就像这样:
   */
  val button = new JButton()
  button.addActionListener(
    _ => println("pressed!")
  )

  /**
   * Scala让我们可以在这样的场合使用函数字面量，
   * 因为与Java类似，
   *
   * @Scala允许在任何需要声明单个抽象方法的类或特质的实例的地方使用函数类型的值。
   * 这对任何单个抽象方法都有效。例如，可以定义一个名称为Increaser的特质，包含单个抽象方法increase:
   */

  trait Increaser {
    def increase(i: Int): Int
  }

  /**
   * 然后可以定义一个接收Increaser特质的方法:
   */
  def increaseOne(increaser: Increaser): Int =
    increaser.increase(1)

  //要调用这个新方法，可以传入一个匿名的Increaser实例，就像这样:
  increaseOne(new Increaser {
    override def increase(i: Int): Int = i + 7
  })

  /**
   * 在Scala 2.12或更新版本的Scala中，也可以直接使用函数字面量,因为Increaser特质是SAM类型的:
   */
  increaseOne(i => i + 7)
  println(increaseOne(_ + 7))
}
