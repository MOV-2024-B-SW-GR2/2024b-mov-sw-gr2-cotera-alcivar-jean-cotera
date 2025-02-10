package com.example.gr2sw2024b_jpca

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import org.example.Empresa

class ECrudEmpresa : AppCompatActivity() {

    private lateinit var empresaCrearButton: Button
    private lateinit var empresaActualizarButton: Button
    private lateinit var empresaIdEditText: EditText
    private lateinit var empresaNombreEditText: EditText
    private lateinit var empresaFechaFundacionEditText: EditText
    private lateinit var empresaEstaActivaEditText: EditText
    private lateinit var empresaIngresosAnualesEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ecrud_empresa)

        setupViews()
        setupListeners()

        val empresa = intent.getParcelableExtra<Empresa>("empresa")
        val opcion = intent.getStringExtra("opcion")

        when (opcion) {
            "editar" -> mostrarEmpresaParaEditar(empresa)
            "crear" -> mostrarFormularioParaCrearEmpresa()
        }
    }

    private fun setupViews() {
        empresaCrearButton = findViewById(R.id.btn_crear_bdd)
        empresaActualizarButton = findViewById(R.id.btn_actualizar_bdd)
        empresaIdEditText = findViewById(R.id.input_id_empresa)
        empresaNombreEditText = findViewById(R.id.input_nombre_empresa)
        empresaFechaFundacionEditText = findViewById(R.id.input_fecha_fundacion)
        empresaEstaActivaEditText = findViewById(R.id.input_esta_activa)
        empresaIngresosAnualesEditText = findViewById(R.id.input_ingresos_anuales)
    }

    private fun setupListeners() {
        empresaCrearButton.setOnClickListener { crearEmpresa() }
        empresaActualizarButton.setOnClickListener { actualizarEmpresa() }
    }

    private fun mostrarEmpresaParaEditar(empresa: Empresa?) {
        empresaIdEditText.setText(empresa?.id.toString())
        empresaNombreEditText.setText(empresa?.nombre)
        empresaFechaFundacionEditText.setText(empresa?.fechaFundacion)
        empresaEstaActivaEditText.setText(if (empresa?.esActiva == true) "Si" else "No")
        empresaIngresosAnualesEditText.setText(empresa?.ingresosAnuales.toString())
        empresaActualizarButton.visibility = View.VISIBLE
    }

    private fun mostrarFormularioParaCrearEmpresa() {
        empresaCrearButton.visibility = View.VISIBLE
    }

    private fun crearEmpresa() {
        val nombre = empresaNombreEditText.text.toString()
        val fechaFundacion = empresaFechaFundacionEditText.text.toString()
        val estaActiva = empresaEstaActivaEditText.text.toString().toLowerCase() == "si"
        val ingresosAnuales = empresaIngresosAnualesEditText.text.toString().toDouble()


        val respuesta = ESqliteHelperEmpresa(this).crearEmpresa( Empresa(
            0,
                nombre,
                fechaFundacion,
                estaActiva,
                ingresosAnuales
            )
        )

        if (respuesta) {
            val ultimaEmpresaCreada = ESqliteHelperEmpresa(this).obtenerUltimaEmpresaCreada()
            val intentDevolverRespuesta = Intent()
            intentDevolverRespuesta.putExtra("empresa", ultimaEmpresaCreada)
            setResult(RESULT_OK, intentDevolverRespuesta)
            finish()
        } else {
            mostrarSnackbar("No se pudo crear")
        }
    }

    private fun actualizarEmpresa() {
        val id = empresaIdEditText.text.toString().toInt()
        val nombre = empresaNombreEditText.text.toString()
        val fechaFundacion = empresaFechaFundacionEditText.text.toString()
        val estaActiva = empresaEstaActivaEditText.text.toString().toLowerCase() == "si"
        val ingresosAnuales = empresaIngresosAnualesEditText.text.toString().toDouble()

        val respuesta = ESqliteHelperEmpresa(this).actualizarEmpresa( Empresa (
            id,
            nombre,
            fechaFundacion,
            estaActiva,
            ingresosAnuales
        )
        )

        if (respuesta) {
            val empresaActualizada = ESqliteHelperEmpresa(this).consultarEmpresaPorId(id)
            val intentDevolverRespuesta = Intent()
            intentDevolverRespuesta.putExtra("empresa", empresaActualizada)
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