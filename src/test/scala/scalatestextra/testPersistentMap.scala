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

case class Blarg(a: Int, b: String)
case class Flurg(a: Blarg, b: Double)

@RunWith(classOf[JUnitRunner])
class TestPersistentMap extends FunGeneratorSuite {
  val pickleTable = PickleTable[Foo]("TestTable")

  test("a vanilla unit test", InstantTest) {
    val database = Database.forURL("jdbc:sqlite:db.sqlite", driver = "org.sqlite.JDBC")

    val map = PersistentMap.create[Blarg, Flurg]("test", database)

    val blarg1 = Blarg(1, "one")
    val blarg2 = Blarg(2, "two")
    
    val flurg1 = Flurg(blarg1, 1.0)
    val flurg2 = Flurg(blarg2, 2.0)
    
    map += (blarg1 -> flurg1)
  }

  test("a generator driven test", InstantTest) {
    val evenInts = for (n <- Gen.choose(-1000, 1000)) yield 2 * n
    forAll(evenInts) { x =>
      assert(x % 2 == 0)
    }
  }
}