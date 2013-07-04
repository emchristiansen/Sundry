package scalatestextra

import org.scalatest.Tag

////////////////////////////////////////////////////////////////////////////////

/**
 * Less than 0.1s.
 */
object InstantTest extends Tag("nebula.InstantTest")

/**
 * Less than 1s.
 */
object FastTest extends Tag("nebula.FastTest")

/**
 * Less than 10s.
 */
object MediumTest extends Tag("nebula.MediumTest")

/**
 * Less than 100s.
 */
object SlowTest extends Tag("nebula.SlowTest")

/**
 * Requires the user to inspect or do something (not fully automatic).
 */
object InteractiveTest extends Tag("nebula.InteractiveTest")

/**
 * External dataset required to run test.
 */
object DatasetTest extends Tag("nebula.DatasetTest")