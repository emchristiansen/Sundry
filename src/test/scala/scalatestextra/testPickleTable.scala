package scalatestextra

import org.scalatest.fixture
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.scalacheck.Gen
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

import scala.pickling._
import scala.pickling.binary._

import scala.slick.driver.H2Driver.simple._
import Database.threadLocalSession

////////////////////////////////////////////////////////////////////////////////

case class Foo(bar: Int)

@RunWith(classOf[JUnitRunner])
class TestPickleTable extends FunGeneratorSuite {
  val pickleTable = PickleTable[Foo]("TestTable")

  test("a vanilla unit test", InstantTest) {    
    Database.forURL("jdbc:h2:mem:TestPickleTable", driver = "org.h2.Driver") withSession {
      pickleTable.ddl.create
      
      val foo1 = Foo(1)
      val foo2 = Foo(2)
      
      pickleTable.insert(foo1)
      pickleTable.insert(foo2)
      
      val foos = Query(pickleTable).elements.toSet
      
      assert(foos.contains(foo1))
      assert(foos.contains(foo2))
      
//      a.pickle.unpickle[Foo]
    }
  }

  test("a generator driven test", InstantTest) {
    val evenInts = for (n <- Gen.choose(-1000, 1000)) yield 2 * n
    forAll(evenInts) { x =>
      assert(x % 2 == 0)
    }
  }
}