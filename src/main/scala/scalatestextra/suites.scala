package scalatestextra

import org.scalatest
import org.scalatest.fixture
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.prop.GeneratorDrivenPropertyChecks

////////////////////////////////////////////////////////////////////////////////

trait FunGeneratorSuite extends scalatest.FunSuite with GeneratorDrivenPropertyChecks

trait FunGeneratorConfigSuite extends fixture.FunSuite with fixture.ConfigMapFixture with GeneratorDrivenPropertyChecks