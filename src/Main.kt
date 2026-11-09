import app.*
import modelo.*
import utils.*
import java.time.LocalDate

fun main() {
    println("=========================================")
    println("             PIZZERIA DUOC               ")
    println("=========================================")

    val hoy = LocalDate.now()
    val promocion = PeriodoPromocion(
        hoy.withDayOfMonth(1),
        hoy.withDayOfMonth(hoy.lengthOfMonth())
    )
    println("Periodo promocional (ejemplo): ${promocion.inicio} al ${promocion.fin}")

    val catalogo: List<Producto> = listOf(
        PizzaEspecial("Pepperoni especial", 8000, mapOf("Masa" to 1, "Queso" to 2, "Pepperoni" to 2)),
        PizzaClasica("Margarita", 7000, mapOf("Masa" to 1, "Queso" to 2, "Tomate" to 2)),
        ComboEspecial("Combo pizza y bebida", 10000, mapOf("Masa" to 1, "Queso" to 2, "Bebida" to 1))
    )
    val inventario = mutableMapOf(
        "Masa" to 12, "Queso" to 24, "Pepperoni" to 12,
        "Tomate" to 12, "Bebida" to 6
    )
    val repartidores = listOf(
        Repartidor("Carlos", "+56 (912) 345-6789", 2.0),
        Repartidor("Sofia", "+56 (923) 456-7890", 4.0)
    )
    val clientes = mutableSetOf<Cliente>()
    var pedidosAceptados: Array<Pedido> = emptyArray()
    var siguienteId = 1

    do {
        println("\n--- NUEVO REGISTRO ---")
        val cliente = registrarCliente(clientes)
        val fecha = leerFecha()
        println("Promocion en fecha ingresada: ${if (promocion.estaVigente(fecha)) "SI" else "NO"}")

        println("\n--- CATALOGO ---")
        for ((indice, producto) in catalogo.withIndex()) {
            println("${indice + 1}. ${producto.nombre} - ${pesos(producto.precio)} " +
                if (producto.esPromocionable()) "(promocionable)" else "")
        }

        print("Notas opcionales del pedido (Enter para omitir): ")
        val notas = readlnOrNull()?.trim()?.takeIf { it.isNotBlank() }

        println("\nPrimer borrador:")
        val primero = crearBorrador(siguienteId++, cliente, fecha, promocion, catalogo, notas)
        println("Segundo borrador:")
        val segundo = crearBorrador(siguienteId, cliente, fecha, promocion, catalogo, notas)
        val combinado = primero + segundo
        siguienteId = combinado.id + 1
        println("Combinacion mediante +: ${primero.id} + ${segundo.id} = ${combinado.id}")
        println("Total recalculado: ${pesos(combinado.totales().total)}")

        if (aprobarInventario(combinado, inventario)) {
            combinado.cambiarEstado(EstadoPedido.EN_PREPARACION)
            // Solo se registran pedidos que realmente pasaron el control de stock.
            pedidosAceptados += combinado
            avanzarEntrega(combinado, repartidores)
            println(resumenPedido(combinado))
            combinado.cliente.correo.takeIf { it.contains("@") }?.let { correo ->
                println("Notificacion simulada a $correo: pedido ${combinado.id} ${combinado.estado.descripcion}.")
            }
            mostrarDemostraciones(cliente, combinado, catalogo)
        }

    } while (preguntarSiNo("\nRegistrar otro pedido? (s/n): "))

    println("\n" + reporteDiario(pedidosAceptados))
    println("\nInventario al cierre:")
    for ((ingrediente, cantidad) in inventario) {
        println("$ingrediente: $cantidad")
    }
    println("Gracias por su preferencia.")
}
