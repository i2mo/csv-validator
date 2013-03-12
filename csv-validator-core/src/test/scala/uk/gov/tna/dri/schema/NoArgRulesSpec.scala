package uk.gov.tna.dri.schema

import org.specs2.mutable.Specification
import scalaz.{Failure, Success}
import uk.gov.tna.dri.metadata.{Cell, Row}

/**
 * User: Jim Collins
 * Date: 2/20/13
 */
class NoArgRulesSpec extends Specification {

  val globalDirsOne = List(TotalColumns(1))

  "UriRule" should  {

    "succeed if cell has a valid uri" in {
      val uriRule = UriRule()
      uriRule.evaluate(0, Row(List(Cell("http://datagov.nationalarchives.gov.uk/66/WO/409/9999/0/aaaaaaaa-aaaa-4aaa-9eee-0123456789ab")), 1), Schema(globalDirsOne, List(ColumnDefinition("column1")))) mustEqual Success(true)
    }

    "fail if cell has an invalid uri" in {
      val uriRule = UriRule()
      uriRule.evaluate(0, Row(List(Cell("http://datagovern.nationalarchives.gov.uk/66/WO/409/9999/0/aaaaaaaa-aaaa-4aaa-9eee-0123456789ab")), 1), Schema(globalDirsOne, List(ColumnDefinition("column1")))) must beLike {
        case Failure(messages) => messages.head mustEqual """uri fails for line: 1, column: column1, value: "http://datagovern.nationalarchives.gov.uk/66/WO/409/9999/0/aaaaaaaa-aaaa-4aaa-9eee-0123456789ab""""
      }
    }
  }

  "XsdDateTimeRule" should  {

    "succeed if cell has a valid xsdDateTime" in {
      val xsdDateRule = XsdDateTimeRule()
      xsdDateRule.evaluate(0, Row(List(Cell("2002-99-30T09:00:10")), 1), Schema(globalDirsOne, List(ColumnDefinition("column1")))) mustEqual Success(true)
    }

    "fail if cell has an invalid xsdDateTime" in {
      val xsdDateRule = XsdDateTimeRule()
      xsdDateRule.evaluate(0, Row(List(Cell("2002-999-30T09:00:10")), 1), Schema(globalDirsOne, List(ColumnDefinition("column1")))) must beLike {
        case Failure(messages) => messages.head mustEqual """xDateTime fails for line: 1, column: column1, value: "2002-999-30T09:00:10""""
      }
    }
  }

  "XsdDateRule" should  {

    "succeed if cell has a valid xsdDate" in {
      val xsdDateRule = XsdDateRule()
      xsdDateRule.evaluate(0, Row(List(Cell("2002-99-30")), 1), Schema(globalDirsOne, List(ColumnDefinition("column1")))) mustEqual Success(true)
    }

    "fail if cell has an invalid xsdDate" in {
      val xsdDateRule = XsdDateRule()
      xsdDateRule.evaluate(0, Row(List(Cell("2002-999-30")), 1), Schema(globalDirsOne, List(ColumnDefinition("column1")))) must beLike {
        case Failure(messages) => messages.head mustEqual """xDate fails for line: 1, column: column1, value: "2002-999-30""""
      }
    }
  }

  "UkDateRule" should  {

    "succeed if cell has a valid UK Date" in {
      val ukDateRule = UkDateRule()
      ukDateRule.evaluate(0, Row(List(Cell("99/0/0009")), 1), Schema(globalDirsOne, List(ColumnDefinition("column1")))) mustEqual Success(true)
    }

    "fail if cell has an invalid UK Date" in {
      val ukDateRule = UkDateRule()
      ukDateRule.evaluate(0, Row(List(Cell("990/00/0009")), 1), Schema(globalDirsOne, List(ColumnDefinition("column1")))) must beLike {
        case Failure(messages) => messages.head mustEqual """ukDate fails for line: 1, column: column1, value: "990/00/0009""""
      }
    }
  }

