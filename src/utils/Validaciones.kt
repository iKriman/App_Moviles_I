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

// Acepta doce digitos: 2 del pais y 10 del numero, con + opcional.
// Ejemplo: 569123456789 -> +56 (912) 345-6789.
fun formatearTelefono(entrada: String): String? {
    val digitos = entrada.filter { it.isDigit() }
    if (!Regex("^\\+?[0-9()\\s-]+$").matches(entrada.trim())) return null
    if (!Regex("^\\d{12}$").matches(digitos)) return null
    return "+${digitos.substring(0, 2)} (${digitos.substring(2, 5)}) " +
        "${digitos.substring(5, 8)}-${digitos.substring(8, 12)}"
}

fun leerTelefono(): String {
    while (true) {
        print("Telefono (ejemplo 569123456789): ")
        val original = readlnOrNull()?.trim().orEmpty()
        val telefono = formatearTelefono(original)
        if (telefono != null) return telefono
        println("Error: use 12 digitos, como 569123456789.")
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
