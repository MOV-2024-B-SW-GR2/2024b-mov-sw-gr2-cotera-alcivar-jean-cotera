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
import org.example.Reparacion

class MainReparacion : AppCompatActivity() {

    private lateinit var reparacionAdapter: ArrayAdapter<Reparacion>
    private var reparaciones = ArrayList<Reparacion>()
    private lateinit var reparacionSeleccionado: Reparacion
    private var posicionSeleccionada = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_proyect)
        setupUI()
        inicializarBaseDeDatos()
        cargarReparaciones()
        setupListeners()
    }

    private fun setupUI() {
        supportActionBar?.hide()
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
    }

    private fun inicializarBaseDeDatos() {
        EBaseDeDatos.tablaReparacion = ESqliteHelperReparacion(this)
    }

    private fun cargarReparaciones() {
        val vehiculo = intent.getParcelableExtra<Vehiculo>("vehiculo")
        val vehiculoId = vehiculo!!.id
        reparacions = EBaseDeDatos.tablaProyecto!!.obtenerTodasLasReparacionesPorIdVehiculo(vehiculoId).toCollection(ArrayList())
        reparacionAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, reparacions)
        val listView = findViewById<ListView>(R.id.lv_list_view)
        listView.adapter = reparacionAdapter
    }

    private fun setupListeners() {
        val botonCrearReparacion = findViewById<Button>(R.id.btn_empresa)
        val vehiculo = intent.getParcelableExtra<Vehiculo>("vehiculo")
        botonCrearReparacion.setOnClickListener {
            val intent = Intent(this, ECrudReparacion::class.java)
            intent.putExtra("opcion", "crear")
            intent.putExtra("vehiculoId", vehiculo?.id.toString())
            lanzarActividadConResultado.launch(intent)
        }

        val listView = findViewById<ListView>(R.id.lv_list_view)

        registerForContextMenu(listView)
    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_proy, menu)
        val info = menuInfo as AdapterView.AdapterContextMenuInfo
        posicionSeleccionada = info.position
        reparacionSeleccionado = reparacions[posicionSeleccionada]
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mi_eliminar -> {
                EBaseDeDatos.tablaReparacion!!.eliminarReparacion(reparacionSeleccionado.id)
                reparacions.removeAt(posicionSeleccionada)
                reparacionAdapter.notifyDataSetChanged()
                true
            }
            R.id.mi_editar -> {
                val intent = Intent(this, ECrudReparacion::class.java)
                intent.putExtra("opcion", "editar")
                intent.putExtra("reparacion", reparacionSeleccionado)
                lanzarActividadConResultado.launch(intent)
                true
            }

            else -> super.onContextItemSelected(item)
        }
    }

    val lanzarActividadConResultado = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val reparacion = result.data?.getParcelableExtra<Reparacion>("reparacion")
            val vehiculo = intent.getParcelableExtra<Vehiculo>("vehiculo")
            val vehiculoId = vehiculo!!.id
            if (reparacion != null) {
                reparacions.removeIf { it.id == reparacion.id }

                if (reparacion.empresaId == vehiculoId) {
                    reparacions.add(reparacion)
                }
                reparacionAdapter.notifyDataSetChanged()
            }
        }
    }
}