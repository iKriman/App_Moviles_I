package utils

import modelo.CategoriaProducto
import modelo.Producto
import modelo.esPromocionable
import java.text.NumberFormat
import java.time.LocalDate
import java.util.Locale

private const val DESCUENTO_MAS_DE_TRES = 10
private const val DESCUENTO_PROMOCIONAL = 10
private const val IMPUESTO = 19

data class Totales(
    val subtotal: Int,
    val descuentoCantidad: Int,
    val descuentoPromocion: Int,
    val impuesto: Int,
    val total: Int
)

data class PeriodoPromocion(val inicio: LocalDate, val fin: LocalDate) {
    init { require(!inicio.isAfter(fin)) { "Rango de promocion invalido." } }
    fun estaVigente(fecha: LocalDate): Boolean = fecha in inicio..fin
}

fun pesos(monto: Int): String =
    "$" + NumberFormat.getIntegerInstance(Locale.forLanguageTag("es-CL")).format(monto)

fun calcularTotales(
    productos: List<Producto>,
    fecha: LocalDate,
    periodo: PeriodoPromocion
): Totales {
    require(productos.isNotEmpty()) { "No se permite un pedido vacio." }
    val subtotal = productos.sumOf { it.precio }
    val cantidadPizzas = productos.count { it.categoria == CategoriaProducto.PIZZA }
    val descuentoCantidad = if (cantidadPizzas > 3) subtotal * DESCUENTO_MAS_DE_TRES / 100 else 0
    val subtotalElegible = productos.filter { it.esPromocionable() }.sumOf { it.precio }
    val descuentoPromocion = if (periodo.estaVigente(fecha)) {
        subtotalElegible * DESCUENTO_PROMOCIONAL / 100
    } else 0
    val base = subtotal - descuentoCantidad - descuentoPromocion
    val impuesto = base * IMPUESTO / 100
    return Totales(subtotal, descuentoCantidad, descuentoPromocion, impuesto, base + impuesto)
}
