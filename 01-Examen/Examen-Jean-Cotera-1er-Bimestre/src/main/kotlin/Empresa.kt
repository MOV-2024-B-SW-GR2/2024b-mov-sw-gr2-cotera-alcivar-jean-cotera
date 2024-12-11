package org.example

import java.time.LocalDate

data class Empresa(
    val id: Int,
    val nombre: String,
    var fechaFundacion: LocalDate,
    val esActiva: Boolean,
    val ingresosAnuales: Double
)