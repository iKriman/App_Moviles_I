# Pizzería DUOC — Semana 3 (evaluación sumativa)

Proyecto Kotlin/JVM de consola que da continuidad a la pizzería desarrollada en las semanas 1 y 2.

## Ejecución en IntelliJ IDEA

1. Descomprimir el ZIP y abrir la carpeta `PizzeriaDuocSemana3` en IntelliJ IDEA.
2. Si IntelliJ detecta `build.gradle.kts`, importar el proyecto Gradle y disponer de JDK 21.
3. Abrir `src/Main.kt` y ejecutar `MainKt` con el triángulo verde.
4. Alternativamente, copiar la carpeta `src` a un proyecto Kotlin/JVM ya creado.
5. Puedes ejecutar `PruebasKt` directamente para correr las pruebas automáticas sin ingresar datos.

## Ejecución con Kotlin CLI (si está instalado)

```bash
kotlinc src -include-runtime -d Pizzeria.jar
java -cp Pizzeria.jar MainKt
```

## Requisitos demostrados

- Regex de correo (reintento) y normalización de teléfono al formato `+XX (XXX) XXX-XXXX`.
- Rango `1..100` de cantidades; validación de fechas ISO `AAAA-MM-DD` y promoción dentro del mes corriente.
- Anotación `@Promocionable` en `PizzaEspecial` y `ComboEspecial`, comprobada por reflexión en tiempo de ejecución.
- Reflection de cinco clases con miembros y anotaciones.
- `Pedido.plus` combina dos borradores del mismo cliente y fecha, sin descontar stock dos veces.
- `==` utiliza `equals`/`hashCode` coherentes en `Cliente` y `Producto`; duplicados excluidos mediante `Set`.
- Desestructuración de `Cliente` y `Pedido`.
- Estados encapsulados y transiciones validadas; repartidores con disponibilidad controlada.
- Inventario comprobado para el lote completo; reabastecimiento y reintento del mismo pedido sin reingresar datos.
- Arreglo `Array<Pedido>` con los pedidos **aceptados** y reporte de entregados/pendientes.
- Polimorfismo con `List<Usuario>`; mensajes generados fuera de las clases de dominio.

## Ejemplos para probar

**Cliente**: Ana / **Teléfono**: `569123456789` / **Correo**: `ana@example.com` / **Fecha**: fecha actual en `AAAA-MM-DD`.

Pulsa Enter al preguntar por notas si quieres dejar `Sin notas`.

- Seleccionar la pizza especial x2 y la Margarita x2: combina dos borradores, aplica descuento del 10% por más de 3 pizzas, promoción si corresponde, e impuesto demostrativo del 19% sobre el neto.
- Seleccionar la pizza especial x10 y el combo x5: supera el stock inicial, permite reabastecer y reintenta el mismo pedido.
- Ingresar `abc@` como correo, una fecha inexistente o cantidad 101: se solicita reingreso.
- Ingresar fecha de un mes distinto al actual: no se aplica descuento promocional por fecha.
- Tras confirmar el pedido, responder `s` a "listo" y "entrega" para demostrar cambios de estado y liberar repartidor.


El patrón telefónico utilizado responde al formato solicitado en la pauta (`+XX (XXX) XXX-XXXX`), que es un formato de demostración y no reproduce necesariamente todos los números de Chile.