  "XsdTimeRule" should  {

    "succeed if cell has a valid xsdTime" in {
      val xsdTimeRule = XsdTimeRule()
      xsdTimeRule.evaluate(0, Row(List(Cell("99:00:88")), 1), Schema(globalDirsOne, List(ColumnDefinition("column1")))) mustEqual Success(true)
    }

    "fail if cell has an invalid xsdTime" in {
      val xsdTimeRule = XsdTimeRule()
      xsdTimeRule.evaluate(0, Row(List(Cell("99:000:88")), 1), Schema(globalDirsOne, List(ColumnDefinition("column1")))) must beLike {
        case Failure(messages) => messages.head mustEqual """xTime fails for line: 1, column: column1, value: "99:000:88""""
      }
    }
  }

  "Uuid4Rule" should  {

    "succeed if cell has a valid uuid4" in {
      val uuid4Rule = Uuid4Rule()
      uuid4Rule.evaluate(0, Row(List(Cell("aaaaaaaa-aaaa-4aaa-9eee-0123456789ab")), 1), Schema(globalDirsOne, List(ColumnDefinition("column1")))) mustEqual Success(true)
    }

    "fail if cell has an invalid uuid4" in {
      val uuid4Rule = Uuid4Rule()
      uuid4Rule.evaluate(0, Row(List(Cell("aaaaaaaaa-aaaa-4aaa-9eee-0123456789ab")), 1), Schema(globalDirsOne, List(ColumnDefinition("column1")))) must beLike {
        case Failure(messages) => messages.head mustEqual """uuid4 fails for line: 1, column: column1, value: "aaaaaaaaa-aaaa-4aaa-9eee-0123456789ab""""
      }
    }
  }

  "PositiveIntegerRule" should  {

    "succeed if cell has a positive integer" in {
      val posIntRule = PositiveIntegerRule()
      posIntRule.evaluate(0, Row(List(Cell("120912459")), 1), Schema(globalDirsOne, List(ColumnDefinition("column1")))) must beLike { case Success(_) => ok }
    }

    "succeed if cell has a single digit positive integer" in {
      val posIntRule = PositiveIntegerRule()
      posIntRule.evaluate(0, Row(List(Cell("3")), 1), Schema(globalDirsOne, List(ColumnDefinition("column1")))) must beLike { case Success(_) => ok }
    }

    "fail if cell has a negative integer" in {
      val posIntRule = PositiveIntegerRule()
      posIntRule.evaluate(0, Row(List(Cell("-123")), 1), Schema(globalDirsOne, List(ColumnDefinition("column1")))) must beLike {
        case Failure(messages) => messages.head mustEqual """positiveInteger fails for line: 1, column: column1, value: "-123""""
      }
    }

    "fail if cell has a non integer" in {
      val posIntRule = PositiveIntegerRule()
      posIntRule.evaluate(0, Row(List(Cell("123.45")), 1), Schema(globalDirsOne, List(ColumnDefinition("column1")))) must beLike {
        case Failure(messages) => messages.head mustEqual """positiveInteger fails for line: 1, column: column1, value: "123.45""""
      }
    }

    "succeed for cell with a leading zero" in {
      val posIntRule = PositiveIntegerRule()
      posIntRule.evaluate(0, Row(List(Cell("0123")), 1), Schema(globalDirsOne, List(ColumnDefinition("column1")))) must beLike { case Success(_) => ok }
    }

    "fail if cell has a minus sign midway through" in {
      val posIntRule = PositiveIntegerRule()
      posIntRule.evaluate(0, Row(List(Cell("123-4456")), 1), Schema(globalDirsOne, List(ColumnDefinition("column1")))) must beLike {
        case Failure(messages) => messages.head mustEqual """positiveInteger fails for line: 1, column: column1, value: "123-4456""""
      }
    }

    "fail if cell has a non numeric character" in {
      val posIntRule = PositiveIntegerRule()
      posIntRule.evaluate(0, Row(List(Cell("12abc45")), 1), Schema(globalDirsOne, List(ColumnDefinition("column1")))) must beLike {
        case Failure(messages) => messages.head mustEqual """positiveInteger fails for line: 1, column: column1, value: "12abc45""""
      }
    }
  }
}
