package modelo

enum class CategoriaProducto { PIZZA, BEBIDA, COMBO }

open class Producto(
    val nombre: String,
    val categoria: CategoriaProducto,
    val precio: Int,
    val ingredientes: Map<String, Int> = emptyMap()
) {
    init {
        require(nombre.isNotBlank()) { "El nombre del producto es obligatorio." }
        require(precio > 0) { "El precio debe ser mayor que cero." }
        require(ingredientes.values.all { it > 0 }) {
            "Los ingredientes deben tener cantidades positivas."
        }
    }

    fun detallarProducto(): String = "$nombre (${categoria.name}) - $precio pesos"

    // Kotlin traduce a == b a a.equals(b).
    override fun equals(other: Any?): Boolean =
        this === other || (other is Producto &&
            nombre.equals(other.nombre, ignoreCase = true) &&
            categoria == other.categoria)

    override fun hashCode(): Int =
        31 * nombre.lowercase().hashCode() + categoria.hashCode()
}
