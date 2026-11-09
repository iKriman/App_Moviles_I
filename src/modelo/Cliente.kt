package modelo

class Cliente(
    nombre: String,
    telefono: String,
    val correo: String
) : Usuario(nombre, telefono) {

    init {
        require(nombre.isNotBlank()) { "El nombre no puede estar vacio." }
        require(correo.isNotBlank()) { "El correo no puede estar vacio." }
    }

    override fun presentar(): String = "Cliente: $nombre | Correo: $correo | Telefono: $telefono"

    // Permite: val (nombre, correo, telefono) = cliente
    operator fun component1(): String = nombre
    operator fun component2(): String = correo
    operator fun component3(): String = telefono

    // La identidad del cliente se define por nombre y correo.
    override fun equals(other: Any?): Boolean =
        this === other || (other is Cliente &&
            nombre.equals(other.nombre, ignoreCase = true) &&
            correo.equals(other.correo, ignoreCase = true))

    override fun hashCode(): Int =
        31 * nombre.lowercase().hashCode() + correo.lowercase().hashCode()
}
