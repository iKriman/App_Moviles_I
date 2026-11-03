
import modelo.*
import utils.*

fun registrarCliente(): Cliente {
    println()
    println("--- REGISTRO DE CLIENTE ---")

    print("Nombre: ")
    val nombre = readlnOrNull()?.trim()
        ?.takeIf { it.isNotEmpty() } ?: "Sin Nombre"

    print("Telefono: ")
    val telefono = validarTelefono(readlnOrNull())

    print("Correo: ")
    val correo = validarCorreo(readlnOrNull())

    print("Direccion: ")
    val direccion = readlnOrNull()?.trim()
        ?.takeIf { it.isNotEmpty() } ?: "Sin Direccion"

    return Cliente(nombre, telefono, correo, direccion)
}

fun main() {
    println("--------------------------------")
    println("       PIZZERIA DUOC UC         ")
    println("--------------------------------")

    // catalogo de pizzas
    val catalogo = listOf(
        Producto(
            "Pepperoni",
            8000,
            mapOf("Masa" to 1, "Queso" to 2, "Pepperoni" to 2)
        ),
        Producto(
            "Margarita",
            7000,
            mapOf("Masa" to 1, "Queso" to 2, "Tomate" to 2)
        ),
        Producto(
            "Jamon y Queso",
            9000,
            mapOf("Masa" to 1, "Queso" to 2, "Jamon" to 2)
        )
    )

    // inventario inicial
    val inventario = mutableMapOf(
        "Masa" to 12,
        "Queso" to 24,
        "Pepperoni" to 12,
        "Tomate" to 12,
        "Jamon" to 12
    )

    // repartidores disponibles
    val repartidores = listOf(
        Repartidor("Carlos", "912345678", 2.0),
        Repartidor("Pedro", "923456789", 5.0),
        Repartidor("Juan", "934567890", 3.0)
    )

    // pedidos del dia
    val pedidosDelDia = mutableListOf<Pedido>()

    var continuar: Boolean

    do {
        val cliente = registrarCliente()

        println()
        println("--- CATALOGO DE PIZZAS ---")

        for ((indice, pizza) in catalogo.withIndex()) {
            println("${indice + 1}. ${pizza.detallarProducto()}")
        }

        val opcion = leerCantidad(
            "Seleccione una pizza (1-${catalogo.size}): ",
            catalogo.size
        )

        val cantidad = leerCantidad(
            "Cantidad de pizzas (1-20): ",
            20
        )

        print("Notas del pedido (opcional): ")
        val notas = readlnOrNull()?.trim()
            ?.takeIf { it.isNotEmpty() }

        val pizzaSeleccionada = catalogo[opcion - 1]
        val productos = List(cantidad) { pizzaSeleccionada }

        val pedido = Pedido(
            pedidosDelDia.size + 1,
            cliente,
            productos,
            notas
        )

        println()
        println("--- VERIFICACION DE INVENTARIO ---")

        if (prepararPedido(productos, inventario)) {
            pedido.cambiarEstado(EstadoPedido.EN_PREPARACION)

            if (preguntarSiNo("Marcar pedido como listo? (s/n): ")) {
                pedido.cambiarEstado(EstadoPedido.LISTO)

                val repartidor = buscarRepartidorMasCercano(repartidores)

                if (repartidor != null) {
                    pedido.repartidor = repartidor
                    repartidor.disponible = false
                    pedido.cambiarEstado(EstadoPedido.EN_CAMINO)

                    println(
                        "Repartidor asignado: ${repartidor.nombre} " +
                                "(${repartidor.distanciaKm} km)"
                    )

                    if (preguntarSiNo("Confirmar entrega? (s/n): ")) {
                        repartidor.entregarPedido(pedido)
                    }
                } else {
                    println("No hay repartidores disponibles.")
                }
            }

        } else {
            if (preguntarSiNo("Desea reabastecer inventario? (s/n): ")) {
                println("Ingredientes disponibles: ${inventario.keys.joinToString()}")

                print("Nombre del ingrediente: ")
                val ingrediente = readlnOrNull()?.trim().orEmpty()

                print("Cantidad a agregar: ")
                val unidades = readlnOrNull()?.trim()?.toIntOrNull() ?: -1

                try {
                    reabastecerInventario(
                        inventario,
                        ingrediente,
                        unidades
                    )
                } catch (e: IllegalArgumentException) {
                    println("Error: ${e.message}")
                }

                println("Puede volver a ingresar el pedido.")
            }
        }

        pedidosDelDia.add(pedido)
        pedido.mostrarResumen()

        // notificacion simulada
        cliente.correo
            .takeIf { it != "correo@invalido.com" }
            ?.let { correo ->
                println(
                    "Notificacion simulada a $correo: " +
                            "Pedido ${pedido.id} - ${pedido.estado.descripcion}"
                )
            }

        continuar = preguntarSiNo(
            "\nDesea registrar otro pedido? (s/n): "
        )

    } while (continuar)

    println()
    println("--------------------------------")
    println("            REPORTE             ")
    println("--------------------------------")

    val arregloPedidos = pedidosDelDia.toTypedArray()

    for (pedido in arregloPedidos) {
        val pizzas = pedido.productos
            .groupingBy { it.nombre }
            .eachCount()
            .entries
            .joinToString { "${it.key} x${it.value}" }

        val total = if (pedido.estado == EstadoPedido.PENDIENTE) {
            "No confirmado"
        } else {
            formatearPesos(pedido.calcularTotal())
        }

        println(
            "Pedido ${pedido.id} | " +
                    "Cliente: ${pedido.cliente.nombre} | " +
                    "Pizzas: $pizzas | " +
                    "Total: $total | " +
                    "Estado: ${pedido.estado.descripcion}"
        )
    }

    val completados = arregloPedidos.filter {
        it.estado == EstadoPedido.ENTREGADO
    }

    val estadosPendientes = setOf(
        EstadoPedido.PENDIENTE,
        EstadoPedido.EN_PREPARACION,
        EstadoPedido.LISTO,
        EstadoPedido.EN_CAMINO
    )

    val pendientes = arregloPedidos.filter {
        it.estado in estadosPendientes
    }

    println()
    println("Pedidos completados: ${completados.size}")
    println("Pedidos pendientes: ${pendientes.size}")

    println()
    println("--- INVENTARIO FINAL ---")

    for ((ingrediente, cantidad) in inventario) {
        println("$ingrediente: $cantidad unidades")
    }

    println()
    println("--------------------------------")
    println("    GRACIAS POR SU PREFERENCIA  ")
    println("--------------------------------")
}
