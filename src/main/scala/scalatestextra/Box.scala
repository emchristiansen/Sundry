package scalatestextra

////////////////////////////////////////////////

/**
 * A wrapper around a value with an implicit view to the underlying value.
 * 
 * Useful for checking invariants of wrapped values.
 */
trait Box[A] {
  val data: A
}

object Box {
  implicit def boxAToA[A](box: Box[A]): A = box.data
}