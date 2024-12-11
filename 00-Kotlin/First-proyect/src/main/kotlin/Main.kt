package org.example

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main() {
    val numero = 2

    when (numero) {
        1 -> println("El número es 1")
        2 -> println("El número es 2")
        3 -> println("El número es 3")
        else -> println("Número no reconocido")
    }

    val edad = 20

    // Operador ternario simulado en Kotlin
    val mensaje = if (edad >= 18) "Eres mayor de edad" else "Eres menor de edad"

    println(mensaje)

    mostrarInformacion("Juan", 25, "Madrid")

    // Llamada con el parámetro opcional (edad) y el parámetro nulo (ciudad)
    mostrarInformacion("Ana", ciudad = null)

    // Llamada con solo el parámetro requerido (nombre), usando los valores por defecto para los otros
    mostrarInformacion("Carlos")

    val sumaA = Suma(1, 1)
    val sumaB = Suma(null, 1)
    val sumaC = Suma(1, null)
    val sumaD = Suma(null, null)

    // Llamada a la función sumar() desde el main
    println("Resultado de la suma A: ${sumaA.sumar()}")
    println("Resultado de la suma B: ${sumaB.sumar()}")
    println("Resultado de la suma C: ${sumaC.sumar()}")
    println("Resultado de la suma D: ${sumaD.sumar()}")

    println(Suma.pi)
    println(Suma.elevarAlCuadrado(2))
    println(Suma.historial)

    val arrayEstatico: Array<Int> = arrayOf(1, 2, 3)

    arrayEstatico.forEach { println(it) }

    val arrayDinamico: ArrayList<Int> = arrayListOf<Int>(
        1, 2, 3, 4, 5
    )

    val arrayUsandoFilter = arrayDinamico.filter { it % 2 == 0 }

    val arrayUsandoMap = arrayDinamico.map { it * 2 }

    val hayMayoresA3 = arrayDinamico.any { it > 3 }

    val todosSonMayoresA3 = arrayDinamico.any { it > 3 }

    val sumaTotal = arrayDinamico.reduce { acumulador, elemento ->
        acumulador + elemento
    }



}

fun saludo(nombre: String): String {
    return "Hola, $nombre!"
}

fun despedida(nombre: String): Unit {
    println("Hola, $nombre!")
}

fun mostrarInformacion(
    nombre: String,
    edad: Int = 18,
    ciudad: String? = null
) {
    println("Nombre: $nombre")
    println("Edad: $edad")
    if (ciudad != null) {
        println("Ciudad: $ciudad")
    } else {
        println("Ciudad: No proporcionada")
    }
}

