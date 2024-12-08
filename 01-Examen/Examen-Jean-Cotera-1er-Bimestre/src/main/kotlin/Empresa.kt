package org.example

import java.time.LocalDate

data class Empresa(
    val id: Int,
    val nombre: String,
    var fechaFundacion: LocalDate,  // Cambiado a LocalDate
    val esActiva: Boolean,
    val ingresosAnuales: Double
)