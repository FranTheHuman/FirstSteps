object DDD_Theoretical {

  // Lo primero que me gustaría decir es que DDD no difiere mucho del paradigma de Programación Orientado a Objetos (POO).
  // En POO se busca describir la realidad en un modelo expresado con Objetos.
  // Además estos objetos tienen comportamiento o dicho de otra manera, tienen operaciones.
  // Y entonces qué es lo que agrega DDD?

  // DDD establece algunas prácticas y criterios de cómo organizar el modelado o diseño de la solución.
  // Como su nombre lo indica: el diseño va estar guiado por el Dominio.
  // Con Dominio nos referimos a una parte de la realidad que nos interesa expresar en un Modelo computacional.
  // Entonces, podemos inferir que es super importante entender el Dominio, es más,
  // es super importante entender el lenguaje con el cual se expresa dicho Dominio.

    // Entities, Value Objects y Aggregates

      // En POO, Producto es un Objeto. Este Objeto posee otros atributos,
      // como por ejemplo: Descripción, Color, etc.
      // Los atributos que en sí mismo representan un Valor y ese valor no puede mutar,
      // en DDD se denomina Value Object (o Valor). A su vez, como el objeto Producto, en el Dominio del Retail,
      // tiene una identidad conceptual, en DDD se denomina Entity.

      // En el momento en que encontramos un conjunto de objetos (Entidades y Valores)
      // agrupados cohesivamente en un contexto determinado, en DDD lo vamos a llamar Aggregate.
      // En nuestro Dominio de ejemplo, el Producto es un Aggregate. Los Aggregate tienen una Entidad que los identifica,
      // esta Entidad se denomina Root Aggregate. Entonces, la entidad Producto pasa a ser el Root Aggregate.

    // Bounded Context

       // A medida que vamos descubriendo el dominio empezamos a encontrar que podemos agrupar los Aggregates
       // de manera cohesiva y que se relacionan con otro grupo de Aggregates a través un subconjunto de entidades.
       // A estos grupos de Aggregates los podemos organizar en Módulos o Subdominios.
       // Por ejemplo: Pagos, Ordenes, Productos, Canales de Venta, etc. Los límites del Subdominio se llaman Bounded Context.

       // El Subdominio ya nos da un marco conceptual para construir un Microservicio.
}
