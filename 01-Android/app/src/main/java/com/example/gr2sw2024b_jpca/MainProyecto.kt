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
import org.example.Proyecto

class MainProyecto : AppCompatActivity() {

    private lateinit var proyectoAdapter: ArrayAdapter<Proyecto>
    private var proyectos = ArrayList<Proyecto>()
    private lateinit var proyectoSeleccionado: Proyecto
    private var posicionSeleccionada = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_proyect)
        setupUI()
        inicializarBaseDeDatos()
        cargarProyectos()
        setupListeners()
    }

    private fun setupUI() {
        supportActionBar?.hide()
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
    }

    private fun inicializarBaseDeDatos() {
        EBaseDeDatos.tablaProyecto = ESqliteHelperProyecto(this)
    }

    private fun cargarProyectos() {
        val empresa = intent.getParcelableExtra<Empresa>("empresa")
        val empresaId = empresa!!.id
        proyectos = EBaseDeDatos.tablaProyecto!!.obtenerTodosLosProyectosPorIdEmpresa(empresaId).toCollection(ArrayList())
        proyectoAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, proyectos)
        val listView = findViewById<ListView>(R.id.lv_list_view)
        listView.adapter = proyectoAdapter
    }

    private fun setupListeners() {
        val botonCrearProyecto = findViewById<Button>(R.id.btn_empresa)
        val empresa = intent.getParcelableExtra<Empresa>("empresa")
        botonCrearProyecto.setOnClickListener {
            val intent = Intent(this, ECrudProyecto::class.java)
            intent.putExtra("opcion", "crear")
            intent.putExtra("empresaId", empresa?.id.toString())
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
        proyectoSeleccionado = proyectos[posicionSeleccionada]
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mi_eliminar -> {
                EBaseDeDatos.tablaProyecto!!.eliminarProyecto(proyectoSeleccionado.id)
                proyectos.removeAt(posicionSeleccionada)
                proyectoAdapter.notifyDataSetChanged()
                true
            }
            R.id.mi_editar -> {
                val intent = Intent(this, ECrudProyecto::class.java)
                intent.putExtra("opcion", "editar")
                intent.putExtra("proyecto", proyectoSeleccionado)
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
            val proyecto = result.data?.getParcelableExtra<Proyecto>("proyecto")
            val empresa = intent.getParcelableExtra<Empresa>("empresa")
            val empresaId = empresa!!.id
            if (proyecto != null) {
                proyectos.removeIf { it.id == proyecto.id }

                if (proyecto.empresaId == empresaId) {
                    proyectos.add(proyecto)
                }
                proyectoAdapter.notifyDataSetChanged()
            }
        }
    }
}