package modelo

open class Usuario(
    val nombre: String,
    val telefono: String
) {
    open fun mostrarDatos(): String {
        return "Nombre: $nombre\nTelefono: $telefono"
    }
}
