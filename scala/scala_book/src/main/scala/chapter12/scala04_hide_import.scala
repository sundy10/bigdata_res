package chapter12

import collection.mutable

object scala04_hide_import extends App {

  /**
   * 12.4隐式引入
   *
   *  Scala对每个程序都隐式地添加了一些引入。
   *  @在本质上，这就好比在每个扩展名为“.scala”的源码文件的顶部都添加了如下3行引入子句:
   *
   *  import java.lang.*  //java.lang 包的全部内容
   *  import scala.*      //scala.lang 包的全部内容
   *  import Predef.*  //java.lang 包的全部内容
   */

  /**
   * java.lang包包含了标准的Java类。它总是被隐式地引入Scala源码文件中。由于java.lang包是被隐式引入的，举例来说，可以直接写Thread，而不是java.lang.Thread。
   * (Scala原本还有个.NET平台的实现,默认引入的命名空间为System，对应Java的java.lang。)
   *
   * 你无疑已经意识到，scala包 包含了Scala的标准类库，这里面有许多公用的类和对象。由于scala包是被隐式引入的，举例来说，可以直接写List，而不是scala.List。
   */

  List()
  mutable.HashMap

  /**
   * Predef对象包含了许多类型、方法和隐式转换的定义。这些定义在Scala程序中经常被用到。举例来说，由于Predef对象是被隐式引入的，可以直接写assert，而不是Predef.assert。
   *
   * @Scala对这3个引入子句做了一些特殊处理，后引入的会遮挡前面的。举例来说，scala包和java.lang包都定义了StringBuilder类。
   * @由于scala包的引入遮挡了java.lang包的引入，因此StringBuilder这个简单名称会被引用为scala.StringBuilder，而不是java.lang.StringBuilder。
   */
  StringBuilder

}
