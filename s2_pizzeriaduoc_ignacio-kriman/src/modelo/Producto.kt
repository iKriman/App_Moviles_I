
package modelo

import utils.formatearPesos

data class Producto(
    val nombre: String,
    val precio: Int,
    val ingredientes: Map<String, Int>
) {
    init {
        require(nombre.isNotBlank()) {
            "El producto debe tener un nombre."
        }

        require(precio > 0) {
            "El precio debe ser mayor a cero."
        }

        require(
            ingredientes.isNotEmpty() &&
                    ingredientes.values.all { it > 0 }
        ) {
            "Los ingredientes deben tener cantidades validas."
        }
    }

    fun detallarProducto(): String {
        val detalle = ingredientes.entries.joinToString {
            "${it.key}: ${it.value}"
        }

        return "$nombre - ${formatearPesos(precio)} | Ingredientes: $detalle"
    }
}
