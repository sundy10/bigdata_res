package chapter20

object scala04_abstract_var extends App {

  /**
   * 20.4 抽象的var
   *
   * 与抽象的val类似，抽象的var也只声明了名称和类型，并不给出初始值。例如，示例20.2的AbstractTime特质声明了两个抽象的var ,即hour和minute:
   */
  trait AbstractTime:
    var hour:Int
    var minute:Int

  /**
   * 像hour和minute这样的抽象的var的含义是什么?我们在16.2节看到过，
   * 被声明为类成员的var默认都带上了getter和setter方法。这一点对抽象的var而言同样成立。
   * 举例来说，如果我们声明了名称为hour的抽象的var，则实际上也隐式地定义了一个抽象的getter方法 hour和一个抽象的setter方法hour_=。
   * 这里并不需要定义一个可被重新赋值的字段，这个字段会自然地出现在定义这个抽象的var的具体实现的子类中。举例来说，示例20.2中的AbstractTime特质与示例20.3中的是完全等效的。
   */
  //抽象的var是如何被展开成getter和setter方法的
  trait AbstractTime1:
    def hour :Int               //hour 的 getter方法
    def hour_=(x:Int):Unit      //hour 的 setter方法
    def minute:Int              //minute的getter方法
    def minute_=(x:Int):Unit     //minute的setter方法
  

}
