package org.example

import java.util.Scanner

class Suma {
    private val numeroUno: Int
    private val numeroDos: Int

    constructor(numeroUno: Int, numeroDos: Int) {
        this.numeroUno = numeroUno
        this.numeroDos = numeroDos
    }

    // Constructor secundario: cuando solo el segundo número puede ser nulo
    constructor(numeroUno: Int, numeroDos: Int?) : this(numeroUno, numeroDos?: 0)

    // Constructor secundario: cuando solo el primer número puede ser nulo
    constructor(numeroUno: Int?, numeroDos: Int) : this(numeroUno?: 0, numeroDos)

    // Constructor secundario: cuando ambos números pueden ser nulos
    constructor(numeroUno: Int?, numeroDos: Int?) : this(numeroUno ?: 0, numeroDos ?: 0)

    fun sumar():Int{
        val total = numeroUno + numeroDos;
        agregarHistorial(total);
        return total;
    }

    companion object {
        val pi = 3.14159;
        fun elevarAlCuadrado(numero:Int): Int{
            return  numero * numero;
        }

        val historial = arrayListOf<Int>();

        fun agregarHistorial(total: Int) {
            historial.add(total);
        }
    }


}
