package modelo

open class Usuario(val nombre: String, val telefono: String) {
    open fun presentar(): String = "Usuario: $nombre | Telefono: $telefono"
}
