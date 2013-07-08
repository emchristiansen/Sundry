package scalatestextra

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
trait Dataset {
  def datasetRoot(implicit configMap: Map[String, Any]): File = { 
    val file = new File(configMap("datasetRoot").toString)
    assert(file.isDirectory)
    file
  }
}

/**
 * Expects the user to pass |logRoot|, for test which create logs, for example
 * interactive tests which log images and require the user to eyeball them.
 */
trait Logging {
  def logRoot(implicit configMap: Map[String, Any]): File = { 
    val file = new File(configMap("logRoot").toString)
    assert(file.isDirectory)
    file
  }
}