package modelo

class Repartidor(nombre: String, telefono: String, val distanciaKm: Double) :
    Usuario(nombre, telefono) {

    var disponible: Boolean = true
        private set

    override fun presentar(): String =
        "Repartidor: $nombre | Distancia: $distanciaKm km | Disponible: $disponible"

    fun reservar() {
        check(disponible) { "El repartidor $nombre ya esta ocupado." }
        disponible = false
    }

    internal fun liberar() {
        disponible = true
    }

    fun entregarPedido(pedido: Pedido): String {
        check(pedido.repartidor === this) { "El repartidor no esta asignado al pedido." }
        check(pedido.estado == EstadoPedido.EN_CAMINO) {
            "Solo se puede entregar un pedido en camino."
        }
        pedido.cambiarEstado(EstadoPedido.ENTREGADO)
        return "Pedido ${pedido.id} entregado por $nombre."
    }
}
