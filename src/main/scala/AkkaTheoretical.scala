object AkkaTheoretical {

  // resilient elastic distributed real-time transaction processing

  // Write correct distributed, concurrent, fault-tolerant and scalable applications is too hard.
  //  Akka is here to change that.
  //  Using the Actor Model we raise the abstraction level and provide a better platform to build scalable,
  //  resilient and responsive applications

  // Actors:
    // un actor es un objeto que vive en memoria y encapsula el estado de una entidad del dominio.
    // Por ejemplo, un producto en particular o la orden #145 de una tienda específica.

    // Cualquier cambio que se quiera hacer sobre este estado debe hacerse mediante un mensaje que se envía al actor de
    // forma asincrónica. Usando los términos de Domain Driven Design, los actores implementan aggregates
    // y los mensajes nos permiten enviar comandos.

    // Un Aggregate lo vamos representar con un Actor.

    // Event Sourcing es poder reconstituir un Aggregate a partir de sus Eventos de Dominio.

    // # ActorRef:
         // Para enviar estos mensajes, Akka nos provee lo que llama una referencia (ActorRef) que nos abstrae de la
         // ubicación real de los actores. Mediante esta referencia, Akka se encarga de resolver la ubicación real del
         // actor para hacerle llegar el mensaje, ya sea en la misma JVM o en un host externo.

    // # Mailbox:
         // los mensajes que recibe un actor no se procesan inmediatamente, sino que se encolan en un mailbox
         // y se van procesando uno a uno.

  // ¿Cómo ser Elásticos y Resilientes con Akka?

    // En este ejemplo, cada tienda tiene un árbol donde se agrupan sus productos por sus características en forma jerárquica.
    // Dicho árbol se pide cada vez que se renderiza la página principal de una tienda.
    // La tasa de consulta es muy alta: en el caso de Tiendanube hablamos de unos treinta mil peticiones (request) por minuto aproximadamente (30k rpm).
    // El hecho de usar Actores para mantener el estado del árbol nos da una latencia muy baja y constante,
    // pero tambíen se va a ver degradada si los rpm son muy altos por el uso de hardware: CPU y memoria fundamentalmente.
  
    // Bien, en este punto tenemos dos opciones: aumentar la potencia del hardware existente o agregar más hardware (nodos),
    // es decir, tenemos que Escalar.
    // El primer caso se conoce como escalabilidad vertical y el segundo como escalabilidad horizontal.

    // En definitiva, agregando más nodos vamos a distribuir la carga: los requests se van a repartir entre los nodos existentes.
    // El simple hecho de hacer esto tiene varios problemas: el primero es mantener la consistencia entre los nodos.
    // Y si nuestra aplicación no está preparada para escalar horizontalmente vamos a tener que agregar codigo
    // especifico para soportarlo.

  // ----------------------------------------------------------------------------------
  // Akka Cluster nos permite construir un Sistema de Actores (ActorSystem) distribuido,
  // es decir que un solo ActorSystem va a estar repartido entre multiples nodos.
  // ----------------------------------------------------------------------------------

    // El identificador de cada árbol es el Id de la tienda. Supongamos que ya estamos dentro de un cluster y
    // le llega al Nodo 1 un request pidiendo el árbol con Id 1, entonces, como es un Actor Persistente,
    // se hace el recovery de todos sus evento para crear el Actor que representa el árbol y
    // se devuelve el último estado del árbol.
    // Hasta acá todo bien, pero ahora llega un nuevo request al Nodo 2 por el mismo árbol con Id 1.
    //  Si creamos una nueva instancia de este árbol en este nodo vamos a tener dos actores que representan
    //  el mismo árbol dentro del custer. Qué pasa si cambia de estado uno de los árboles debido a que se
    //  agregó una categoría más al árbol: CHAN!

   // ----------------------------------------------------------------------------------
   // Cluster Sharding nos provee la posibilidad de distribuir un conjunto de actores por todo el Cluster,
   //  y acceder a ellos sin importar donde están instanciados físicamente. Este feature es extremadamente
   //  útil para los casos que los actores son persistentes y es necesario mantener una solo instancia dentro del Cluster.
   //  En Akka estos actores shardeados se llaman Entidades y nosotros los podemos pensar como Aggregates.
   // ----------------------------------------------------------------------------------

   // Network Partitions (Split Brain)

     // Un aspecto muy importante en un cluster stateful es poder detectar si uno de los miembros está disponible
     // y en caso de que no, tomar acciones lo mas tempranas posibles. Para esto, existe asociado al protocolo gossip,
     // lo que se conoce como Failure Detector. Periódicamente los nodos se envían mensajes entre sí para indicar que
     // estan “vivos”. Esta frecuencia puede variar y dependerá del tamaño del Cluster y de las condiciones de la red.
     // Si uno o varios nodos dejan de enviar estos mensajes (heartbeats), cada nodo al que no le llegue el heatbeat
     // va a marcar, en su lista de miembros, como “nodo no alcanzado” (unreachable).
     // En este momento lo que está pasando es que algunos nodos se ven entre sí y otros no;
     // esta condición de llama Network Partition o Split Brain.

  // Understanding dispatchers in akka

      // Thread Pools

        // Background
        // Server Programs such as database and web servers repeatedly execute requests from multiple clients and these
        // are oriented around processing a large number of short tasks. An approach for building a server application would
        // be to create a new thread each time a request arrives and service this new request in the newly created thread.
        // While this approach seems simple to implement, it has significant disadvantages. A server that creates a new thread
        // for every request would spend more time and consume more system resources in creating and destroying threads than
        // processing actual requests.

        //Since active threads consume system resources, a JVM creating too many threads at the same time can cause the system
        // to run out of memory. This necessitates the need to limit the number of threads being created.

        // What is ThreadPool in Java?

             // A thread pool reuses previously created threads to execute current tasks and offers a solution to
             // the problem of thread cycle overhead and resource thrashing. Since the thread is already existing when
             // the request arrives, the delay introduced by thread creation is eliminated,
             // making the application more responsive.


    // Akka dispatcher

       // Easiest is to think about a dispatcher as about a thread pool (which it really is)
       // that is used to run your actors. Depending on the nature of your actors you can run them on
       // fork-join thread pool (most common) or on CachedThreadPool (if you're dong IO) etc.

    // In Akka, they are responsible for selecting an actor and it’s messages and assigning them to the CPU.
    // So let’s understand this with an example.

     // The role of the dispatcher is to:

       // 1. Select an actor and pick a message from the mailbox queue of that actor.
       // 2. The actor and the message selected are allocated to a thread for its execution.
       // 3. The thread to which the allocation is done is now mapped to a processor.

     // Fork-join-executor
            // fork-join-executor allows you to have a static thread pool configuration where number of
            // threads will be between parallelism-min and parallelism-max bounds.

         /*
         my-dispatcher {
           # Dispatcher is the name of the event-based dispatcher
           type = Dispatcher
           # What kind of ExecutionService to use
             executor = "fork-join-executor"
           # Configuration for the fork join pool
             fork-join-executor {
               # Min number of threads to cap factor-based parallelism number to
                 parallelism-min = 2
               # Parallelism (threads) ... ceil(available processors * factor)
               parallelism-factor = 2.0
               # Max number of threads to cap factor-based parallelism number to
                 parallelism-max = 10
             }
           # Throughput defines the maximum number of messages to be
           # processed per actor before the thread jumps to the next actor.
           # Set to 1 for as fair as possible.
           throughput = 100
         }*/

     // Thread-pool-executor
            // You can use thread-pool-executor if you want your thread pool to have dynamic nature.
            // thread-pool-executor differs from fork-join-executor in that it allows us to have a dynamic
            // thread pool that can grow and shrink depending on the load.
            // thread-pool-executor has core-pool-size-* and max-pool-size-* properties that
            // control how thread-pool-executor grows. core-pool-size-* are used to define minimum
            // number of threads that ThreadPool will have. This works similarly to how fork-join-executor works.

      // https://shekhargulati.com/2017/09/30/understanding-akka-dispatchers/#:~:text=thread%2Dpool%2Dexecutor%20differs%20from,thread%2Dpool%2Dexecutor%20grows.
}
