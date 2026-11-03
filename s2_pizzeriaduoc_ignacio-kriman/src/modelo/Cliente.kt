package modelo

class Cliente(
    nombre: String,
    telefono: String,
    val correo: String,
    val direccion: String
) : Usuario(nombre, telefono) {

    override fun mostrarDatos(): String {
        return """
            Nombre: $nombre
            Telefono: $telefono
            Correo: $correo
            Direccion: $direccion
        """.trimIndent()
    }
}
