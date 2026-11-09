package modelo

import utils.PeriodoPromocion
import utils.Totales
import utils.calcularTotales
import java.time.LocalDate

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
    val fecha: LocalDate,
    val periodo: PeriodoPromocion,
    val notas: String? = null
) {
    init { require(productos.isNotEmpty()) { "El pedido no puede estar vacio." } }

    var estado: EstadoPedido = EstadoPedido.PENDIENTE
        private set

    var repartidor: Repartidor? = null
        private set

    fun totales(): Totales = calcularTotales(productos, fecha, periodo)

    fun cambiarEstado(nuevo: EstadoPedido) {
        val permitido = when (estado) {
            EstadoPedido.PENDIENTE -> nuevo == EstadoPedido.EN_PREPARACION
            EstadoPedido.EN_PREPARACION -> nuevo == EstadoPedido.LISTO
            EstadoPedido.LISTO -> nuevo == EstadoPedido.EN_CAMINO && repartidor != null
            EstadoPedido.EN_CAMINO -> nuevo == EstadoPedido.ENTREGADO
            EstadoPedido.ENTREGADO -> false
        }
        check(permitido) { "Transicion no permitida: ${estado.descripcion} -> ${nuevo.descripcion}" }
        estado = nuevo
        if (nuevo == EstadoPedido.ENTREGADO) repartidor?.liberar()
    }

    fun asignarRepartidor(elegido: Repartidor) {
        check(estado == EstadoPedido.LISTO) { "El pedido debe estar listo." }
        check(repartidor == null) { "El pedido ya tiene un repartidor." }
        elegido.reservar()
        repartidor = elegido
    }

    // Une dos borradores del mismo cliente y fecha, recalculando todos los importes.
    operator fun plus(otro: Pedido): Pedido {
        require(cliente == otro.cliente) { "No se pueden combinar clientes diferentes." }
        require(fecha == otro.fecha) { "Los pedidos deben tener la misma fecha." }
        require(estado == EstadoPedido.PENDIENTE && otro.estado == EstadoPedido.PENDIENTE) {
            "Solo se pueden combinar pedidos aun no aceptados."
        }
        return Pedido(maxOf(id, otro.id) + 1, cliente, productos + otro.productos, fecha, periodo,
            notas ?: otro.notas)
    }

    // Permite: val (cliente, productos, total) = pedido
    operator fun component1(): Cliente = cliente
    operator fun component2(): List<Producto> = productos
    operator fun component3(): Int = totales().total
}
