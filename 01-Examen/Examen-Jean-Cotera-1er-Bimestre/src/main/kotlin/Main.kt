package org.example

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Scanner
fun main() {
    val scanner = Scanner(System.`in`)
    val empresaController = EmpresaController()
    val proyectoController = ProyectoController()
    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    var opcion: Int

    do {
        println(
            """
            Menú de opciones:
            1. Crear una empresa
            2. Crear un proyecto
            3. Ver todas las empresas
            4. Ver todos los proyectos
            5. Actualizar una empresa
            6. Eliminar una empresa
            7. Actualizar un proyecto
            8. Eliminar un proyecto
            9. Salir
            Selecciona una opción:
            """.trimIndent()
        )

        opcion = scanner.nextInt()
        scanner.nextLine() // Consumir el salto de línea

        when (opcion) {
            1 -> {
                println("Ingrese los datos de la empresa:")
                print("ID: ")
                val id = scanner.nextInt()
                scanner.nextLine() // Consumir el salto de línea
                print("Nombre: ")
                val nombre = scanner.nextLine()
                print("Fecha de Fundación (yyyy-MM-dd): ")
                val fechaString = scanner.nextLine()

                // Convertir la fecha de String a LocalDate
                val fechaFundacion = try {
                    LocalDate.parse(fechaString, dateFormatter)
                } catch (e: Exception) {
                    println("Error: La fecha ingresada no es válida. Asegúrese de usar el formato yyyy-MM-dd.")
                    continue
                }

                print("¿Está activa? (true/false): ")
                val esActiva = scanner.nextBoolean()
                print("Ingresos Anuales: ")
                val ingresosAnuales = scanner.nextDouble()

                empresaController.crearEmpresa(Empresa(id, nombre, fechaFundacion, esActiva, ingresosAnuales))
                println("Empresa creada exitosamente.")
            }

            2 -> {
                println("Ingrese los datos del proyecto:")
                print("ID: ")
                val id = scanner.nextInt()
                scanner.nextLine() // Consumir el salto de línea
                print("Nombre: ")
                val nombre = scanner.nextLine()
                print("Descripción: ")
                val descripcion = scanner.nextLine()
                print("Presupuesto: ")
                val presupuesto = scanner.nextDouble()
                scanner.nextLine() // Consumir el salto de línea
                print("ID de la empresa asociada: ")
                val empresaId = scanner.nextInt()

                // Validar si la empresa asociada existe
                val empresaAsociada = empresaController.leerEmpresas().find { it.id == empresaId }

                if (empresaAsociada != null) {
                    proyectoController.crearProyecto(Proyecto(id, nombre, descripcion, presupuesto, empresaId))
                    println("Proyecto creado exitosamente.")
                } else {
                    println("Error: No existe una empresa con el ID $empresaId. No se puede crear el proyecto.")
                }
            }

            3 -> {
                println("Lista de empresas:")
                empresaController.leerEmpresas().forEachIndexed { index, empresa ->
                    println("[$index] $empresa")
                }
            }

            4 -> {
                println("Lista de proyectos:")
                proyectoController.leerProyectos().forEachIndexed { index, proyecto ->
                    println("[$index] $proyecto")
                }
            }

            5 -> {
                println("Actualizar una empresa:")
                print("ID de la empresa a actualizar: ")
                val id = scanner.nextInt()
                scanner.nextLine() // Consumir el salto de línea

                // Validar si el ID existe
                val empresaExistente = empresaController.leerEmpresas().find { it.id == id }

                if (empresaExistente != null) {
                    print("Nuevo nombre: ")
                    val nombre = scanner.nextLine()
                    print("Fecha de Fundación (yyyy-MM-dd): ")
                    val fechaString = scanner.nextLine()
                    // Convertir la fecha de String a LocalDate
                    val fechaFundacion = try {
                        LocalDate.parse(fechaString, dateFormatter)
                    } catch (e: Exception) {
                        println("Error: La fecha ingresada no es válida. Asegúrese de usar el formato yyyy-MM-dd.")
                        continue
                    }
                    print("¿Está activa? (true/false): ")
                    val esActiva = scanner.nextBoolean()
                    print("Nuevos ingresos anuales: ")
                    val ingresosAnuales = scanner.nextDouble()

                    empresaController.actualizarEmpresa(id, Empresa(id, nombre, fechaFundacion, esActiva, ingresosAnuales))
                    println("Empresa actualizada exitosamente.")
                } else {
                    println("Error: No existe una empresa con el ID $id.")
                }
            }


            6 -> {
                println("Eliminar una empresa:")
                print("ID de la empresa a eliminar: ")
                val id = scanner.nextInt()

                // Validar si el ID existe
                val empresaExistente = empresaController.leerEmpresas().find { it.id == id }

                if (empresaExistente != null) {
                    // Eliminar la empresa
                    empresaController.eliminarEmpresa(id)
                    // Eliminar proyectos asociados
                    val proyectosAsociados = proyectoController.leerProyectos().filter { it.empresaId == id }
                    proyectosAsociados.forEach { proyectoController.eliminarProyecto(it.id) }

                    println("Empresa y sus proyectos asociados eliminados exitosamente.")
                } else {
                    println("Error: No existe una empresa con el ID $id.")
                }
            }

            7 -> {
                println("Actualizar un proyecto:")
                print("ID del proyecto a actualizar: ")
                val id = scanner.nextInt()
                scanner.nextLine() // Consumir el salto de línea

                // Validar si el ID existe
                val proyectoExistente = proyectoController.leerProyectos().find { it.id == id }

                if (proyectoExistente != null) {
                    print("Nuevo nombre: ")
                    val nombre = scanner.nextLine()
                    print("Nueva descripción: ")
                    val descripcion = scanner.nextLine()
                    print("Nuevo presupuesto: ")
                    val presupuesto = scanner.nextDouble()
                    scanner.nextLine() // Consumir el salto de línea
                    print("Nuevo ID de la empresa asociada: ")
                    val empresaId = scanner.nextInt()

                    // Validar si la empresa asociada existe
                    val empresaAsociada = empresaController.leerEmpresas().find { it.id == empresaId }
                    if (empresaAsociada != null) {
                        proyectoController.actualizarProyecto(id, Proyecto(id, nombre, descripcion, presupuesto, empresaId))
                        println("Proyecto actualizado exitosamente.")
                    } else {
                        println("Error: No existe una empresa con el ID $empresaId para asociar al proyecto.")
                    }
                } else {
                    println("Error: No existe un proyecto con el ID $id.")
                }
            }

            8 -> {
                println("Eliminar un proyecto:")
                print("ID del proyecto a eliminar: ")
                val id = scanner.nextInt()

                // Validar si el ID existe
                val proyectoExistente = proyectoController.leerProyectos().find { it.id == id }

                if (proyectoExistente != null) {
                    proyectoController.eliminarProyecto(id)
                    println("Proyecto eliminado exitosamente.")
                } else {
                    println("Error: No existe un proyecto con el ID $id.")
                }
            }

            9 -> println("Saliendo del programa...")
            else -> println("Opción no válida. Por favor, intente nuevamente.")
        }
        println()
    } while (opcion != 9)
}
