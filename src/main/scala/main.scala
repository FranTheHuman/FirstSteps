import play.api.libs.json.{Format, Json}
import play.api.libs.json.Json.toJson

import scala.collection.immutable.{SortedMap, TreeMap}

object Main extends App {



     // ------------------------- How to walk through a Scala collection with ‘reduce’ and ‘fold’ -------------------------

        // Problem:
            // You want to walk through all of the elements in a Scala sequence,
            // comparing two neighboring elements as you walk through the collection.
        // Solution:
            // Use the reduceLeft, foldLeft, reduceRight, and foldRight methods to walk through the elements in a sequence,
            // applying your function to neighboring elements to yield a new result, which is then compared to the next
            // element in the sequence to yield a new result.

            // For example, use reduceLeft to walk through a sequence from left to right (from the first element to the last).
            // reduceLeft starts by comparing the first two elements in the collection with your algorithm,
            // and returns a result.

            val a = Array(1, 2, 3)
            a.reduceRight(_ + _)
            a.reduceLeft(_ + _)
            a.foldLeft(20)(_ + _)
            a.foldLeft(100)(_ + _)


     // --------------------- EJERCICIOS -------------------------

     // 1. Write a Scala program to exchange the first and last characters in a given string and return the new string.
           implicit class FirstExercise(str: String) {
              def firstExercise: String = s"${str.charAt(str.length - 1)}${str.substring(1, str.length - 1)}${str.charAt(0)}"
           }
           println("Hola".firstExercise)
           println("Scala".firstExercise)
           println("The Best Language".firstExercise)

     // 2. Write a Scala program to check whether a given positive number is a multiple of 3 or a multiple of 7
            implicit class SecondExercise(num: Int) {
              def secondExercise: Boolean = (num % 3) == 0 || (num % 7) == 0
            }
            println(6.secondExercise)
            println(49.secondExercise)
            println(56.secondExercise)
            println(145.secondExercise)

     // 3. Write a Scala program to check the largest number among three given integers
            println(List(1, 6, 9, 3, 10, 150, 1000).reduceLeft(_ max _))

     // 4. Write a Scala program to check which number is nearest to the value 100 among two given integers.
     //    Return 0 if the two numbers are equal.
           def fourthExercise(num1: Int, num2: Int): Int = {
             val rest1 = Math.abs(100 - num1)
             val rest2 = Math.abs(100 - num2)
             if(rest1 == rest2) 0
             else if(rest1 < rest2) num1
             else num2
           }
           println(fourthExercise(80, 30))
           println(fourthExercise(100, 99))
           println(fourthExercise(150, 110))

     // 5. Write a Scala program to check whether a given character presents in a string between 2 to 4 times.
           def fifthExercise(str: String, char: Char): Boolean = {
             val quantity = str.count(s => s == char)
             quantity >= 2 && quantity <= 4
           }
           println(fifthExercise("aAdfgdfgaa", 'a'))
           println(fifthExercise("dqoiwdhmuwdpwqdhuwdsifasdqwodspjrdb", 'w'))

     // 6. Write a Scala program to add single element and multiple elements to a given Array.
           val array1: Array[Int] = Array(30, 40, 50, 60)
           20 +: array1 :+ 70

     // 7. Write a Scala program to check a given list is a palindrome or not.
           val palindrome = List("O", "T", "T", "O")
           println(palindrome.reverse == palindrome)

     // 8. Write a Scala program to triplicate each element immediately next to the given list of integers.
           def eighthExercise(list: List[Char]): List[Char] = list flatMap { char => List(char, char, char) }
           println(eighthExercise(List('C', 'H', 'A', 'N', 'C', 'H', 'I', 'T', 'A')))

     // 9. Write a Scala program to sum values of an given array.
           println(List(10, 11, 50, 30, 50, 1).sum)

     // 10.  Create a Scala program to output the following basic JSON notation.

            // mannual json
            val strJson =
              """
                | {
                |  "id": 1,
                |  "name": "Fran",
                |  "description": "awesome"
                | }
                |""".stripMargin

            trait Animal
            case class Dog(race: String, name: String, age: Int) extends Animal
            case class Cat(race: String, name: String, age: Int) extends Animal
            case class Human(nationality: String, name: String, age: Int, favouriteAnimal: Animal) extends Animal

