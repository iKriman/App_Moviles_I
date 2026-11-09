package utils

import modelo.*

fun resumenPedido(pedido: Pedido): String {
    val t = pedido.totales()
    val cantidades = pedido.productos.groupingBy { it.nombre }.eachCount()
    return buildString {
        appendLine("====== RESUMEN DEL PEDIDO ======")
        appendLine("Numero: ${pedido.id} | Fecha: ${pedido.fecha}")
        appendLine("Cliente: ${pedido.cliente.nombre}")
        appendLine("Correo: ${pedido.cliente.correo}")
        appendLine("Telefono: ${pedido.cliente.telefono}")
        appendLine("Productos:")
        for ((nombre, cantidad) in cantidades) {
            val precio = pedido.productos.first { it.nombre == nombre }.precio
            appendLine("  $nombre x$cantidad - ${pesos(precio)} c/u")
        }
        appendLine("Subtotal: ${pesos(t.subtotal)}")
        appendLine("Descuento por cantidad: ${pesos(t.descuentoCantidad)}")
        appendLine("Descuento promocional: ${pesos(t.descuentoPromocion)}")
        appendLine("Impuesto (19%, demostrativo): ${pesos(t.impuesto)}")
        appendLine("Total: ${pesos(t.total)}")
        appendLine("Estado: ${pedido.estado.descripcion}")
        appendLine("Repartidor: ${pedido.repartidor?.nombre ?: "Sin asignar"}")
        appendLine("Notas: ${pedido.notas?.takeIf { it.isNotBlank() } ?: "Sin notas"}")
        append("===============================")
    }
}

fun reporteDiario(pedidos: Array<Pedido>): String {
    val lista = mutableListOf<String>()
    lista += "====== REPORTE DIARIO ======" 
    for (pedido in pedidos) {
        lista += "Pedido ${pedido.id} | ${pedido.cliente.nombre} | " +
            "${pedido.productos.size} productos | ${pesos(pedido.totales().total)} | " +
            pedido.estado.descripcion
    }
    val completos = pedidos.filter { it.estado == EstadoPedido.ENTREGADO }
    val pendientes = pedidos.filter { it.estado in EstadoPedido.entries.filter { e -> e != EstadoPedido.ENTREGADO } }
    lista += "Entregados: ${completos.size} | Pendientes: ${pendientes.size}"
    return lista.joinToString("\n")
}

// Reflection de Java: no exige agregar la dependencia externa kotlin-reflect.
fun describirClase(tipo: Class<*>): String {
    val propiedades = tipo.declaredFields.filterNot { it.isSynthetic }
        .map { it.name }.sorted()
    val metodos = tipo.declaredMethods.filterNot { it.isSynthetic }
        .map { it.name }.distinct().sorted()
    return "Clase ${tipo.simpleName}\nPropiedades: ${propiedades.joinToString()}\n" +
        "Metodos: ${metodos.joinToString()}\n" +
        "Anotaciones: ${tipo.declaredAnnotations.joinToString { it.annotationClass.simpleName.orEmpty() }}"
}
