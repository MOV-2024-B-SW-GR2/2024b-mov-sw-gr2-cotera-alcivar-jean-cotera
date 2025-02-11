package com.example.gr2sw2024b_jpca

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import org.example.Vehiculo

class MainActivity : AppCompatActivity() {

    private lateinit var vehiculoAdapter: ArrayAdapter<Vehiculo>
    private var vehiculos = ArrayList<Vehiculo>()
    private lateinit var vehiculoSeleccionada: Vehiculo
    private var posicionSeleccionada = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupUI()
        inicializarBaseDeDatos()
        cargarEmpresas()
        setupListeners()
    }

    private fun setupUI() {
        supportActionBar?.hide()
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
    }

    private fun inicializarBaseDeDatos() {
        EBaseDeDatos.tablaEmpresa = ESqliteHelperVehiculo(this)
        EBaseDeDatos.tablaProyecto = ESqliteHelperReparacion(this)
    }

    private fun cargarEmpresas() {
        vehiculos = EBaseDeDatos.tablaEmpresa!!.obtenerTodasLasEmpresas().toCollection(ArrayList())
        vehiculoAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, vehiculos)
        val listView = findViewById<ListView>(R.id.lv_list_view)
        listView.adapter = vehiculoAdapter
    }

    private fun setupListeners() {
        val botonCrearEmpresa = findViewById<Button>(R.id.btn_empresa)
        botonCrearEmpresa.setOnClickListener {
            val intent = Intent(this, ECrudVehiculo::class.java)
            intent.putExtra("opcion", "crear")
            lanzarActividadConResultado.launch(intent)
        }

        val listView = findViewById<ListView>(R.id.lv_list_view)

        registerForContextMenu(listView)
    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        val info = menuInfo as AdapterView.AdapterContextMenuInfo
        posicionSeleccionada = info.position
        vehiculoSeleccionada = vehiculos[posicionSeleccionada]
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mi_editar -> {
                val intent = Intent(this, ECrudVehiculo::class.java)
                intent.putExtra("opcion", "editar")
                intent.putExtra("empresa", vehiculoSeleccionada)
                lanzarActividadConResultado.launch(intent)
                true
            }
            R.id.mi_eliminar -> {
                EBaseDeDatos.tablaEmpresa!!.eliminarEmpresa(vehiculoSeleccionada.id)
                vehiculos.removeAt(posicionSeleccionada)
                vehiculoAdapter.notifyDataSetChanged()
                true
            }
            R.id.mi_proyecto -> {
                val intent = Intent(this, MainReparacion::class.java)
                intent.putExtra("empresa", vehiculoSeleccionada)
                startActivity(intent)
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    val lanzarActividadConResultado = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val vehiculo = result.data?.getParcelableExtra<Vehiculo>("empresa")
            if (vehiculo != null) {
                vehiculos.removeIf { it.id == vehiculo.id }
                vehiculos.add(vehiculo)
                vehiculoAdapter.notifyDataSetChanged()
            }
        }

    }
}