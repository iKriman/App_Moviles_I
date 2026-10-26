
fun main() {
    // saludo de bienvenida
    println("------------------")
    println("   Bienvenido!    ")
    println("------------------")

    // datos del cliente ingresados mediante consola
    print("Ingrese su nombre: ")
    val nombre: String = readlnOrNull()
        ?.trim()
        ?.takeIf { it.isNotEmpty() } ?: "Sin Nombre"

    print("Ingrese su direccion: ")
    val direccion: String = readlnOrNull()
        ?.trim()
        ?.takeIf { it.isNotEmpty() } ?: "Sin Direccion"

    print("Ingrese su telefono: ")
    val telefono: String = readlnOrNull()
        ?.trim()
        ?.takeIf { it.matches(Regex("\\+?[0-9]{9,12}")) }
        ?: "Sin Telefono"

    // info mostrada en consola
    println() // salto de linea
    println("------CLIENTE REGISTRADO------")
    println("Nombre: $nombre")
    println("Direccion: $direccion")
    println("Telefono: $telefono")

    println()
    println("--- NUEVO PEDIDO ---")

    // informacion de la pizza
    val nombrePizza: String = "Pepperoni"
    val precioUnitario: Int = 8000

    println("Pizza seleccionada: $nombrePizza")
    println("Precio unitario: $$precioUnitario")

    // solicitar cantidad de pizzas
    var cantidadPizzas: Int

    do {
        print("Ingrese cantidad de pizzas: ")
        cantidadPizzas = readlnOrNull()?.toIntOrNull() ?: 0

        if (cantidadPizzas <= 0) {
            println("Error: Ingrese una cantidad valida.")
        }
    } while (cantidadPizzas <= 0)

    // calcular subtotal
    val subtotal: Int = precioUnitario * cantidadPizzas

    // aplicar descuento si compra mas de 3 pizzas
    val descuento: Int = if (cantidadPizzas > 3) {
        subtotal * 10 / 100
    } else {
        0
    }

    val totalPedido: Int = subtotal - descuento

    // mostrar resultado en consola
    println()
    println("--- COTIZACION DEL PEDIDO ---")
    println("Subtotal: $$subtotal")
    println("Descuento: $$descuento")
    println("Total estimado: $$totalPedido")

    println()
    println("=== VERIFICACION DE STOCK ===")

    // cantidad de pizzas que podemos preparar
    var ingredientesDisponibles: Int = 10

    // identificador y estado inicial del pedido
    val idPedido: Int = 1
    var estadoPedido: String = "Pendiente"

    // comprobar disponibilidad de ingredientes
    val mensajeStock: String = if (
        cantidadPizzas > 0 &&
        cantidadPizzas <= ingredientesDisponibles
    ) {
        ingredientesDisponibles -= cantidadPizzas
        estadoPedido = "En preparacion"
        "Pedido confirmado correctamente"
    } else {
        estadoPedido = "Rechazado"
        "No hay suficientes ingredientes o la cantidad es invalida"
    }

    println(mensajeStock)
    println("Estado del pedido: $estadoPedido")
    println("Ingredientes restantes: $ingredientesDisponibles")

    // asignacion del repartidor
    val repartidorAsignado: String = if (
        estadoPedido == "En preparacion"
    ) {
        "Carlos"
    } else {
        "Sin asignar"
    }

    println()
    println("--------------------------------")
    println("       RESUMEN DEL PEDIDO       ")
    println("--------------------------------")

    // informacion del pedido
    println("Numero de pedido: $idPedido")

    // datos del cliente
    println()
    println("--- DATOS DEL CLIENTE ---")
    println("Nombre: $nombre")
    println("Direccion: $direccion")
    println("Telefono: $telefono")

    // informacion de la pizza
    println()
    println("--- INFORMACION DE LA PIZZA ---")
    println("Pizza: $nombrePizza")
    println("Precio unitario: $$precioUnitario")

    // detalles del pedido
    println()
    println("--- DETALLES DEL PEDIDO ---")
    println("Cantidad de pizzas: $cantidadPizzas")
    println("Subtotal: $$subtotal")

    if (estadoPedido == "En preparacion") {
        println("Descuento aplicado: $$descuento")
        println("Total a pagar: $$totalPedido")
    } else {
        println("Descuento calculado: $$descuento")
        println("Total a pagar: No corresponde")
    }

    println("Estado: $estadoPedido")
    println("Repartidor: $repartidorAsignado")

    println()
    println("--------------------------------")
    println("    GRACIAS POR SU PREFERENCIA  ")
    println("--------------------------------")
}

