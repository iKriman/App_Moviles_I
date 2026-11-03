
package modelo

import utils.calcularTotales
import utils.formatearPesos

enum class EstadoPedido(val descripcion: String) {
    PENDIENTE("Pendiente"),
    EN_PREPARACION("En preparacion"),
    LISTO("Listo"),
    EN_CAMINO("En camino"),
    ENTREGADO("Entregado")
}

class Pedido(
    val id: Int,
    val cliente: Cliente,
    val productos: List<Producto>,
    val notas: String? = null
) {
    init {
        require(productos.isNotEmpty()) {
            "El pedido debe contener productos."
        }
    }

    var estado: EstadoPedido = EstadoPedido.PENDIENTE
        private set

    var repartidor: Repartidor? = null

    fun calcularTotal(): Int {
        return calcularTotales(productos).total
    }

    fun cambiarEstado(nuevoEstado: EstadoPedido) {
        estado = nuevoEstado
    }

    fun mostrarResumen() {
        val totales = calcularTotales(productos)

        println()
        println("--------------------------------")
        println("       RESUMEN DEL PEDIDO       ")
        println("--------------------------------")

        println("Numero de pedido: $id")

        println()
        println("--- DATOS DEL CLIENTE ---")
        println(cliente.mostrarDatos())

        println()
        println("--- PRODUCTOS ---")

        val agrupados = productos.groupBy { it.nombre }

        for ((nombre, pizzas) in agrupados) {
            val precio = pizzas.first().precio
            println(
                "$nombre | Cantidad: ${pizzas.size} | " +
                        "Precio unitario: ${formatearPesos(precio)}"
            )
        }

        println()
        println("--- TOTALES ---")
        println("Subtotal: ${formatearPesos(totales.subtotal)}")
        println("Descuento: ${formatearPesos(totales.descuento)}")
        println("Impuestos: ${formatearPesos(totales.impuesto)}")

        if (estado == EstadoPedido.PENDIENTE) {
            println("Total estimado: ${formatearPesos(totales.total)}")
            println("Total a pagar: No corresponde, pedido pendiente")
        } else {
            println("Total final: ${formatearPesos(calcularTotal())}")
        }

        println()
        println("Estado: ${estado.descripcion}")
        println("Repartidor: ${repartidor?.nombre ?: "Sin asignar"}")
        println("Notas: ${notas?.takeIf { it.isNotBlank() } ?: "Sin notas"}")

        println("--------------------------------")
    }
}
