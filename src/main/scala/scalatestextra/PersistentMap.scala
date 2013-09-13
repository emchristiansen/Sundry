package scalatestextra

import scala.pickling._
import scala.pickling.binary._
import scala.slick.session.Database
import scala.slick.session.Session
import scala.slick.jdbc._
import StaticQuery.interpolation
import scala.slick.jdbc.meta.MTable
import scala.slick.session.PositionedParameters
import scala.slick.session.PositionedResult

///////////////////////////////////////////

case class TypeRecord(keyType: String, valueType: String)

case class KeyValueRecord[A, B](key: A, value: B)

/**
 * A mutable map which is backed by a database.
 *
 * `name` is the name of this map, and is used to connect to
 * correct tables in the database.
 * _These tables must already exist_; see `PersistentMap.create`
 * to create a new map.
 * `database` holds the table in which this map is stored.
 */
class PersistentMap[A: SPickler: Unpickler: FastTypeTag, B: SPickler: Unpickler: FastTypeTag](
  //  name: String,
  database: Database,
  typeTableName: String,
  recordsTableName: String) extends collection.mutable.Map[A, B] {
  // This table stores a single record, recording the type information
  // of this map.
  // It is used to verify the table the map connects to actually stores
  // the required types.
  // TODO: Store type tags, not strings.
  //  val typeTable: TypeTable

  // This table stores the data in the map.
  // The first entry, of type `Long`, stores hash codes and is used
  // for efficient lookup.
  //  val recordsTable: RecordsTable[A, B]

  implicit val getTypeRecordResult =
    GetResult(r => TypeRecord(r.nextString, r.nextString))
  implicit val getKeyValueRecordResult =
    GetResult(r => KeyValueRecord(
      BinaryPickle(r.nextBytes).unpickle[A],
      BinaryPickle(r.nextBytes).unpickle[B]))

  // These implicits come from
  // https://github.com/slick/slick/issues/97
  implicit object SetByteArray extends SetParameter[Array[Byte]] {
    def apply(v: Array[Byte], pp: PositionedParameters) {
      pp.setBytes(v)
    }
  }

  implicit object SetByteArrayOption extends SetParameter[Option[Array[Byte]]] {
    def apply(v: Option[Array[Byte]], pp: PositionedParameters) {
      pp.setBytesOption(v)
    }
  }

  implicit object GetByteArray extends GetResult[Array[Byte]] {
    def apply(rs: PositionedResult) = rs.nextBytes()
  }

  implicit object GetByteArrayOption extends GetResult[Option[Array[Byte]]] {
    def apply(rs: PositionedResult) = rs.nextBytesOption()
  }

  // Do consistency checks on the two backing tables.
  database withSession { implicit session: Session =>
    // The type table:
    // 1) must exist,
    require(MTable.getTables(typeTableName).list().size == 1)
    val entries = sql"select * from #$typeTableName".as[TypeRecord].list
    // 2) must have exactly one entry,
    require(entries.size == 1)
    // 3) and that entry must reflect the required type.
    require(entries.head.keyType == implicitly[FastTypeTag[A]].toString)
    require(entries.head.valueType == implicitly[FastTypeTag[B]].toString)

    // The records table must exist.
    require(MTable.getTables(recordsTableName).list().size == 1)
  }

  override def get(key: A): Option[B] = {
    database withSession { implicit session: Session =>
      // We look up the key-value pair using the key's hash code.
      val pickledKey = key.pickle.value
      val list =
        sql"select * from #$recordsTableName where key = $pickledKey".as[KeyValueRecord[A, B]].list

      // We should get either zero or one result.
      assert(list.size == 0 || list.size == 1)

      list.headOption map { _.value }
    }
  }

  override def iterator: Iterator[(A, B)] = {
    database withSession { implicit session: Session =>
      // We simply return all the database entries, dropping the hash codes.
      // TODO: Make this a proper enumerator by either using a newer
      // version of Slick or by using the workaround described here:
      // http://stackoverflow.com/questions/16728545/nullpointerexception-when-plain-sql-and-string-interpolation
      sql"select * from #$recordsTableName".as[KeyValueRecord[A, B]].list.toIterator map {
        record => (record.key, record.value)
      }
    }
  }

  override def +=(kv: (A, B)): this.type = {
    database withSession { implicit session: Session =>
      val (key, value) = kv
      // If this key is in the database already, we delete its record.
      this -= key

      // Then we insert our new record.
      sqlu"insert into #$recordsTableName values(${key.pickle.value}, ${value.pickle.value})".first
    }

    this
  }

  override def -=(key: A): this.type = {
    database withSession { implicit session: Session =>
      sqlu"delete from #$recordsTableName where key = ${key.pickle.value}".first
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
    val typeTableName = name + "TypeTable"
    val recordsTableName = name + "RecordsTable"

    database withSession { implicit session: Session =>
      // Initialize the type table.      
      if (!MTable.getTables(typeTableName).elements.isEmpty)
        sqlu"drop table #$typeTableName".first

      sqlu"create table #$typeTableName(keyType varchar not null, valueType varchar not null)".first

      val aString = implicitly[FastTypeTag[A]].toString
      val bString = implicitly[FastTypeTag[B]].toString
      sqlu"insert into #$typeTableName values($aString, $bString)".first

      // Initialize the records table.
      if (!MTable.getTables(recordsTableName).elements.isEmpty)
        sqlu"drop table #$recordsTableName".first

      sqlu"create table #$recordsTableName(key blob not null primary key, value blob not null)".first
    }

    // Build the final map.
    new PersistentMap[A, B](database, typeTableName, recordsTableName)
  }
}