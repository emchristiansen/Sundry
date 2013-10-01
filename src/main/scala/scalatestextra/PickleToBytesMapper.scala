//package scalatestextra
//
//import scala.pickling._
//import scala.pickling.binary._
//
//import scala.slick.driver.H2Driver.simple._
//import Database.threadLocalSession
//
//import scala.slick.lifted.MappedTypeMapper.base
//import scala.slick.lifted.TypeMapper
//
/////////////////////////////////////////////
//
//object PickleToBytesMapper {
//  // Amusingly, adding an explicit type here (`TypeMapper[A]`) changes the
//  // semantics of the program, because it makes this implicit def available
//  // from inside the definition itself.
//  // Unfortunately, this causes the function to call itself forever and
//  // stack overflow.
//  implicit def pickleToBytesMapper[A: SPickler: Unpickler: FastTypeTag] =
//    base[A, Array[Byte]](
//      (scalaObject: A) => scalaObject.pickle.value,
//      (bytes: Array[Byte]) => BinaryPickle(bytes).unpickle[A])
//}