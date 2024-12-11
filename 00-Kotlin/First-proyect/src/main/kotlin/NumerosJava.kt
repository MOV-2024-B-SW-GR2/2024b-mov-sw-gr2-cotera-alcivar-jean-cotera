package org.example

abstract class NumerosJava {
    protected val numeroUno: Int
    protected val numeroDos: Int

    constructor(uno: Int, dos: Int) {
        this.numeroUno = uno
        this.numeroDos = dos
        println("Inicializando")
    }
}