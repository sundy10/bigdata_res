package chapter14

object scala03_build_List extends App {

  /**
   * 14.3 构建列表
   *
   * 所有的列表都构建自两个基础的构建单元:Nil和::(读作"cons”)。
   * Nil表示空列表;中缀操作符::表示在列表前追加元素。
   * 也就是说，x ::xs表示这样一个列表:第一个元素为x，接下来是列表xs的全部元素。因此，前面的列表值也可以这样来定义:
   */

  val fruit = "apples"::("oranges"::("pears"::Nil))
  val nums = 1::(2::(3::(4::Nil)))

  /**
   * @事实上，之前我们使用List(...)"对fruit、nums、diag3和empty进行的定义,不过是最终展开成上面这些定义的包装方法而已。
   * 例如，List(1,2,3)创建的列表就是1::(2:(3 :Nil))。
   *
   * 由于::以冒号结尾，而::这个操作符是右结合的，例如，A::B::c会被翻译成A: (B:C)。因此，我们可以在前面的定义中去掉圆括号，例如:
   * 这与之前的nums定义是等效的。
   */
  val nums1 =1::2::3::4::Nil




}