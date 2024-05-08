package chapter12:
    object scala01_package extends App {

      /**
       * 第12章 包、引入和导出
       *
       *
       *  在处理程序，尤其是大型程序时，减少耦合（coupling）是很重要的。
       *  所谓的耦合，指的是程序不同部分依赖其他部分的程度。低耦合能减少程序某个局部的某个看似无害的改动对其他部分造成严重后果的风险。
       *  减少耦合的一种方式是以模块化的风格编写代码。你可以将程序切分成若干个较小的模块，每个模块都有所谓的内部和外部之分。
       *  当在模块内部（即实现部分)工作时，你只需要与同样在这个模块工作的程序员协同即可。只有当你必须修改模块的外部（即接口部分)时，才有必要与在其他模块工作的开发者协同。
       *
       *
       *  本章将向你展示若干能够帮助你以模块化风格编程的代码结构，包括如何将代码放进包里，如何通过引入让名称变得可见，以及如何通过访问修饰符控制定义的可见性等代码结构。
       *  这些代码结构在精神上与Java相似，不过有区别（通常更一致)，因此即使你已经知道Java，本章也值得一读。
       *
       */

      /**
       *  12.1 将代码放进包里
       *
       *  Scala代码存在于Java平台全局的包层次结构中。
       *  到目前为止,你看到的本书中的示例代码都位于未命名(unnamed）包。
       *
       *  在Scala中，可以通过两种方式将代码放进带名称的包里。
       *  第一种方式是在文件顶部放置一个package子句，将整个文件的内容放进指定的包里，如示例12.1所示。
       *
       *  package bobsrockets.navigation
       */
      class Navigator

      /**
       * 示例12.1中的 package子句将Navigator类放进了名称为bobsrockets. navigation的包里。根据名称推测，这是一个由Bob'sRockets, Inc.开发的导航软件。
       *
       * @注意
       * 由于Scala代码是Java生态的一部分，因此对于你打算发布出来的Scala包，建议你遵循Java将域名倒过来作为包名的习惯。
       * 例如，对Navigator而言，更好的包名也许是com.bobsrockets. navigation。不过在本章，我们将省去“com.”，让代码更好理解。
       *
       * 另一种将Scala代码放进包里的方式更像是C#的命名空间。可以在package子句之后加上冒号和一段缩进代码块，这个代码块包含了被放进该包里的定义。
       * 这个语法叫作“打包”(packaging)。示例12.2中的代码与示例12.1中的代码效果一样。
       *  package bobsrockets.navigation:
       *    class Navigator
       */


      /**
       * 对这样一个简单的例子而言，完全可以用示例12.1那样的写法。
       * 不过，这个更通用的表示法可以让我们在一个文件里包含多个包的内容。举例来说，可以把某个类的测试代码与原始代码放在同一个文件里，只需分成不同的包即可，如示例12.3所示。
       *
       * package bobsrockets:
       *    package navigation:
       *      //位于bobsrockets.navigation包中
       *      class Navigator
       *
       *      package launch:
       *          //位于bobsrockets.navigation. launch包中
       *          class Booster
       *
       */




    }

