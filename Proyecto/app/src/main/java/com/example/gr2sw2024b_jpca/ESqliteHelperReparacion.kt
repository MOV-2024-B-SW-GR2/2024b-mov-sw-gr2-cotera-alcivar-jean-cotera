// ESqliteHelperProyecto.kt
package com.example.gr2sw2024b_jpca

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import org.example.Reparacion

class ESqliteHelperReparacion(contexto: Context?) :
    SQLiteOpenHelper(contexto, "prueba", null, 1) {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("""
            CREATE TABLE Reparacion(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                titulo VARCHAR(50),
                descripcion VARCHAR(50),
                costo REAL,
                vehiculoId INTEGER,
                FOREIGN KEY (vehiculoId) REFERENCES Vehiculo(id)
            )
        """.trimIndent())
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {}

    fun crearReparacion(reparacion: Reparacion): Boolean {
        val db = writableDatabase
        val valores = ContentValues().apply {
            put("titulo", reparacion.titulo)
            put("descripcion", reparacion.descripcion)
            put("costo", reparacion.costo)
            put("vehiculoId", reparacion.vehiculoId)
        }
        val resultado = db.insert("Reparacion", null, valores)
        db.close()
        return resultado != -1L
    }

    fun eliminarReparacion(id: Int): Boolean {
        val db = writableDatabase
        val resultado = db.delete("Reparacion", "id = ?", arrayOf(id.toString()))
        db.close()
        return resultado != 0
    }

    fun actualizarReparacion(reparacion: Reparacion): Boolean {
        val db = writableDatabase
        val valores = ContentValues().apply {
            put("titulo", reparacion.titulo)
            put("descripcion", reparacion.descripcion)
            put("costo", reparacion.costo)
            put("vehiculoId", reparacion.vehiculoId)
        }
        val resultado = db.update("Reparacion", valores, "id = ?", arrayOf(reparacion.id.toString()))
        db.close()
        return resultado != 0
    }

    fun consultarReparacionPorId(id: Int): Reparacion? {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM Reparacion WHERE id = ?", arrayOf(id.toString()))
        return if (cursor.moveToFirst()) {
            Reparacion(
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

    fun obtenerTodasLasReparacionesPorIdVehiculo(idVehiculo: Int): ArrayList<Reparacion> {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM Reparacion WHERE vehiculoId = ?", arrayOf(idVehiculo.toString()))
        val reparacions = ArrayList<Reparacion>()
        while (cursor.moveToNext()) {
            reparacions.add(
                Reparacion(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getDouble(3),
                    cursor.getInt(4)
                )
            )
        }
        cursor.close()
        return reparacions
    }

    fun obtenerUltimaReparacionCreada(idVehiculo: Int): Reparacion {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM Reparacion WHERE vehiculoId = ? ORDER BY id DESC LIMIT 1", arrayOf(idVehiculo.toString()))
        cursor.moveToFirst()
        val reparacion = Reparacion(
            cursor.getInt(0),
            cursor.getString(1),
            cursor.getString(2),
            cursor.getDouble(3),
            cursor.getInt(4)
        )
        cursor.close()
        return reparacion
    }
}