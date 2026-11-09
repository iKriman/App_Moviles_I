package utils

import modelo.Producto

data class ResultadoInventario(val aprobado: Boolean, val faltantes: Map<String, Int>)

fun prepararPedido(
    productos: List<Producto>,
    inventario: MutableMap<String, Int>
): ResultadoInventario {
    val necesarios = mutableMapOf<String, Int>()
    for (producto in productos) {
        for ((ingrediente, cantidad) in producto.ingredientes) {
            necesarios[ingrediente] = necesarios.getOrDefault(ingrediente, 0) + cantidad
        }
    }
    val faltantes = necesarios.mapNotNull { (ingrediente, necesario) ->
        val falta = necesario - inventario.getOrDefault(ingrediente, 0)
        if (falta > 0) ingrediente to falta else null
    }.toMap()

    return if (faltantes.isEmpty()) {
        for ((ingrediente, cantidad) in necesarios) {
            inventario[ingrediente] = inventario.getOrDefault(ingrediente, 0) - cantidad
        }
        ResultadoInventario(true, emptyMap())
    } else {
        ResultadoInventario(false, faltantes)
    }
}

fun reabastecer(
    inventario: MutableMap<String, Int>,
    ingrediente: String,
    cantidad: Int
) {
    require(ingrediente in inventario) { "Ingrediente desconocido: $ingrediente" }
    require(cantidad in 1..10000) { "Reabastecimiento fuera de rango (1 a 10000)." }
    inventario[ingrediente] = Math.addExact(inventario.getValue(ingrediente), cantidad)
}
