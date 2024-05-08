package chapter07

object scala01_if_expression extends App {

//  var filename= "default.txt"
//  if !args.isEmpty then
//    filename=args(0)

   val filename =
      if "arg".length>100 then "arg"
      else "default.txt"

    println(filename)

}
