// ESqliteHelperEmpresa.kt
package com.example.gr2sw2024b_jpca

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import org.example.Vehiculo

class ESqliteHelperVehiculo(contexto: Context?) :
    SQLiteOpenHelper(contexto, "moviles", null, 1) {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("""
        CREATE TABLE Vehiculo(
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            placa VARCHAR(50),
            fechaCompra VARCHAR(50),
            usaDiesel INTEGER,
            precio REAL   
        )
    """.trimIndent())
    }
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {}

    fun crearVehiculo(vehiculo: Vehiculo): Boolean {
        val db = writableDatabase
        val valores = ContentValues().apply {
            put("placa", vehiculo.placa)
            put("fechaCompra", vehiculo.fechaCompra)
            put("usaDiesel", vehiculo.esActiva)
            put("precio", vehiculo.precio)
        }
        val resultado = db.insert("Vehiculo", null, valores)
        db.close()
        return resultado != -1L
    }
    fun eliminarVehiculo(id: Int): Boolean {
        val db = writableDatabase
        val resultado = db.delete("Vehiculo", "id = ?", arrayOf(id.toString()))
        db.close()
        return resultado != 0
    }

    fun actualizarVehiculo(vehiculo: Vehiculo): Boolean {
        val db = writableDatabase
        val valores = ContentValues().apply {
            put("placa", vehiculo.placa)
            put("fechaCompra", vehiculo.fechaCompra)
            put("usaDiesel", vehiculo.esActiva)
            put("precio", vehiculo.precio)
        }
        val resultado = db.update("Vehiculo", valores, "id = ?", arrayOf(vehiculo.id.toString()))
        db.close()
        return resultado != 0
    }

    fun consultarVehiculoPorId(id: Int): Vehiculo? {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM Vehiculo WHERE id = ?", arrayOf(id.toString()))
        return if (cursor.moveToFirst()) {
            Vehiculo(
                cursor.getInt(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getInt(3) == 1,
                cursor.getDouble(4),
            )
        } else {
            null
        }.also { cursor.close() }
    }

    fun obtenerTodosLosVehiculos(): ArrayList<Vehiculo> {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM Vehiculo", null)
        val vehiculos = ArrayList<Vehiculo>()
        while (cursor.moveToNext()) {
            vehiculos.add(
                Vehiculo(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getInt(3) == 1,
                    cursor.getDouble(4),
                )
            )
        }
        cursor.close()
        return vehiculos
    }

    fun obtenerUltimoVehiculoCreado(): Vehiculo {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM Vehiculo ORDER BY id DESC LIMIT 1", null)
        cursor.moveToFirst()
        val vehiculo = Vehiculo(
            cursor.getInt(0),
            cursor.getString(1),
            cursor.getString(2),
            cursor.getInt(3) == 1,
            cursor.getDouble(4)
        )
        cursor.close()
        return vehiculo
    }
}