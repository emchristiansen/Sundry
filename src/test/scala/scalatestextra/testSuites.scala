package scalatestextra

import org.scalatest.fixture
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.scalacheck.Gen

////////////////////////////////////////////////////////////////////////////////

class TestFunGeneratorSuite extends FunGeneratorSuite {
  test("a vanilla unit test", InstantTest) {
    val x = 1
    assert(x == 1)
  }

  test("a generator driven test", InstantTest) {
    val evenInts = for (n <- Gen.choose(-1000, 1000)) yield 2 * n
    forAll(evenInts) { x =>
      assert(x % 2 == 0)
    }
  }
}

class TestFunGeneratorConfigSuite extends FunGeneratorConfigSuite {
  test("make sure the ConfigMap exists for a vanilla unit test") { configMap =>
    assert(configMap.toString.contains("Map"))
  }

  test("make sure the ConfigMap exists for a generator drivent test") {
    configMap =>
      val evenInts = for (n <- Gen.choose(-1000, 1000)) yield 2 * n
      forAll(evenInts) { x =>
        assert(x % 2 == 0)
        assert(configMap.toString.contains("Map"))
      }
  }
}