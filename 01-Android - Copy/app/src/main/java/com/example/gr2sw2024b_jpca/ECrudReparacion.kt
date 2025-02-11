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

    private lateinit var proyectoCrearButton: Button
    private lateinit var proyectoActualizarButton: Button
    private lateinit var proyectoIdEditText: EditText
    private lateinit var proyectoNombreEditText: EditText
    private lateinit var proyectoDescripcionEditText: EditText
    private lateinit var proyectoPresupuestoEditText: EditText
    private lateinit var proyectoEmpresaIdEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ecrud_reparacion)

        setupViews()
        setupListeners()

        val reparacion = intent.getParcelableExtra<Reparacion>("proyecto")
        val opcion = intent.getStringExtra("opcion")

        when (opcion) {
            "editar" -> mostrarProyectoParaEditar(reparacion)
            "crear" -> mostrarFormularioParaCrearProyecto()
        }
    }

    private fun setupViews() {
        proyectoCrearButton = findViewById(R.id.btn_crear_bdd)
        proyectoActualizarButton = findViewById(R.id.btn_actualizar_bdd)
        proyectoIdEditText = findViewById(R.id.input_id_proyecto)
        proyectoNombreEditText = findViewById(R.id.input_nombre_proyecto)
        proyectoDescripcionEditText = findViewById(R.id.input_descripcion_proyecto)
        proyectoPresupuestoEditText = findViewById(R.id.input_presupuesto_proyecto)
        proyectoEmpresaIdEditText = findViewById(R.id.input_id_empresa_proyecto)
    }

    private fun setupListeners() {
        proyectoCrearButton.setOnClickListener { crearProyecto() }
        proyectoActualizarButton.setOnClickListener { actualizarProyecto() }
    }

    private fun mostrarProyectoParaEditar(reparacion: Reparacion?) {
        proyectoIdEditText.setText(reparacion?.id.toString())
        proyectoNombreEditText.setText(reparacion?.nombre)
        proyectoDescripcionEditText.setText(reparacion?.descripcion)
        proyectoPresupuestoEditText.setText(reparacion?.presupuesto.toString())
        proyectoEmpresaIdEditText.setText(reparacion?.empresaId.toString())
        proyectoActualizarButton.visibility = View.VISIBLE
        proyectoEmpresaIdEditText.visibility = View.VISIBLE
    }

    private fun mostrarFormularioParaCrearProyecto() {
        proyectoCrearButton.visibility = View.VISIBLE
        proyectoEmpresaIdEditText.setText(intent.getStringExtra("empresaId")?.toString())
    }

    private fun crearProyecto() {
        val nombre = proyectoNombreEditText.text.toString()
        val descripcion = proyectoDescripcionEditText.text.toString()
        val presupuesto = proyectoPresupuestoEditText.text.toString().toDouble()
        val empresaId = proyectoEmpresaIdEditText.text.toString().toInt()

        val respuesta = ESqliteHelperReparacion(this).crearProyecto(Reparacion( 0,nombre, descripcion, presupuesto, empresaId))

        if (respuesta) {
            val ultimaProyectoCreada = ESqliteHelperReparacion(this).obtenerUltimoProyectoCreado(empresaId)
            val intentDevolverRespuesta = Intent()
            intentDevolverRespuesta.putExtra("proyecto", ultimaProyectoCreada)
            setResult(RESULT_OK, intentDevolverRespuesta)
            finish()
        } else {
            mostrarSnackbar("No se pudo crear")
        }
    }

    private fun actualizarProyecto() {
        val id = proyectoIdEditText.text.toString().toInt()
        val nombre = proyectoNombreEditText.text.toString()
        val descripcion = proyectoDescripcionEditText.text.toString()
        val presupuesto = proyectoPresupuestoEditText.text.toString().toDouble()
        val empresaId = proyectoEmpresaIdEditText.text.toString().toInt()

        val respuesta = ESqliteHelperReparacion(this).actualizarProyecto(Reparacion( id, nombre, descripcion, presupuesto, empresaId))

        if (respuesta) {
            val proyectoActualizado = ESqliteHelperReparacion(this).consultarProyectoPorId(id)
            val intentDevolverRespuesta = Intent()
            intentDevolverRespuesta.putExtra("proyecto", proyectoActualizado)
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