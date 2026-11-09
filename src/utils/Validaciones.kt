package utils

import java.time.LocalDate
import java.time.format.DateTimeParseException

private val PATRON_CORREO = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
private val PATRON_FECHA = Regex("^\\d{4}-\\d{2}-\\d{2}$")

fun leerNombre(): String {
    while (true) {
        print("Nombre del cliente: ")
        val nombre = readlnOrNull()?.trim().orEmpty()
        if (nombre.isNotBlank()) return nombre
        println("Error: el nombre es obligatorio.")
    }
}

fun leerCorreo(): String {
    while (true) {
        print("Correo (nombre@dominio.com): ")
        val correo = readlnOrNull()?.trim().orEmpty()
        if (PATRON_CORREO.matches(correo)) return correo.lowercase()
        println("Error: formato de correo incorrecto. Intente nuevamente.")
    }
}

// Acepta celulares chilenos: 9XXXXXXXX, 569XXXXXXXX o +569XXXXXXXX.
// Tambien permite espacios, guiones y parentesis en la entrada.
// Siempre retorna el formato +56 9 XXXX XXXX.
fun formatearTelefono(entrada: String): String? {
    val limpio = entrada.trim()
    if (!Regex("^\\+?[0-9()\\s-]+$").matches(limpio)) return null

    val digitos = limpio.filter { it.isDigit() }
    val numeroNacional = when {
        Regex("^9\\d{8}$").matches(digitos) -> digitos
        Regex("^569\\d{8}$").matches(digitos) -> digitos.drop(2)
        else -> return null
    }

    return "+56 9 ${numeroNacional.substring(1, 5)} ${numeroNacional.substring(5, 9)}"
}

fun leerTelefono(): String {
    while (true) {
        print("Telefono chileno (9XXXXXXXX o +569XXXXXXXX): ")
        val original = readlnOrNull()?.trim().orEmpty()
        val telefono = formatearTelefono(original)
        if (telefono != null) return telefono
        println("Error: ingrese un celular chileno valido, como 912345678 o +56912345678.")
    }
}

fun leerFecha(): LocalDate {
    while (true) {
        print("Fecha del pedido (AAAA-MM-DD): ")
        val texto = readlnOrNull()?.trim().orEmpty()
        try {
            require(PATRON_FECHA.matches(texto)) { "Formato incorrecto." }
            return LocalDate.parse(texto)
        } catch (e: DateTimeParseException) {
            println("Error: fecha inexistente. Intente nuevamente.")
        } catch (e: IllegalArgumentException) {
            println("Error: ${e.message} Intente nuevamente.")
        }
    }
}

fun leerCantidad(mensaje: String): Int {
    var cantidad: Int
    do {
        print(mensaje)
        val texto = readlnOrNull()?.trim()
        cantidad = texto?.toIntOrNull() ?: 0
        if (cantidad !in 1..100) println("Error: ingrese una cantidad de 1 a 100.")
    } while (cantidad !in 1..100)
    return cantidad
}

fun preguntarSiNo(mensaje: String): Boolean {
    print(mensaje)
    return readlnOrNull()?.trim()?.lowercase() == "s"
}
