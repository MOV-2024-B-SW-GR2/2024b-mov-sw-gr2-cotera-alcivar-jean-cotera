// ESqliteHelperProyecto.kt
package com.example.gr2sw2024b_jpca

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import org.example.Proyecto

class ESqliteHelperProyecto(contexto: Context?) :
    SQLiteOpenHelper(contexto, "moviles", null, 1) {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("""
            CREATE TABLE Proyecto(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre VARCHAR(50),
                descripcion VARCHAR(50),
                presupuesto REAL,
                empresaId INTEGER,
                FOREIGN KEY (empresaId) REFERENCES Empresa(id)
            )
        """.trimIndent())
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {}

    fun crearProyecto(proyecto: Proyecto): Boolean {
        val db = writableDatabase
        val valores = ContentValues().apply {
            put("nombre", proyecto.nombre)
            put("descripcion", proyecto.descripcion)
            put("presupuesto", proyecto.presupuesto)
            put("empresaId", proyecto.empresaId)
        }
        val resultado = db.insert("Proyecto", null, valores)
        db.close()
        return resultado != -1L
    }

    fun eliminarProyecto(id: Int): Boolean {
        val db = writableDatabase
        val resultado = db.delete("Proyecto", "id = ?", arrayOf(id.toString()))
        db.close()
        return resultado != 0
    }

    fun actualizarProyecto(proyecto: Proyecto): Boolean {
        val db = writableDatabase
        val valores = ContentValues().apply {
            put("nombre", proyecto.nombre)
            put("descripcion", proyecto.descripcion)
            put("presupuesto", proyecto.presupuesto)
            put("empresaId", proyecto.empresaId)
        }
        val resultado = db.update("Proyecto", valores, "id = ?", arrayOf(proyecto.id.toString()))
        db.close()
        return resultado != 0
    }

    fun consultarProyectoPorId(id: Int): Proyecto? {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM Proyecto WHERE id = ?", arrayOf(id.toString()))
        return if (cursor.moveToFirst()) {
            Proyecto(
                cursor.getInt(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getDouble(3),
                cursor.getInt(4)
            )
        } else {
            null
        }.also { cursor.close() }
    }

    fun obtenerTodosLosProyectosPorIdEmpresa(idEmpresa: Int): ArrayList<Proyecto> {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM Proyecto WHERE empresaId = ?", arrayOf(idEmpresa.toString()))
        val proyectos = ArrayList<Proyecto>()
        while (cursor.moveToNext()) {
            proyectos.add(
                Proyecto(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getDouble(3),
                    cursor.getInt(4)
                )
            )
        }
        cursor.close()
        return proyectos
    }

    fun obtenerUltimoProyectoCreado(idEmpresa: Int): Proyecto {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM Proyecto WHERE empresaId = ? ORDER BY id DESC LIMIT 1", arrayOf(idEmpresa.toString()))
        cursor.moveToFirst()
        val proyecto = Proyecto(
            cursor.getInt(0),
            cursor.getString(1),
            cursor.getString(2),
            cursor.getDouble(3),
            cursor.getInt(4)
        )
        cursor.close()
        return proyecto
    }
}