
package utils

import modelo.Producto

fun prepararPedido(
    productos: List<Producto>,
    inventario: MutableMap<String, Int>
): Boolean {

    val necesarios = mutableMapOf<String, Int>()

    // calcular ingredientes necesarios
    for (producto in productos) {
        for ((ingrediente, cantidad) in producto.ingredientes) {
            necesarios[ingrediente] =
                necesarios.getOrDefault(ingrediente, 0) + cantidad
        }
    }

    // verificar ingredientes faltantes
    val faltantes = necesarios.filter { (ingrediente, cantidad) ->
        inventario.getOrDefault(ingrediente, 0) < cantidad
    }

    return when {
        faltantes.isEmpty() -> {
            for ((ingrediente, cantidad) in necesarios) {
                inventario[ingrediente] =
                    inventario.getOrDefault(ingrediente, 0) - cantidad
            }

            println("Stock suficiente. Pedido confirmado.")
            true
        }

        else -> {
            println("No hay suficientes ingredientes.")

            for ((ingrediente, cantidad) in faltantes) {
                val disponible = inventario.getOrDefault(ingrediente, 0)

                println(
                    "$ingrediente: disponibles $disponible, " +
                            "necesarios $cantidad"
                )

                println(
                    "Faltan ${cantidad - disponible} unidades de $ingrediente."
                )
            }

            println()
            println("Pasos para reabastecer:")
            println("1. Revisar los ingredientes faltantes.")
            println("2. Solicitar las cantidades al proveedor.")
            println("3. Registrar el nuevo stock.")
            println("4. Volver a procesar el pedido.")

            false
        }
    }
}

fun reabastecerInventario(
    inventario: MutableMap<String, Int>,
    ingrediente: String,
    cantidad: Int
) {
    require(ingrediente in inventario) {
        "El ingrediente no existe en inventario."
    }

    require(cantidad > 0) {
        "La cantidad debe ser mayor a cero."
    }

    val stockActual = inventario.getValue(ingrediente)

    require(cantidad <= 1000) {
        "No se pueden ingresar mas de 1000 unidades a la vez."
    }

    inventario[ingrediente] = stockActual + cantidad

    println("Inventario actualizado correctamente.")
}
