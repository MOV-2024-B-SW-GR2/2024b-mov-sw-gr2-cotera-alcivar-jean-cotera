package org.example
import java.io.File
import java.time.LocalDate
import java.time.format.DateTimeFormatter
class EmpresaController {
    private val archivo = File("empresas.txt")
    private val empresas = mutableListOf<Empresa>()

    init {
        cargarEmpresas()
    }



    private fun cargarEmpresas() {
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd") // Definir el patrón de fecha

        if (archivo.exists()) {
            archivo.forEachLine { linea ->
                val datos = linea.split(",")
                empresas.add(
                    Empresa(
                        id = datos[0].toInt(),
                        nombre = datos[1],
                        // Convertir la fecha de String a LocalDate usando el DateTimeFormatter
                        fechaFundacion = LocalDate.parse(datos[2], dateFormatter),
                        esActiva = datos[3].toBoolean(),
                        ingresosAnuales = datos[4].toDouble()
                    )
                )
            }
        }
    }


    private fun guardarEmpresas() {
        try {
            archivo.writeText(empresas.joinToString("\n") {
                "${it.id},${it.nombre},${it.fechaFundacion},${it.esActiva},${it.ingresosAnuales}"
            })
        } catch (e: Exception) {
            println("Error al guardar empresas: ${e.message}")
        }
    }

    fun crearEmpresa(empresa: Empresa) {
        empresas.add(empresa)
        guardarEmpresas()
    }

    fun leerEmpresas(): List<Empresa> = empresas

    fun actualizarEmpresa(id: Int, nuevaEmpresa: Empresa) {
        val index = empresas.indexOfFirst { it.id == id }
        if (index != -1) {
            empresas[index] = nuevaEmpresa
            guardarEmpresas()
        }
    }

    fun eliminarEmpresa(id: Int) {
        empresas.removeIf { it.id == id }
        guardarEmpresas()
    }
}