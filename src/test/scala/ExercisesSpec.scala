import org.scalatest._
import flatspec._
import matchers._

import Main._

class ExercisesSpec extends AnyFlatSpec with must.Matchers {

  "Exercise 1" should "exchange the first and last characters in a given string and return the new string" in {
    assert("Hola".firstExercise === "aolH")
    assert("Scala".firstExercise === "acalS")
    assert("The Best Language".firstExercise === "ehe Best LanguagT")
  }

  "Exercise 2" should "check whether a given positive number is a multiple of 3 or a multiple of 7" in {
    assert(6.secondExercise === true)
    assert(56.secondExercise === true)
    assert(145.secondExercise === false)
  }

  "Exercise 4" should "check which number is nearest to the value 100 among two given integers" in {
    assert(fourthExercise(99, -99) === 99)
  }

}
