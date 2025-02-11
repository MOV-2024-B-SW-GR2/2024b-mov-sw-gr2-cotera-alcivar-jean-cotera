package com.example.gr2sw2024b_jpca

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import org.example.Vehiculo

class ECrudVehiculo : AppCompatActivity() {

    private lateinit var vehiculoCrearButton: Button
    private lateinit var vehiculoActualizarButton: Button
    private lateinit var vehiculoIdEditText: EditText
    private lateinit var vehiculoPlacaEditText: EditText
    private lateinit var vehiculoFechaCompraEditText: EditText
    private lateinit var vehiculoUsaDieselEditText: EditText
    private lateinit var vehiculoPrecioEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ecrud_vehiculo)

        setupViews()
        setupListeners()

        val vehiculo = intent.getParcelableExtra<Vehiculo>("vehiculo")
        val opcion = intent.getStringExtra("opcion")

        when (opcion) {
            "editar" -> mostrarEmpresaParaEditar(vehiculo)
            "crear" -> mostrarFormularioParaCrearVehiculo()
        }
    }

    private fun setupViews() {
        vehiculoCrearButton = findViewById(R.id.btn_crear_bdd)
        vehiculoActualizarButton = findViewById(R.id.btn_actualizar_bdd)
        vehiculoIdEditText = findViewById(R.id.input_id_empresa)
        vehiculoPlacaEditText = findViewById(R.id.input_nombre_empresa)
        vehiculoFechaCompraEditText = findViewById(R.id.input_fecha_fundacion)
        vehiculoUsaDieselEditText = findViewById(R.id.input_esta_activa)
        vehiculoPrecioEditText = findViewById(R.id.input_ingresos_anuales)
    }

    private fun setupListeners() {
        vehiculoCrearButton.setOnClickListener { crearVehiculo() }
        vehiculoActualizarButton.setOnClickListener { actualizarVehiculo() }
    }

    private fun mostrarEmpresaParaEditar(vehiculo: Vehiculo?) {
        vehiculoIdEditText.setText(vehiculo?.id.toString())
        vehiculoPlacaEditText.setText(vehiculo?.placa)
        vehiculoFechaCompraEditText.setText(vehiculo?.fechaCompra)
        vehiculoUsaDieselEditText.setText(if (vehiculo?.usaDiesel == true) "Si" else "No")
        vehiculoPrecioEditText.setText(vehiculo?.precio.toString())
        empresaLatitudEditText.setText("0")
        empresaLongitudEditText.setText("0")
        vehiculoActualizarButton.visibility = View.VISIBLE
    }

    private fun mostrarFormularioParaCrearVehiculo() {
        vehiculoCrearButton.visibility = View.VISIBLE
    }

    private fun crearVehiculo() {
        val placa = vehiculoPlacaEditText.text.toString()
        val fechaCompra = vehiculoFechaCompraEditText.text.toString()
        val usaDiesel = vehiculoUsaDieselEditText.text.toString().toLowerCase() == "si"
        val precio = vehiculoPrecioEditText.text.toString().toDouble()

        val respuesta = ESqliteHelperVehiculo(this).crearVehiculo(
            Vehiculo(0, placa, fechaCompra, usaDiesel, precio)
        )

        if (respuesta) {
            val ultimoVehiculoCreado = ESqliteHelperVehiculo(this).obtenerUltimoVehiculoCreado()
            val intentDevolverRespuesta = Intent()
            intentDevolverRespuesta.putExtra("vehiculo", ultimoVehiculoCreado)
            setResult(RESULT_OK, intentDevolverRespuesta)
            finish()
        } else {
            mostrarSnackbar("No se pudo crear")
        }
    }

    private fun actualizarVehiculo() {
        val id = vehiculoIdEditText.text.toString().toInt()
        val placa = vehiculoPlacaEditText.text.toString()
        val fechaCompra = vehiculoFechaCompraEditText.text.toString()
        val usaDiesel = vehiculoUsaDieselEditText.text.toString().toLowerCase() == "si"
        val precio = vehiculoPrecioEditText.text.toString().toDouble()
        val respuesta = ESqliteHelperVehiculo(this).actualizarVehiculo(
            Vehiculo(id, placa, fechaCompra, usaDiesel, precio)
        )

        if (respuesta) {
            val vehiculoActualizado = ESqliteHelperVehiculo(this).consultarVehiculoPorId(id)
            val intentDevolverRespuesta = Intent()
            intentDevolverRespuesta.putExtra("vehiculo", vehiculoActualizado)
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