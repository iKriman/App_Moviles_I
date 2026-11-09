package modelo

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Promocionable

@Promocionable
class PizzaEspecial(nombre: String, precio: Int, ingredientes: Map<String, Int>) :
    Producto(nombre, CategoriaProducto.PIZZA, precio, ingredientes)

@Promocionable
class ComboEspecial(nombre: String, precio: Int, ingredientes: Map<String, Int>) :
    Producto(nombre, CategoriaProducto.COMBO, precio, ingredientes)

class PizzaClasica(nombre: String, precio: Int, ingredientes: Map<String, Int>) :
    Producto(nombre, CategoriaProducto.PIZZA, precio, ingredientes)

fun Producto.esPromocionable(): Boolean =
    javaClass.isAnnotationPresent(Promocionable::class.java)
