// ESqliteHelperEmpresa.kt
package com.example.gr2sw2024b_jpca

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import org.example.Vehiculo

class ESqliteHelperVehiculo(contexto: Context?) :
    SQLiteOpenHelper(contexto, "prueba", null, 1) {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("""
        CREATE TABLE EMPRESA(
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            nombre VARCHAR(50),
            fechaFundacion VARCHAR(50),
            esActiva INTEGER,
            ingresosAnuales REAL,
            latitud REAL,     
            longitud REAL   
        )
    """.trimIndent())
    }
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {}

    fun crearEmpresa(vehiculo: Vehiculo): Boolean {
        val db = writableDatabase
        val valores = ContentValues().apply {
            put("nombre", vehiculo.nombre)
            put("fechaFundacion", vehiculo.fechaFundacion)
            put("esActiva", vehiculo.esActiva)
            put("ingresosAnuales", vehiculo.ingresosAnuales)
            put("latitud", vehiculo.latitud)      // Nuevo campo
            put("longitud", vehiculo.longitud)    // Nuevo campo
        }
        val resultado = db.insert("EMPRESA", null, valores)
        db.close()
        return resultado != -1L
    }
    fun eliminarEmpresa(id: Int): Boolean {
        val db = writableDatabase
        val resultado = db.delete("EMPRESA", "id = ?", arrayOf(id.toString()))
        db.close()
        return resultado != 0
    }

    fun actualizarEmpresa(vehiculo: Vehiculo): Boolean {
        val db = writableDatabase
        val valores = ContentValues().apply {
            put("nombre", vehiculo.nombre)
            put("fechaFundacion", vehiculo.fechaFundacion)
            put("esActiva", vehiculo.esActiva)
            put("ingresosAnuales", vehiculo.ingresosAnuales)
            put("latitud", vehiculo.latitud)      // Nuevo campo
            put("longitud", vehiculo.longitud)    // Nuevo campo
        }
        val resultado = db.update("EMPRESA", valores, "id = ?", arrayOf(vehiculo.id.toString()))
        db.close()
        return resultado != 0
    }

    fun consultarEmpresaPorId(id: Int): Vehiculo? {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM EMPRESA WHERE id = ?", arrayOf(id.toString()))
        return if (cursor.moveToFirst()) {
            Vehiculo(
                cursor.getInt(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getInt(3) == 1,
                cursor.getDouble(4),
                cursor.getDouble(5),  // latitud
                cursor.getDouble(6)   // longitud
            )
        } else {
            null
        }.also { cursor.close() }
    }

    fun obtenerTodasLasEmpresas(): ArrayList<Vehiculo> {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM EMPRESA", null)
        val vehiculos = ArrayList<Vehiculo>()
        while (cursor.moveToNext()) {
            vehiculos.add(
                Vehiculo(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getInt(3) == 1,
                    cursor.getDouble(4),
                    cursor.getDouble(5),  // latitud
                    cursor.getDouble(6)   // longitud
                )
            )
        }
        cursor.close()
        return vehiculos
    }

    fun obtenerUltimaEmpresaCreada(): Vehiculo {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM EMPRESA ORDER BY id DESC LIMIT 1", null)
        cursor.moveToFirst()
        val vehiculo = Vehiculo(
            cursor.getInt(0),
            cursor.getString(1),
            cursor.getString(2),
            cursor.getInt(3) == 1,
            cursor.getDouble(4),
            cursor.getDouble(5),  // latitud
            cursor.getDouble(6)   // longitud
        )
        cursor.close()
        return vehiculo
    }
}