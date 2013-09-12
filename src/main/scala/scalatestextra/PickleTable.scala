package scalatestextra

import scala.pickling._
import scala.pickling.binary._

import scala.slick.driver.H2Driver.simple._
import Database.threadLocalSession

import scala.slick.lifted.MappedTypeMapper.base
import scala.slick.lifted.TypeMapper

import PickleToBytesMapper._

object PickleTable {        
  def apply[A: SPickler: Unpickler: FastTypeTag](name: String) = new Table[(A)](name) {
    def pickledData = column[A]("pickledData")
    def * = pickledData
  }
}