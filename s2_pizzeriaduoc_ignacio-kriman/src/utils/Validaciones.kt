
package utils

fun validarTelefono(telefono: String?): String {
    val valor = telefono?.trim()

    return if (
        valor != null &&
        valor.matches(Regex("\\+?[0-9]{9,12}"))
    ) {
        valor
    } else {
        "(000) 000-0000"
    }
}

fun validarCorreo(correo: String?): String {
    val valor = correo?.trim()

    return if (
        valor != null &&
        valor.matches(
            Regex("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}")
        )
    ) {
        valor
    } else {
        "correo@invalido.com"
    }
}

fun leerCantidad(mensaje: String, maximo: Int): Int {
    var cantidad = 0

    do {
        print(mensaje)

        try {
            val entrada = readlnOrNull()?.trim()?.toIntOrNull()
                ?: throw IllegalArgumentException(
                    "Debe ingresar un numero entero."
                )

            require(entrada in 1..maximo) {
                "Ingrese una cantidad entre 1 y $maximo."
            }

            cantidad = entrada

        } catch (e: IllegalArgumentException) {
            println("Error: ${e.message}")
        }

    } while (cantidad == 0)

    return cantidad
}

fun preguntarSiNo(mensaje: String): Boolean {
    print(mensaje)
    return readlnOrNull()?.trim()?.lowercase() == "s"
}
