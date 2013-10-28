package st.sparse.sundry

import com.sksamuel.scrimage.Image
import java.io.File

/**
 * Adds some useful features to `Logging`.
 */
trait Logging extends com.typesafe.scalalogging.slf4j.Logging {
  def className = this.getClass.getName

  /**
   * Logs an image as a PNG.
   * 
   * `name` should not include the file extension, and "`name`.png" must be
   * a legal filename.
   */
  def logImage(
    image: Image,
    name: String)(implicit logRoot: LogRoot) {
    val directory = new File(logRoot.data, className)
    if (!directory.isDirectory) assert(directory.mkdirs())
    
    val outputFile = new File(directory, name + ".png")
    logger.info(s"Logging image to: ${outputFile.getPath}")
    image.write(outputFile)
  }
}