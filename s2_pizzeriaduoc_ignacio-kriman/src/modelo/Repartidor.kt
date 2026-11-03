package modelo

class Repartidor(
    nombre: String,
    telefono: String,
    val distanciaKm: Double,
    var disponible: Boolean = true
) : Usuario(nombre, telefono) {

    fun entregarPedido(pedido: Pedido) {
        if (
            pedido.estado == EstadoPedido.EN_CAMINO &&
            pedido.repartidor === this
        ) {
            pedido.cambiarEstado(EstadoPedido.ENTREGADO)
            disponible = true
            println("Pedido ${pedido.id} entregado por $nombre")
        } else {
            println("El pedido no esta en camino.")
        }
    }
}

fun buscarRepartidorMasCercano(
    repartidores: List<Repartidor>
): Repartidor? {
    return repartidores
        .sortedBy { it.distanciaKm }
        .find { it.disponible }
}
