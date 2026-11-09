package app

import modelo.*
import utils.*
import java.time.LocalDate

fun registrarCliente(registrados: MutableSet<Cliente>): Cliente {
    val candidato = Cliente(leerNombre(), leerTelefono(), leerCorreo())
    val existente = registrados.find { it == candidato }
    return if (existente != null) {
        println("Cliente existente detectado: se reutiliza su registro (sin duplicar).")
        existente
    } else {
        registrados.add(candidato)
        println("Nuevo cliente registrado.")
        candidato
    }
}

fun elegirProducto(catalogo: List<Producto>): Producto {
    while (true) {
        val numero = leerCantidad("Seleccione producto (1-${catalogo.size}): ")
        if (numero in 1..catalogo.size) return catalogo[numero - 1]
        println("Error: el producto debe estar entre 1 y ${catalogo.size}.")
    }
}

fun crearBorrador(
    id: Int,
    cliente: Cliente,
    fecha: LocalDate,
    periodo: PeriodoPromocion,
    catalogo: List<Producto>,
    notas: String? = null
): Pedido {
    val producto = elegirProducto(catalogo)
    val cantidad = leerCantidad("Cantidad de ese producto (1-100): ")
    return Pedido(id, cliente, List(cantidad) { producto }, fecha, periodo, notas)
}

fun aprobarInventario(pedido: Pedido, inventario: MutableMap<String, Int>): Boolean {
    var resultado = prepararPedido(pedido.productos, inventario)
    if (resultado.aprobado) {
        println("Inventario aprobado: se descontaron los ingredientes de todo el lote.")
        return true
    }

    println("El pedido no se acepta por falta de insumos:")
    for ((ingrediente, faltante) in resultado.faltantes) {
        println("- $ingrediente: faltan $faltante unidades")
    }
    println("Para reabastecer: revisar faltantes, adquirir insumos y registrar cantidades.")

    if (!preguntarSiNo("Reabastecer los faltantes y reintentar ESTE pedido? (s/n): ")) {
        println("Pedido descartado; no aparece en el reporte de aceptados.")
        return false
    }

    try {
        for ((ingrediente, faltante) in resultado.faltantes) {
            reabastecer(inventario, ingrediente, faltante)
            println("Reabastecido $ingrediente con $faltante unidades.")
        }
    } catch (e: IllegalArgumentException) {
        println("Error al reabastecer: ${e.message}")
        return false
    }

    // Se reutilizan pedido, cliente, fecha y productos originales.
    resultado = prepararPedido(pedido.productos, inventario)
    println(if (resultado.aprobado) "Reintento exitoso." else "Sigue faltando inventario.")
    return resultado.aprobado
}

fun avanzarEntrega(pedido: Pedido, repartidores: List<Repartidor>) {
    if (!preguntarSiNo("Marcar pedido en preparacion como listo? (s/n): ")) return
    pedido.cambiarEstado(EstadoPedido.LISTO)

    val elegido = repartidores.sortedBy { it.distanciaKm }.find { it.disponible }
    if (elegido == null) {
        println("No hay repartidores disponibles. Pedido permanece LISTO.")
        return
    }

    pedido.asignarRepartidor(elegido)
    println("Asignado: ${elegido.nombre} (${elegido.distanciaKm} km).")
    pedido.cambiarEstado(EstadoPedido.EN_CAMINO)

    if (preguntarSiNo("Confirmar entrega del pedido? (s/n): ")) {
        println(elegido.entregarPedido(pedido))
    }
}

fun mostrarDemostraciones(cliente: Cliente, pedido: Pedido, catalogo: List<Producto>) {
    println("\n===== DEMOSTRACIONES SEMANA 3 =====")

    val (nombre, correo, telefono) = cliente
    println("Cliente desestructurado: $nombre | $correo | $telefono")

    val (clientePedido, productosPedido, totalPedido) = pedido
    println("Pedido desestructurado: ${clientePedido.nombre} | " +
        "${productosPedido.size} productos | ${pesos(totalPedido)}")

    val clienteDuplicado = Cliente(cliente.nombre, cliente.telefono, cliente.correo.uppercase())
    val conjuntoClientes = setOf(cliente, clienteDuplicado)
    println("Igualdad clientes (==): ${cliente == clienteDuplicado} | " +
        "Clientes unicos en Set: ${conjuntoClientes.size}")

    val original = catalogo.first()
    val productoDuplicado = Producto(original.nombre, original.categoria, original.precio + 500)
    val conjuntoProductos = setOf(original, productoDuplicado)
    println("Igualdad productos (==): ${original == productoDuplicado} | " +
        "Productos unicos en Set: ${conjuntoProductos.size}")

    println("Productos promocionables identificados por @Promocionable:")
    for (producto in catalogo) {
        if (producto.esPromocionable()) println("- ${producto.nombre}")
    }

    println("\nReflection sobre cinco clases:")
    for (tipo in listOf(Pedido::class.java, Cliente::class.java, Producto::class.java, PizzaEspecial::class.java, ComboEspecial::class.java)) {
        println(describirClase(tipo))
    }

    val usuarios: List<Usuario> = listOf(cliente, Repartidor("Ana", "+56 9 1234 5678", 2.0))
    println("\nPolimorfismo mediante List<Usuario>:")
    for (usuario in usuarios) println(usuario.presentar())
}
