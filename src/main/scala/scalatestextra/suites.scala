package scalatestextra

import org.scalatest.fixture
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.prop.GeneratorDrivenPropertyChecks

////////////////////////////////////////////////////////////////////////////////

trait FunGeneratorSuite extends fixture.FunSuite with GeneratorDrivenPropertyChecks

trait FunGeneratorConfigSuite extends FunGeneratorSuite with fixture.ConfigMapFixture