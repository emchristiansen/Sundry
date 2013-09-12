package scalatestextra

import scala.pickling._
import scala.pickling.binary._

import scala.slick.driver.H2Driver.simple._
import Database.threadLocalSession

//import scala.slick.lifted.MappedTypeMapper.base
//import scala.slick.lifted.TypeMapper

import Database.threadLocalSession
import PickleToBytesMapper._
import scala.slick.jdbc.meta.MTable

///////////////////////////////////////////

/**
 * A mutable map which is backed by a database.
 *
 * `name` is the name of this map, and is used to connect to
 * correct tables in the database.
 * _These tables must already exist_; see `PersistentMap.create`
 * to create a new map.
 * `database` holds the table in which this map is stored.
 */
case class PersistentMap[A: SPickler: Unpickler: FastTypeTag, B: SPickler: Unpickler: FastTypeTag](
  name: String,
  database: Database) extends collection.mutable.Map[A, B] {
  // This table stores a single record, recording the type information
  // of this map.
  // It is used to verify the table the map connects to actually stores
  // the required types.
  // TODO: Store type tags, not strings.
  // TODO: Make these tables objects, not values, and disable reflective calls.
  val typeTable = new Table[(String, String)](name + ":typeTable") {
    def keyType = column[String]("keyType")

    def valueType = column[String]("valueType")

    def * = keyType ~ valueType
  }

  // This table stores the data in the map.
  // The first entry, of type `Long`, stores hash codes and is used
  // for efficient lookup.
  val recordsTable = new Table[(Long, A, B)](name + ":recordsTable") {
    def keyHashCode = column[Long]("keyHashCode", O.PrimaryKey)

    def key = column[A]("key")

    def value = column[B]("value")

    def * = keyHashCode ~ key ~ value
  }

  // Do consistency checks on the two backing tables.
  database withSession {
    // The type table:
    // 1) must exist,
    require(MTable.getTables(typeTable.tableName).list().size == 1)
    val entries = Query(typeTable).list
    // 2) must have exactly one entry,
    require(entries.size == 1)
    // 3) and that entry must reflect the required type.
    require(entries.head._1 == implicitly[FastTypeTag[A]].toString)
    require(entries.head._2 == implicitly[FastTypeTag[B]].toString)

    // The records table must exist.
    require(MTable.getTables(recordsTable.tableName).list() == 1)
  }

  override def get(key: A): Option[B] = {
    database withSession {
      // We look up the key-value pair using the key's hash code.
      val list = Query(recordsTable).filter(_.keyHashCode === key.hashCode.toLong).list
      // `keyHashCode` is a primary key.
      assert(list.size == 0 || list.size == 1)
      list.headOption map { tuple =>
        assert(tuple._2 == key)
        tuple._3
      }
    }
  }

  override def iterator: Iterator[(A, B)] = {
    database withSession {
      // We simply return all the database entries, dropping the hash codes.
      Query(recordsTable).elements map { tuple => (tuple._2, tuple._3) }
    }
  }

  override def +=(kv: (A, B)): this.type = {
    database withSession {
      val (key, value) = kv
      // If this key is in the database already, we delete its record.
      this -= key

      // Then we insert our new record.
      recordsTable.insert((key.hashCode.toLong, key, value))
    }

    this
  }

  override def -=(key: A): this.type = {
    database withSession {
      val query =
        Query(recordsTable).filter(_.keyHashCode === key.hashCode.toLong)

      // `keyHashCode` is a primary key.
      assert(query.list.size <= 1)

      query.delete
    }

    this
  }
}

object PersistentMap {
  /**
   * Creates a new map.
   *
   * If another map of the same name already exists, this will clobber it.
   */
  def create[A: SPickler: Unpickler: FastTypeTag, B: SPickler: Unpickler: FastTypeTag](
    name: String,
    database: Database): PersistentMap[A, B] = {
    if(!MTable.getTables(name + ":typeTable").elements.isEmpty)
    
    name + ":recordsTable"
    
    ???
  }
}