            // implicit lazy val animalFormat: Format[Animal] = Json.format[Animal]
            implicit val dogReads = Json.reads[Dog]
            implicit val dogWrites = Json.writes[Dog]
            // implicit val dogFormat = Json.format[Dog]
            implicit val catReads = Json.reads[Cat]
            implicit val catWrites = Json.writes[Cat]
            // implicit val catFormat = Json.format[Cat]
            // implicit val humanReads = Json.reads[Human]
            // implicit val humanWrites = Json.writes[Human]
            // implicit val humanFormat = Json.format[Human]

            println(toJson(Dog("Pitbull", "Firulais", 4)))
            println(toJson(Cat("Naranjosa", "Cersei", 3)))
            // println(toJson(Human("Argentino", "Francisco", 22, Cat("Naranjosa", "Cersei", 3))))


     // 11. Create a Scala program to calculate the total cost for a customer who is buying 10 Glazed donuts.
     //     You can assume that the price of each Glazed donut item is at $2.50.
     //     Notice the format of the $25.00 total cost literal, which is essentially at 2 decimal places.

            case class Product(name: String, price: Double)
            trait Purchase {
              def getPriceByProduct(quantity: Int, product: Product): Double = quantity * product.price
            }
            case class Customer(name: String) extends Purchase

            val glazedDonut1 = Product("GlazedDonut", 2.50)
            val fran = Customer("Fran")
            println(f"${fran.getPriceByProduct(10, glazedDonut1)}%1.2f")


     // 12. Create a Scala program and use an appropriate data structure to present the following key and value
     //     pairs of children and their ages: Bill is 9 years old, Jonny is 8 years old, Tommy is 11 years old,
     //     and Cindy is 13 years old. Sort out the corresponding child to age in reverse alphabet order.

            object ReverseAlphabetOrder extends Ordering[String] {
                def compare(name1: String, name2: String) = name2.compareTo(name1)
            }
            object ReverseAgeOrder extends Ordering[Int] {
              def compare(age1: Int, age2: Int) = age2.compareTo(age1)
            }
            val students = TreeMap(
              "Bill" -> 9,
              "Jonny" -> 8,
              "Tommy" -> 11,
              "Cindy" -> 13
            )(ReverseAlphabetOrder)
            val studentsReverse = TreeMap(
              9 -> "Bill",
              8 -> "Jonny",
              11 -> "Tommy",
              13 -> "Cindy"
            )(ReverseAgeOrder)
            println(s"Students to ages in reverse order by their names = ${students.mkString(", ")}")
            println(s"Students to ages in reverse order by their ages = ${studentsReverse.mkString(", ")}")


     // 13. Create a Vector with the following numeric items: 0, 10, 20, 47, -2, 99, -98.
     //     Write a Scala program to find the smallest and the largest numeric item in the Vector.

            val numbers: Vector[Int] = Vector(0, 10, 20, 47, -2, 99, -98)
            println(s"The largest number in the vector: ${numbers.reduceLeft(_ max _)}")
            println(s"The smallest number in the vector: ${numbers.reduceLeft(_ min _)}")

     // 14. Write a Scala program and list all the odd numbers between 300 and 350. As a tip,
     //     there is no need to manually create a data structure to represent all the number literals, such as, 300, 301, 302, etc.
     //     Instead, use the handy Range type to help you create number literals between 300 and 350. As a second tip,
     //     experiment with the handy collection functions in Scala that can help you avoid boiler-plate code to solve
     //     this particular problem.

            val oddNumbers = (300 to 350).filter(num => (num % 2) != 0)
            println(s"Odd Numbers from 300 to 350 ${oddNumbers.mkString(", ")}")

     // 15. Write a Scala program and use a case class to model a Lollipop type with a name property of type String.
     //     Thereafter, create an object, or instance, of the Lollipop type.
     //     Using the types from the previous scala exercises - that is, the Donut, VanillaDonut, GlazedDonut, and Pastry classes -
     //     try to define a Pastry object that has an upper type bound to the Lollipop type.
     //     What you will observe is that given the upper type bound constraint of P <: Donut on the Pastry type's constructor argument,
     //     you will receive a compile time error because a Pastry of type Lollipop does not match P <: Donut - in other words,
     //     a Lollipop is not a sub-type of the base Donut type.
     //     Next, create a ShoppingCart class that has an addCartItem() method with a parameter named item that
     //     is represented as a lower type bound with respect to the earlier VanillaDonut type.
     //     The method should have no return type, but will output the following details regarding the item parameter.
     //     The output below is obviously the result of creating an object, or instance, of the ShoppingCart class and calling
     //     the addCartItem() multiple times with values of VanillaDonut, GlazedDonut, Lollipop, and a String.
     //     The String is of course to illustrate the point that you have to understand the lower type bound constraint,
     //     and determine that it is right for your particular use case. In other words, unlike upper type bounds,
     //     the lower type bounds may be too unrestrictive, such as in the example below, where we are able to pass-through a
     //     String value to the addCartItem() method.

