package chapter21

object scala05_context_parameter_import extends App {

  class PreferredPrompt(val preferredPrompt: String)

  class PreferredDrink(val preference: String)

  /**
   * 21.5 上下文参数的引入
   *
   * @在类的伴生对象中提供上下文参数值意味着这些上下文参数将对编译器的查找操作可见。
   * 这是对上下文参数合乎常理的默认行为的良好实践，因为使用者很可能总是需要这样的实现，比如，对某个类型提供自然的排序规则。
   * 除此之外，如果不希望上下文参数被自动找到，而是要求使用者在需要时通过引入(即import）主动查找，则把它放在（单独的）单例对象中也是一种良好实践。
   * 为了更易于辨别上下文参数来自哪里，Scala针对上下文参数提供了特殊的引入语法。
   *
   * 假设你像示例21.6一样定义了一个对象。
   */
  //用户偏好对象
  object TomsPrefs:
    val favoriteColor = "blue"

    def favoriteFood = "steak"

    given prompt: PreferredPrompt =
      PreferredPrompt("enjoy>")

    given drink: PreferredDrink =
      PreferredDrink("red wine")

    given prefPromptOrd: Ordering[PreferredPrompt] with
      override def compare(x: PreferredPrompt, y: PreferredPrompt): Int =
        x.preferredPrompt.compareTo(y.preferredPrompt)

    given prefDrinkOrd: Ordering[PreferredDrink] with
      override def compare(x: PreferredDrink, y: PreferredDrink): Int =
        x.preference.compareTo(y.preference)

  /**
   * 在第12章中，你曾看到如何用通配的引入语句来引入val和def。不过常规的通配引入语法并不会引入上下文参数:
   */
  import TomsPrefs.prompt   //引入 prompt

  /**
   * 如果你想引入所有上下文参数，则可以使用特殊的通配上下文参数引入(wildcard given import)语法:
   */
  //引入 prompt drink prefPromptOrd prefDrinkOrd
  import TomsPrefs.given

  /**
   * 由于上下文参数的名称通常并不会在你的代码中被显式地用到，而只是用到了它的类型，同时上下文参数的引入机制也允许你通过类型来引入
   * (由于21.3节描述的匿名上下文参数并没有名字，因此只能通过类型或通配上下文引入的方式引入它。)
   */
  //引入 drink 因为它是一个类型为PreferredDrink的上下文参数
  import TomsPrefs.{given PreferredDrink}

  /**
   * 如果你想以类型的方式同时引入prefPromptOrd和 prefDrinkOrd ,则可以显式地给出它们的类型，并在类型之前加上given:
   */
  //引入prefPromptOrd 和 prefDrinkOrd
  import TomsPrefs.{given Ordering[PreferredPrompt],given Ordering[PreferredDrink]}

  /**
   * 或者，你也可以用一个问号(?）作为类型参数来一起引入，就像这样:
   */
  //引入prefPromptOrd 和 prefDrinkOrd
  import TomsPrefs.{given Ordering[?]}



}
