package scalatestextra

import java.io.File
import org.apache.commons.io.FileUtils

////////////////////////////////////////////////

/**
 * A boxed File, where the File must exist.
 */
case class ExistingDirectory(data: File) extends Box[File] {
  require(data.isDirectory, s"${data} is not an existing directory.")
}

object ExistingDirectory {
  /**
   * Creates an ExistingDirectory from a String.
   */
  def apply(name: String): ExistingDirectory = ExistingDirectory(new File(name))
}