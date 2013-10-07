package st.sparse.sundry

import java.io.File
import org.apache.commons.io.FileUtils

////////////////////////////////////////////////

/**
 * A boxed File, where the File must exist.
 */
case class ExistingFile(data: File) extends Box[File] {
  require(data.isFile, s"${data} is not an existing file.")
}

object ExistingFile {
  /**
   * Creates an ExistingFile from a String.
   */
  def apply(name: String): ExistingFile = ExistingFile(new File(name))

  // TODO: Delete if possible.
//  def mkIfNeeded(name: String): ExistingFile = {
//    val file = new File(name)
//    if (!file.isFile) FileUtils.touch(file)
//    ExistingFile(file)
//  }
}