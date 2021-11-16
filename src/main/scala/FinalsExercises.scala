object FinalsExercises extends App {

  /* Definir la función maxTres tal que (maxTres x y z) es el máximo de X, Y y Z. */

  def maxTres(num1: Int, num2: Int, num3: Int): Int = List(num1, num2, num3).max
  println(maxTres(1, 8, 3))
  println(maxTres(7, 20, 15))
  println(maxTres(100, 4, 30))

  /* Definir la función rango tal que (rango xs) es la lista formada por el menor y mayor elemento de xs. */

  def rango(lista: List[Int]): List[Int] = {
    val listaOrdenada = ((lista.toSet).toList).sorted
    List(listaOrdenada.head, listaOrdenada.last)
  }
  println(rango(List(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)))
  println(rango(List(10, 3, 3, 7, 5, 6, 8, 8, 20, 10)))
  println(rango(List(10, 1, 55, 7, 80, 6, 8, 55, 20, 10)))

  /* Definir la función interior tal que (interior xs) es la lista obtenida eliminando los extremos de la lista xs.v*/

  def interior(lista: List[Int]): List[Int] = {
    val listaOrdenada = ((lista.toSet).toList).sorted
    listaOrdenada.drop(1).dropRight(1)
  }

  println(interior(List(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)))
  println(interior(List(10, 3, 3, 7, 5, 6, 8, 8, 20, 10)))
  println(interior(List(10, 1, 55, 7, 80, 6, 8, 55, 20, 10)))

  /* Definir la función finales tal que (finales n xs) es la lista formada por los n finales elementos de xs. */

  def finales(cantidad: Int, lista: List[Int]): List[Int] = lista.drop(Math.abs(cantidad - lista.size))

  println(finales(3, List(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)))
  println(finales(5, List(10, 3, 3, 7, 5, 6, 8, 8, 20, 10)))
  println(finales(2, List(10, 1, 55, 7, 80, 6, 8, 55, 20, 10)))

  /*  Definir la función segmento tal que (segmento m n xs) es la lista de los elementos de xs comprendidos entre las posiciones m y n.
     Por ejemplo:
       segmento 3 4 [3,4,1,2,7,9,0] == [1,2]
       segmento 3 5 [3,4,1,2,7,9,0] == [1,2
   */

  def segmento(init: Int, fin: Int, lista: List[Int]): List[Int] = {
    val inicio: Int = if (init == 0) init else init - 1
    lista.slice(inicio, fin)
  }

  println(segmento(3, 4, List(3,4,1,2,7,9,0)))
  println(segmento(3, 5, List(3,4,1,2,7,9,0)))
  println(segmento(5, 3, List(3,4,1,2,7,9,0)))


  // Definir la funcion numeroMayor tal que (numeroMayor x y) es el mayor número de dos cifras que puede
  // construirse con los dígitos x e y.

  def numeroMayor(num1: Int, num2: Int): Int = num1 match {
    case `num2` => s"$num1$num2".toInt
    case _ if num1 < num2 => s"$num2$num1".toInt
    case _ => s"$num1$num2".toInt
  }

  println(numeroMayor(2, 5))
  println(numeroMayor(5, 2))
  println(numeroMayor(7, 4))
  println(numeroMayor(4, 7))
  println(numeroMayor(10, 11))
  println(numeroMayor(11, 10))
  println(numeroMayor(22, 22))

  // Escribir una función que reciba una frase y devuelva un diccionario con las palabras que contiene y su longitud.

  def fraseDetalles(frase: String) = {
    val palbras = frase.split(" ")
    Map(
      "palabras" -> frase.split(" ").mkString(" - "),
      "longitud" -> palbras.size
    )
  }

  println(fraseDetalles("La cersei se va a jugar con la luna"))

  /*

  Write a Scala program that defines a base abstract class to model a Vehicle type that has a public make property of type String. ç
  This particular Vehicle base class will be extended by two sub-types, namely, a Car, and a Bike, case class,
  and will wire accordingly the make property of type String from the Vehicle base class.
  Next, create a singleton object named VehicleReport that will define a printVehicles method that will have as input a List of Vehicle types,
  and any of its sub-types or sub-classes. The printVehicles() method will simply iterate through each of the Vehicle type and output its corresponding make property.
  Note that the printVehicles() method will have no return type defined as such. You can use the following vehicle samples to model your data points:
    a car whose make is: bmw 3 series
    a car whose make is: vw golf
    a bike whose make is: bmw g 310 r bike
    a bike whose make is: fire storm bike
  Use the List data structure from Scala's collection type and store the above-mentioned vehicles.
  And, finally, call the VehicleReport's printVehicles() method by passing through your collection of vehicles as defined above.

*/

  abstract class Vehicle(val make: String)

  case class Bike(override val make: String) extends Vehicle(make)
  case class Car(override val make: String) extends Vehicle(make)

  final object VehicleReport {
    def printVehicles[V <: Vehicle](vehicles: List[V]) =
      vehicles.map(_.make)
  }

  val bmwCar = Car("bmw 3 series")
  val vwCar = Car("vw golf")
  val bmwBike = Bike("bmw g 310 r bike")
  val hondaBike = Bike("fire storm bike")
  val vehicles = List(bmwCar, vwCar, bmwBike, hondaBike)
  println(VehicleReport.printVehicles(vehicles))

}
