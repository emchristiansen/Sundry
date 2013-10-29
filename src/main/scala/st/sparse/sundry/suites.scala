package st.sparse.sundry

import org.scalatest
import org.scalatest.fixture
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import java.io.File

////////////////////////////////////////////////////////////////////////////////

/**
 * A suite supporting vanilla unit tests as well as generator driven tests.
 */
trait FunGeneratorSuite extends scalatest.FunSuite with GeneratorDrivenPropertyChecks

/**
 * Essentially a FunGeneratorSuite, but passes a ConfigMap to each test.
 */
trait FunGeneratorConfigSuite extends fixture.FunSuite with fixture.ConfigMapFixture with GeneratorDrivenPropertyChecks

/**
 * Expects the user to pass |datasetRoot|, for tests which require
 * access to additional data.
 */
trait DataTest {
  def dataRoot(implicit configMap: Map[String, Any]): ExistingDirectory =
    ExistingDirectory(new File(configMap("dataRoot").toString))
}

/**
 * Expects the user to pass |logRoot|, for test which create logs, for example
 * interactive tests which log images and require the user to eyeball them.
 */
trait LoggingTest {
  def logRoot(implicit configMap: Map[String, Any]): File =
    ExistingDirectory(new File(configMap("logRoot").toString))
}