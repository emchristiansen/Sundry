package scalatestextra

import org.scalatest.fixture
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.scalacheck.Gen
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

////////////////////////////////////////////////////////////////////////////////

@RunWith(classOf[JUnitRunner])
class TestNear extends FunGeneratorSuite {
  test("standard types", InstantTest) {
    assertNear(0, 0)
    assertNear(1.1, 1.10000000001)
    intercept[AssertionError] {
      assertNear(1.1, 1.2)
    }
  }
}