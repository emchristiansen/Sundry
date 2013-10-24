package st.sparse.sundry

import scala.pickling.FastTypeTag
import scala.reflect.runtime._
import scala.tools.reflect.ToolBox

trait Reflect {
  /**
   * A fully qualified type name.
   */
  def typeName[A: FastTypeTag]: String =
    implicitly[FastTypeTag[A]].tpe.toString

  /**
   * Alias of `typeName`.
   */
  def tn[A: FastTypeTag] = typeName[A]

  //  /**
  //   * The last part of a type name.
  //   */
  //  def shortTypeName[A: FastTypeTag]: String = {
  //    typeName[A]
  ////    println(typeName[A])
  ////    typeName[A].split(".").toSeq.last
  //  }

  //  def shortClassName[A < AnyRef]: String = classOf[A].getName.split(".").toSeq.last

  /**
   * A fully qualified type name, but without type parameters.
   */
  def constructorName[A: FastTypeTag]: String =
    typeName[A].takeWhile(_ != '[')

  /**
   * Alias of `constructorName`.
   */
  def cn[A: FastTypeTag] = constructorName

  /**
   * A fully qualified singleton object name.
   */
  def objectName[A: FastTypeTag]: String =
    typeName[A].replace(".type", "")

  /**
   * Alias of `objectName`.
   */
  def on[A: FastTypeTag]: String = objectName[A]

  private object ReflectionLock

  val cm = universe.runtimeMirror(getClass.getClassLoader)
  val tb = cm.mkToolBox()

  def eval[A](code: String): A = ReflectionLock.synchronized {
    tb.eval(tb.parse(code)).asInstanceOf[A]
  }
}