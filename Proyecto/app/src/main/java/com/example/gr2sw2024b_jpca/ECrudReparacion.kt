package com.example.gr2sw2024b_jpca

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import org.example.Reparacion

class ECrudReparacion : AppCompatActivity() {

    private lateinit var reparacionCrearButton: Button
    private lateinit var reparacionActualizarButton: Button
    private lateinit var reparacionIdEditText: EditText
    private lateinit var reparacionTituloEditText: EditText
    private lateinit var reparacionDescripcionEditText: EditText
    private lateinit var reparacionCostoEditText: EditText
    private lateinit var reparacionVehiculoIdEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ecrud_reparacion)

        setupViews()
        setupListeners()

        val reparacion = intent.getParcelableExtra<Reparacion>("reparacion")
        val opcion = intent.getStringExtra("opcion")

        when (opcion) {
            "editar" -> mostrarReparacionParaEditar(reparacion)
            "crear" -> mostrarFormularioParaCrearReparacion()
        }
    }

    private fun setupViews() {
        reparacionCrearButton = findViewById(R.id.btn_crear_bdd)
        reparacionActualizarButton = findViewById(R.id.btn_actualizar_bdd)
        reparacionIdEditText = findViewById(R.id.input_id_proyecto)
        reparacionTituloEditText = findViewById(R.id.input_nombre_proyecto)
        reparacionDescripcionEditText = findViewById(R.id.input_descripcion_proyecto)
        reparacionCostoEditText = findViewById(R.id.input_presupuesto_proyecto)
        reparacionVehiculoIdEditText = findViewById(R.id.input_id_empresa_proyecto)
    }

    private fun setupListeners() {
        reparacionCrearButton.setOnClickListener { crearReparacion() }
        reparacionActualizarButton.setOnClickListener { actualizarReparacion() }
    }

    private fun mostrarReparacionParaEditar(reparacion: Reparacion?) {
        reparacionIdEditText.setText(reparacion?.id.toString())
        reparacionTituloEditText.setText(reparacion?.titulo)
        reparacionDescripcionEditText.setText(reparacion?.descripcion)
        reparacionCostoEditText.setText(reparacion?.costo.toString())
        reparacionVehiculoIdEditText.setText(reparacion?.vehiculoId.toString())
        reparacionActualizarButton.visibility = View.VISIBLE
        reparacionIdEditText.visibility = View.VISIBLE
    }

    private fun mostrarFormularioParaCrearReparacion() {
        reparacionCrearButton.visibility = View.VISIBLE
        reparacionVehiculoIdEditText.setText(intent.getStringExtra("vehiculoId")?.toString())
    }

    private fun crearReparacion() {
        val titulo = reparacionTituloEditText.text.toString()
        val descripcion = reparacionDescripcionEditText.text.toString()
        val costo = reparacionCostoEditText.text.toString().toDouble()
        val vehiculoId = reparacionVehiculoIdEditText.text.toString().toInt()

        val respuesta = ESqliteHelperReparacion(this).crearReparacion(Reparacion( 0,titulo, descripcion, costo, vehiculoId))

        if (respuesta) {
            val ultimaReparacionCreada = ESqliteHelperReparacion(this).obtenerUltimaReparacionCreada(vehiculoId)
            val intentDevolverRespuesta = Intent()
            intentDevolverRespuesta.putExtra("reparacion", ultimaReparacionCreada)
            setResult(RESULT_OK, intentDevolverRespuesta)
            finish()
        } else {
            mostrarSnackbar("No se pudo crear")
        }
    }

    private fun actualizarReparacion() {
        val id = reparacionIdEditText.text.toString().toInt()
        val titulo = reparacionTituloEditText.text.toString()
        val descripcion = reparacionDescripcionEditText.text.toString()
        val costo = reparacionCostoEditText.text.toString().toDouble()
        val vehiculoId = reparacionVehiculoIdEditText.text.toString().toInt()

        val respuesta = ESqliteHelperReparacion(this).actualizarReparacion(Reparacion( id, titulo, descripcion, costo, vehiculoId))

        if (respuesta) {
            val reparacionActualizada = ESqliteHelperReparacion(this).consultarReparacionPorId(id)
            val intentDevolverRespuesta = Intent()
            intentDevolverRespuesta.putExtra("reparacion", reparacionActualizada)
            setResult(RESULT_OK, intentDevolverRespuesta)
            finish()
        } else {
            mostrarSnackbar("No se pudo actualizar")
        }
    }

    private fun mostrarSnackbar(texto: String) {
        val snack = Snackbar.make(findViewById(R.id.main), texto, Snackbar.LENGTH_INDEFINITE)
        snack.show()
    }
}