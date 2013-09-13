package scalatestextra

import org.scalatest.fixture
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.scalacheck.Gen
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

import scala.pickling._
import scala.pickling.binary._

//import scala.slick.driver.H2Driver.simple._
import scala.slick.driver.SQLiteDriver.simple._
//import Database.threadLocalSession

////////////////////////////////////////////////////////////////////////////////

case class MyKey(a: Int, b: String)
case class MyValue(a: MyKey, b: Double)

@RunWith(classOf[JUnitRunner])
class TestPersistentMap extends FunGeneratorSuite {
  val pickleTable = PickleTable[Foo]("TestTable")

  test("a vanilla unit test", InstantTest) {
    val database = Database.forURL("jdbc:sqlite:db.sqlite", driver = "org.sqlite.JDBC")

    val map = PersistentMap.create[MyKey, MyValue]("test", database)

    val key1 = MyKey(1, "one")
    val key2 = MyKey(2, "two")
    
    val value1 = MyValue(key1, 1.0)
    val value2 = MyValue(key2, 2.0)
    
    val kv11 = key1 -> value1
    val kv22 = key2 -> value2
    val kv12 = key1 -> value2
    
    assert(map.get(key1) == None)
    assert(map.get(key2) == None) 
    assert(map.toSet == Set())
    
    map += kv11
    
    assert(map.get(key1) == Some(value1))
    assert(map.get(key2) == None)
    assert(map.toSet == Set(kv11))

    map += kv22
    
    assert(map.get(key1) == Some(value1))
    assert(map.get(key2) == Some(value2))
    assert(map.toSet == Set(kv11, kv22))
        
    map -= key1
    
    assert(map.get(key1) == None)
    assert(map.get(key2) == Some(value2))
    assert(map.toSet == Set(kv22))
    
    map += kv12
    
    assert(map.get(key1) == Some(value2))
    assert(map.get(key2) == Some(value2))
    assert(map.toSet == Set(kv12, kv22))
  }

  test("a generator driven test", InstantTest) {
    val evenInts = for (n <- Gen.choose(-1000, 1000)) yield 2 * n
    forAll(evenInts) { x =>
      assert(x % 2 == 0)
    }
  }
}