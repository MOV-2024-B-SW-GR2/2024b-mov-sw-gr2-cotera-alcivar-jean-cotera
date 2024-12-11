package org.example

abstract class Numeros(
    protected val numeroUno: Int,
    protected val numeroDos: Int,
    parametroNoUsadoNoPropiedadDeLaClase: Int? = null
) {
    init {
        this.numeroUno
        this.numeroDos
        println("Inicializando")
    }
}
