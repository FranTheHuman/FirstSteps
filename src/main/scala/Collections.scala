object Collections {

  // -------------------- COLLECTIONS TYPES -------------------------

  // ------------------------- Seq -------------------------

  // Seq is a trait which represents indexed sequences that are guaranteed immutable.
  // It maintains insertion order of elements.
  // It is used to represent indexed sequences that are having a defined order of element i.e. guaranteed immutable.

  var seq:Seq[Int] = Seq(52,85,1,8,3,2,7)

  println("\nis Empty: "+seq.isEmpty)
  println("Ends with (2,7): "+ seq.endsWith(Seq(2,7)))
  println("contains 8: "+ seq.contains(8))
  println("last index of 3 : "+seq.lastIndexOf(3))
  println("Reverse order of sequence: "+seq.reverse)

  // ------------------------- Set -------------------------

  // A Set is a collection that contains no duplicate elements.
  // It does not maintain any order for storing elements.

  val fruit1 = Set("apples", "oranges", "pears")
  val fruit2 = Set("mangoes", "banana")
  var fruit = fruit1 ++ fruit2

  println( "Head of fruit : " + fruit.head )
  println( "Tail of fruit : " + fruit.tail ) // This method returns a set consisting of all elements except the first.
  println( "Check if fruit is empty : " + fruit.isEmpty )

  // ------------------------- Map -------------------------

  // Scala map is a collection of key/value pairs.
  // Any value can be retrieved based on its key. Keys are unique in the Map, but values need not be unique.
  val colors: Map[String, String] = Map("red" -> "#FF0000", "azure" -> "#F0FFFF")

  println( "Keys in colors : " + colors.keys )
  println( "Values in colors : " + colors.values )
  println( "Check if colors is empty : " + colors.isEmpty )

  // ------------------------- List -------------------------

  // A list is a collection which contains immutable data. List represents linked list in Scala.
  // The Scala List class holds a sequenced, linear list of items.

  val exampleList: List[Int] = List(1, 2, 3)

  // All lists can be defined using two fundamental building blocks, a tail Nil and ::, which is pronounced cons.
  // Nil also represents the empty list.
  val nums = 1 :: (2 :: (3 :: (4 :: Nil)))

  // ------------------------- Array -------------------------

  // Arrays are mutable, indexed collections of values.
  var name = Array("gfg", "geeks", "GeeksQuize", "geeksforgeeks" )
  // Accessing an array element
  println(name(1) )

  // create a 2D array
  val matrix = Array.ofDim[Int](2,2)
  matrix(0)(0) = 0
  matrix(0)(1) = 1
  matrix foreach { row => row foreach print; println }

  // ------------------------- Iterator -------------------------

  // Iterators are data structures that allow to iterate over a sequence of elements.
  // They have a hasNext method for checking if there is a next element available, and a next method which returns
  // the next element and discards it from the iterator.
  val iterable: Iterable[Int] = 1 to 4
  iterable.take(2)
  val iterator = iterable.iterator
  if (iterator.hasNext) iterator.next

  // ------------------------- Vector -------------------------

  // Vectors in scala are immutable data structures providing random access for elements and is similar to the list.
  // In Scala a vector is an immutable collection. But it can be added to, and updated, fast.
  // Unlike an array, a vector's elements cannot be reassigned. But with update() we can change elements in a vector,
  // copying only the nodes needed.

  // Creating vector
  var vector1 = Vector(2, 3, 4, 5)

  // Adding new elements into new vector
  var newVector = vector1 :+ 10

  // Creating vector
  var vector2 = Vector(7, 100)

  // Merging two vectors
  var mergeVector = newVector ++ vector2


  // ------------------------- Tuple -------------------------

  // A tuple is a collection of elements in ordered form.
  // Scala tuple combines a fixed number of items together so that they can be passed around as a whole.
  // Unlike an array or list, a tuple can hold objects with different types but they are also immutable.
  val t = Tuple3(1, "hello", Console)

  // ------------------------- SortedSet -------------------------

  // To retrieve values from a set in sorted order, use a SortedSet.
  val sortSet = scala.collection.SortedSet(10, 4, 8, 2)
  // sortSet: scala.collection.SortedSet[Int] = TreeSet(2, 4, 8, 10)
  implicit val reverseOrdering = Ordering[Int].reverse
  // sortSet -> result TreeSet(10, 8, 4, 2)

  // CONCLUSION:
  // Seq ----> INMUTABLE - MANTIENE EL ORDEN DE LOS ELEMENTOS
  // Set ----> NO CONTIENE ELEMENTOS DUPLICADOS - NO MANTIENE EL ORDEN DE LOS ELEMENTOS
  // Map ----> LLAVE/VALOR
  // List ---> INMUTABLE - ITEMS SECUENCIALES Y LINEALES
  // Array ----> MUTABLE - INDEXADO - MULTIDIMENSIONALES
  // Iterator ----> PERMITE ITERAR SECUENCIAS DE ELEMENTOS - HASNEXT
  // Vector ----> INMUTABLE
  // Tuple ----> INMUTABLE - COLECCION ORDENADA - MUCHOS TIPOS DE ELEMENTOS
  // SortedSet ----> INMUTABLE - ORDENADO CONDICIONALMENTE

}
