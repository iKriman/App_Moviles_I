
package utils

import modelo.Producto
import java.util.Locale

data class Totales(
    val subtotal: Int,
    val descuento: Int,
    val impuesto: Int,
    val total: Int
)

fun formatearPesos(monto: Int): String {
    return "$" + "%,d".format(
        Locale.forLanguageTag("es-CL"),
        monto
    )
}

fun calcularTotales(productos: List<Producto>): Totales {
    require(productos.isNotEmpty()) {
        "No se puede calcular un pedido vacio."
    }

    val subtotal = productos.sumOf { it.precio }

    val descuento = if (productos.size > 3) {
        subtotal * 10 / 100
    } else {
        0
    }

    val baseImponible = subtotal - descuento

    // impuesto de ejemplo del 19% sobre precios base
    val impuesto = baseImponible * 19 / 100

    val total = baseImponible + impuesto

    return Totales(subtotal, descuento, impuesto, total)
}
