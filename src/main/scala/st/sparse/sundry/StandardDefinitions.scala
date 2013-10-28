package st.sparse.sundry

//////////////////////////////////////////

trait StandardDefinitions {
  /**
   * "5 times println("hello")" prints "hello" 5 times.
   */
  implicit class IntTimes(int: Int) {
    def times[A](function: => A): IndexedSeq[A] =
      (0 until int).map(_ => function)
  }

  /**
   * Adds |compose| for functions of arity 2.
   */
  implicit class AddCompose[-T1, +R](function1: (T1) => R) {
    def compose[A, B](function2: (A, B) => T1): (A, B) => R =
      (a, b) => function1(function2(a, b))
  }
  
  val userHome = ExistingDirectory(System.getProperty("user.home"))
}