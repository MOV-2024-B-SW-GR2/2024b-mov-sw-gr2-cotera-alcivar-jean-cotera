package org.example

import java.io.File


class ProyectoController {
    private val archivo = File("proyectos.txt")
    private val proyectos = mutableListOf<Proyecto>()

    init {
        cargarProyectos()
    }

    private fun cargarProyectos() {
        if (archivo.exists()) {
            archivo.forEachLine { linea ->
                val datos = linea.split(",")
                proyectos.add(
                    Proyecto(
                        id = datos[0].toInt(),
                        nombre = datos[1],
                        descripcion = datos[2],
                        presupuesto = datos[3].toDouble(),
                        empresaId = datos[4].toInt()
                    )
                )
            }
        }
    }

    private fun guardarProyectos() {
        try {
            archivo.writeText(proyectos.joinToString("\n") {
                "${it.id},${it.nombre},${it.descripcion},${it.presupuesto},${it.empresaId}"
            })
        } catch (e: Exception) {
            println("Error al guardar proyectos: ${e.message}")
        }
    }

    fun crearProyecto(proyecto: Proyecto) {
        proyectos.add(proyecto)
        guardarProyectos()
    }

    fun leerProyectos(): List<Proyecto> = proyectos

    fun actualizarProyecto(id: Int, nuevoProyecto: Proyecto) {
        val index = proyectos.indexOfFirst { it.id == id }
        if (index != -1) {
            proyectos[index] = nuevoProyecto
            guardarProyectos()
        }
    }

    fun eliminarProyecto(id: Int) {
        proyectos.removeIf { it.id == id }
        guardarProyectos()
    }
}