            abstract class Donut(name: String) {
              def printName(): Unit
            }

            case class VanillaDonut(name: String) extends Donut(name) {
              override def printName(): Unit = println(name)
            }

            case class GlazedDonut(name: String) extends Donut(name) {
              override def printName(): Unit = println(name)
            }

            val vanillaDonut = VanillaDonut("Vanilla Donut")
            val glazedDonut = GlazedDonut("Glazed Donut")

            // Upper Type Bounds
            class Pastry[P <: Donut](pastry: P){
              def name(): Unit = pastry.printName()
            }

            val vanillaPastry = new Pastry[VanillaDonut](vanillaDonut)
            vanillaPastry.name()

            val glazedPastry = new Pastry[Donut](glazedDonut)
            glazedPastry.name()

            case class Lollipop(name: String)
            val lollipop = Lollipop("Lollipop")
            // val lolliPastry = new Pastry[Lollipop](lollipop)
            // Compile time error because a Lollipop type does not match the P <: Donut constraint

            class ShoppingCart() {
              def addCartItem[I >: VanillaDonut](item: I): Unit = {
                println(s"Adding $item to shopping cart")
                println(item.getClass.getSimpleName)
              }
            }

            val shoppingCart = new ShoppingCart()
            shoppingCart.addCartItem(vanillaDonut)
            shoppingCart.addCartItem(glazedPastry)
            shoppingCart.addCartItem(lollipop)


     // 16. El objetivo de este ejercicio es implementar una función que determine si una palabra está en una sopa de letras.
     //     * - Las palabras pueden estar dispuestas direcciones horizontal o vertical, _no_ en diagonal.
     //     * - Las palabras pueden estar orientadas en cualquier sentido, esto es, de derecha a izquierda o viceversa, y de arriba
     //     * para abajo o viceversa.


     // 17. Se debe ordenar primero por puntuación de manera descendente. Cuando 2 jugadores tienen igual cantidad de puntos,
     //     * el que tiene menos perdidas se lo considerara el mayor. Luego a igual puntos y perdidas se seguirá usando el
     //     * nombre de manera ascendente.
        import scala.collection.immutable.SortedSet


        case class Jugador(nombre: String, puntacion: Int, partidas: Int)

        // OPCION 1
        val jugadoresList: List[Jugador] = List(
          Jugador("Matias", 100, 0),
          Jugador("Alejandro", 100, 0),
          Jugador("Enzo", 50, 0),
          Jugador("Junior", 75, 0),
          Jugador("Pablo", 150, 0)
        ).sortBy { case Jugador(n, p, pa) => (pa, p, n) }
        println(jugadoresList.mkString(", "))

        // OPCION 2
        implicit val puntuacionOrder: Ordering[Jugador] = Ordering.by[Jugador, Int](_.partidas)
          .orElseBy(_.puntacion)
          .orElseBy(_.nombre)

        val jugadores: SortedSet[Jugador] = SortedSet(
          Jugador("Matias", 100, 0),
          Jugador("Alejandro", 100, 0),
          Jugador("Enzo", 50, 0),
          Jugador("Junior", 75, 0),
          Jugador("Pablo", 150, 0)
        )
        println(jugadores.mkString(", "))

        // OPCION 3
        object PuntuacionOrder extends Ordering[Jugador] {
          def compare(jugador1: Jugador, jugador2: Jugador) = {
            jugador2.partidas compare jugador1.partidas match {
              case 0 =>
                (jugador2.puntacion compare jugador1.puntacion) match {
                  case 0 => jugador1.nombre compare jugador2.nombre
                  case c => c
                }
              case other => {
                println(other)
                other
              }
            }
          }
        }
        val jugadoresList1: SortedSet[Jugador] = SortedSet(
          Jugador("Matias", 100, 0),
          Jugador("Alejandro", 100, 0),
          Jugador("Enzo", 50, 0),
          Jugador("Junior", 75, 0),
          Jugador("Pablo", 150, 0)
        )(PuntuacionOrder)
        println(jugadoresList1.mkString(", "))


    /*
      TEST CUIT POC TELEMETY
     */
    println("500-25-41411125-5".split("-", 2)(1))
    println(Math.abs("500-25-41411125-5".split("-", 2)(1).hashCode % 30).toString)


    /*
      TEST (num % 3)
     */
  println(14 % 3)


}
