import scala.collection.SortedSet

object MoreExercises extends App {

  def compareTriplets(a: Array[Int], b: Array[Int]): Array[Int] = {
    val puntos = a
                  .toList
                  .zip(b.toList)
                  .flatten {
                    case puntos if puntos._1 > puntos._2 => List(1, 0)
                    case puntos if puntos._1 < puntos._2 => List(0, 1)
                    case _ => List(0, 0)
                  }
    Array(
      puntos.zipWithIndex.filter(puntos => puntos._2 == 1 || puntos._2 == 3 || puntos._2 == 5).map(puntos => puntos._1).sum,
      puntos.zipWithIndex.filter(puntos => puntos._2 == 0 || puntos._2 == 2 || puntos._2 == 4).map(puntos => puntos._1).sum
    )
  }

  compareTriplets(Array(2, 25, 0), Array(1, 20, 1))

  def aVeryBigSum(ar: Array[Long]): Long = {
    ar.sum
  }

  def diagonalDifference(arr: Array[Array[Int]]): Int = {
    // Write your code here
    val leftToRigth: Int = arr
      .zipWithIndex
      .map {
        case filas: (Array[Int], Int) => {
          filas._1(0 + filas._2)
        }
      }
      .sum
    val rigthToLeft: Int = arr
      .zipWithIndex
      .map {
        case filas: (Array[Int], Int) => {
          filas._1((filas._1.length - 1) - filas._2)
        }
      }
      .sum
    Math.abs(leftToRigth - rigthToLeft)
  }

  diagonalDifference(
    Array(
      Array(11, 2, 4),
      Array(4, 5, 6),
      Array(10, 8, -12),
    )
  )

  def plusMinus(arr: Array[Int]) {

    val positivos: Array[Int] = arr.filter(num => num > 0)
    val negativos: Array[Int] = arr.filter(num => num < 0)
    val ceros: Array[Int] = arr.filter(num => num == 0)

    println(positivos.length.toDouble /  arr.length.toDouble)
    println(negativos.length.toDouble /  arr.length.toDouble)
    println(ceros.length.toDouble /  arr.length.toDouble)

  }

  def maximumToys(prices: Array[Int], k: Int): Int = {
    var contador = 0
    prices
      .toList
      .filter(price => price <= k)
      .sortWith(_ < _)
      .foldLeft(1)((acc: Int, price: Int) => {
        println(price)
        if(acc <= k) {
          contador += 1
          acc + price
        }else acc
      })
    println(contador)
    contador
  }

  maximumToys(Array(1, 12, 5, 111, 200, 1000, 10), 50)*/


  case class Player(score: Int, name: String)

  val playerComparator: Ordering[Player] = Ordering.by[Player, Int](_.score).reverse.orElseBy(_.name)
  object playerComparator2 extends Ordering[Player] {
    override def compare(x: Player, y: Player): Int = {
      y.score compare x.score match {
        case 0 => x.name compare y.name
        case other => other
      }
    }
  }

  def comparator(a: Player, b: Player): SortedSet[Player] = SortedSet(a, b)(playerComparator2)


  println(comparator(Player(60, "ani"), Player(60, "ana")).mkString(", "))*/


  def rotLeft(a: Array[Int], d: Int): Array[Int] = {
    a.drop(d) ++ a.take(d)
  }

  println(rotLeft(Array(1, 2, 3, 4, 5), 4).mkString(", "))*/

  def twoStrings(s1: String, s2: String): String = {
    s1.map( (value: Char) => s2 contains value ).contains(true) match {
      case true => "YES"
      case _ => "NO"
    }
  }
  println(twoStrings("hello", "world"))*/


  def staircase(n: Int) {
    (1 to n).zipWithIndex.foreach { value =>
      println(" " *  ((n - 1) - value._2) + "#" * value._1)
    }
  }
  staircase(6)

    def miniMaxSum(arr: Array[Int]) {
      val x = arr.sum
      println(s"${x - arr.max} ${x - arr.min}")
    }

}
