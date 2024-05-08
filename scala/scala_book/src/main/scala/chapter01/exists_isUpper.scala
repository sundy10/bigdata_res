package chapter01

import scala.collection.mutable.ListBuffer


object exists_isUpper {
  def main(args: Array[String]): Unit = {
    val name = "Tom"
    val name1 = "tom"

    println(name.exists(_.isUpper))
    println(name1.exists(_.isUpper))

    Console println 10

    val listBuffer =new ListBuffer[String]
    listBuffer.append()
  }

  def max(x: Int, y: Int) =
    if x > y then x
    else y

}
