package st.sparse.sundry

import org.scalatest.Tag

////////////////////////////////////////////////////////////////////////////////
// Optional tags which can be used to select which tests to run.
// For example, a user may decide to run only the fast tests, or may decide
// not to run tests which require interaction.
////////////////////////////////////////////////////////////////////////////////

/**
 * Tests which run for less than 0.1s.
 */
object InstantTest extends Tag("InstantTest")

/**
 * Tests which run for less than 1s.
 */
object FastTest extends Tag("FastTest")

/**
 * Tests which run for less than 10s.
 */
object MediumTest extends Tag("MediumTest")

/**
 * Tests which run for less than 100s.
 */
object SlowTest extends Tag("SlowTest")

/**
 * Test which require the user to inspect or do something (not fully automatic).
 */
object InteractiveTest extends Tag("InteractiveTest")

/**
 * Tests which require an external dataset.
 */
object DatasetTest extends Tag("DatasetTest")