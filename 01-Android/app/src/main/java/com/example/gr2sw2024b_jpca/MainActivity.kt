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
import org.example.Empresa

class MainActivity : AppCompatActivity() {

    private lateinit var empresaAdapter: ArrayAdapter<Empresa>
    private var empresas = ArrayList<Empresa>()
    private lateinit var empresaSeleccionada: Empresa
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
        EBaseDeDatos.tablaEmpresa = ESqliteHelperEmpresa(this)
        EBaseDeDatos.tablaProyecto = ESqliteHelperProyecto(this)
    }

    private fun cargarEmpresas() {
        empresas = EBaseDeDatos.tablaEmpresa!!.obtenerTodasLasEmpresas().toCollection(ArrayList())
        empresaAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, empresas)
        val listView = findViewById<ListView>(R.id.lv_list_view)
        listView.adapter = empresaAdapter
    }

    private fun setupListeners() {
        val botonCrearEmpresa = findViewById<Button>(R.id.btn_empresa)
        botonCrearEmpresa.setOnClickListener {
            val intent = Intent(this, ECrudEmpresa::class.java)
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
        empresaSeleccionada = empresas[posicionSeleccionada]
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mi_editar -> {
                val intent = Intent(this, ECrudEmpresa::class.java)
                intent.putExtra("opcion", "editar")
                intent.putExtra("empresa", empresaSeleccionada)
                lanzarActividadConResultado.launch(intent)
                true
            }
            R.id.mi_eliminar -> {
                EBaseDeDatos.tablaEmpresa!!.eliminarEmpresa(empresaSeleccionada.id)
                empresas.removeAt(posicionSeleccionada)
                empresaAdapter.notifyDataSetChanged()
                true
            }
            R.id.mi_proyecto -> {
                val intent = Intent(this, MainProyecto::class.java)
                intent.putExtra("empresa", empresaSeleccionada)
                startActivity(intent)
                true
            }
            R.id.mi_ubicacion -> {  // Nueva opción para el mapa
                val intent = Intent(this, MapsActivity::class.java).apply {
                    putExtra("latitud", empresaSeleccionada.latitud)
                    putExtra("longitud", empresaSeleccionada.longitud)
                    putExtra("nombre", empresaSeleccionada.nombre)
                }
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
            val empresa = result.data?.getParcelableExtra<Empresa>("empresa")
            if (empresa != null) {
                empresas.removeIf { it.id == empresa.id }
                empresas.add(empresa)
                empresaAdapter.notifyDataSetChanged()
            }
        }

    }
}