package org.example

data class  Proyecto(
    var id: Int,
    var nombre: String,
    var descripcion: String,
    var presupuesto: Double, // Decimal
    var empresaId: Int // Relación con Empresa
)
