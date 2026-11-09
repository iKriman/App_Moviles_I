import modelo.*
import utils.*
import java.time.LocalDate

// Ejecutar PruebasKt para comprobar reglas sin ingresar datos por consola.
fun main() {
    val fecha = LocalDate.of(2026, 11, 6)
    val periodo = PeriodoPromocion(fecha.minusDays(1), fecha.plusDays(1))
    val cliente = Cliente("Ana", "+56 9 1234 5678", "ana@example.com")
    val clon = Cliente("ANA", "999999999", "ANA@example.com")
    check(cliente == clon && setOf(cliente, clon).size == 1)

    val especial = PizzaEspecial("Pepperoni", 8000, mapOf("Masa" to 1, "Queso" to 2))
    val duplicado = Producto("PEPPERONI", CategoriaProducto.PIZZA, 9000)
    check(especial == duplicado && setOf(especial, duplicado).size == 1)
    check(especial.esPromocionable())
    check(!PizzaClasica("Margarita", 7000, mapOf("Masa" to 1)).esPromocionable())
    // Un numero de celular chileno puede ingresarse local o con +56.
    val celularEsperado = "+56 9 1234 5678"
    val formatosValidos = listOf(
        "912345678", "+56912345678", "56912345678",
        "9 1234 5678", "+56 9 1234 5678", "+56 (9) 1234-5678",
        "9-1234-5678"
    )
    formatosValidos.forEach { entrada ->
        check(formatearTelefono(entrada) == celularEsperado) {
            "Formato chileno rechazado: $entrada"
        }
    }
    val formatosInvalidos = listOf(
        "", "12345678", "812345678", "569123456789",
        "abc56912345678", "++56912345678", "+55 9 1234 5678"
    )
    formatosInvalidos.forEach { entrada ->
        check(formatearTelefono(entrada) == null) {
            "Numero invalido aceptado: $entrada"
        }
    }
    check(periodo.estaVigente(fecha) && !periodo.estaVigente(fecha.plusDays(5)))

    val p1 = Pedido(1, cliente, List(2) { especial }, fecha, periodo)
    val p2 = Pedido(2, cliente, List(2) { especial }, fecha, periodo)
    val combinado = p1 + p2
    check(combinado.productos.size == 4)
    check(combinado.totales().descuentoCantidad == 3200)
    check(combinado.totales().descuentoPromocion == 3200)
    val (_, _, total) = combinado
    check(total == combinado.totales().total)

    val stock = mutableMapOf("Masa" to 2, "Queso" to 4)
    val rechazado = prepararPedido(combinado.productos, stock)
    check(!rechazado.aprobado && stock["Masa"] == 2 && stock["Queso"] == 4)
    rechazado.faltantes.forEach { (nombre, cantidad) -> reabastecer(stock, nombre, cantidad) }
    check(prepararPedido(combinado.productos, stock).aprobado)
    check(stock.values.all { it == 0 })

    check(runCatching { combinado.cambiarEstado(EstadoPedido.ENTREGADO) }.isFailure)
    combinado.cambiarEstado(EstadoPedido.EN_PREPARACION)
    combinado.cambiarEstado(EstadoPedido.LISTO)
    val repartidor = Repartidor("Carlos", "+56 9 1234 5678", 1.5)
    combinado.asignarRepartidor(repartidor)
    check(!repartidor.disponible)
    combinado.cambiarEstado(EstadoPedido.EN_CAMINO)
    check(repartidor.entregarPedido(combinado).contains("entregado"))
    check(combinado.estado == EstadoPedido.ENTREGADO && repartidor.disponible)

    println("PRUEBAS AUTOMATICAS: todas aprobadas.")
    println("Validaciones, promociones, igualdad, desestructuracion, sobrecarga, " +
        "inventario, reabastecimiento, estados y repartidores: OK")
}
