package chapter06

object scala01_rational extends App {

  /**
   * 不可变对象的设计取舍
   *
   * 与可变对象相比，不可变对象具有若干优势和一个潜在的劣势。
   * 首先，不可变对象通常比可变对象更容易推理，因为不可变对象没有随着时间变化而变化的复杂的状态空间。
   * 其次，可以相当自由地传递不可变对象，而对于可变对象，在传递给其他代码之前，你可能需要对其进行保护式的复制。
   * 再次，假如有两个并发的线程同时访问某个不可变对象，则它们没有机会在对象被正确构造以后破坏其状态，因为没有线程可以改变某个不可变对象的状态。
   * 最后，不可变对象可以被安全地用作哈希表里的键。举例来说，如果某个可变对象在被添加到HashSet以后改变了，则当你下次再检索该HashSet的时候，可能就找不到这个对象了
   *
   * 不可变对象的主要劣势是它有时候需要复制一个大的对象图，而实际上也许一个局部的更新就能满足要求。
   * 在某些场景下，不可变对象可能用起来比较别扭，同时会带来性能瓶颈。
   * 因此，类库对于不可变的类也提供可变的版本这样的做法并不罕见。例如，StringBuilder
   */


  /**
   * Scala编泽器会将你在类定义体中给出的非字段或方法定义的代码编译进类的主构造方法中。举例来说，可以像这样来打印一条调试消息:
   */

  class Rational(n: Int, d: Int) {
    //println("Created " + n + "/" + d)
    //require方法接收一个Boolean类型的参数。如果传入的参数为true，则require方法将会正常返回;否则,require方法将会抛出lllegalArgumentException来阻止对象的构建。
    require(d != 0)

    val numer: Int = n
    val denom: Int = d

    override def toString: String = s"$n/$d"

    def add(that: Rational): Rational = Rational(
      numer * that.denom + that.numer * denom,
      denom * that.denom
    )

    def lessThan(that: Rational) =
      numer * that.denom < that.numer * denom

    def max(that:Rational) =
      if lessThan(that) then that else this
  }

  /**
   * 当你实例化那些接收参数的类（如Rational类)时，可以选择不new关键字。这样的代码编写方式被称作“通用应用方法”(universal apply method)。例如:
   */
  println(Rational(1, 2)) //Created 1/2

  val oneHalf = Rational(1, 2)
  val twoThirds = Rational(2, 3)
  println(oneHalf add twoThirds) //7/6


}
