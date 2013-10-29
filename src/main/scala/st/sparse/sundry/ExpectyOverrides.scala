package st.sparse.sundry

import org.expecty.Expecty

/**
 * Provides better overloads for standard assertion statements, using Expecty. 
 * 
 * To use, just import the contents of this object.
 */
object ExpectyOverrides {
  val require = new Expecty()
  val assert = new Expecty()
}