package com.example.teoguideas.Controllers.Activities

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.teoguideas.Adapter.AdapterInicioSearch
import com.example.teoguideas.Controllers.Fragments.ExplorarFragment
import com.example.teoguideas.Controllers.Fragments.InicioFragment
import com.example.teoguideas.Controllers.Fragments.PerfilFragment
import com.example.teoguideas.Controllers.Fragments.PlanesFragment
import com.example.teoguideas.Model.CentroHistorico
import com.example.teoguideas.Model.InicioSearchResult
import com.example.teoguideas.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_root.*
import kotlinx.android.synthetic.main.fragment_inicio.*
import kotlin.math.log

class RootActivity : AppCompatActivity() {

    lateinit var inicioSearchAdapter: AdapterInicioSearch
    var searchResultList:List<InicioSearchResult> = ArrayList()
    var userId:String?=null

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        navigateTo(item)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_root)
        userId=intent.getStringExtra("url")
        Log.d("ID USER ROOT ACTIVITY", userId)
        setSupportActionBar(findViewById(R.id.topbar_main))



        val navView: BottomNavigationView = findViewById(R.id.navigationBar)
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
       navigateTo(navView.menu.findItem(R.id.navigation_inicio))


        inicioSearchAdapter= AdapterInicioSearch(searchResultList)

        recyclerViewSearchResult.apply {
            adapter=inicioSearchAdapter
            layoutManager= LinearLayoutManager(this.context)
        }
        recyclerViewSearchResult.visibility=View.GONE

        searchTextMain.setOnEditorActionListener{v,actionId,event->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                //performSearch();
                true;
            }
            false;
        }
        searchTextMain.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                        fetchSearchResults()
            }
        })
        searchTextMain.setOnFocusChangeListener(View.OnFocusChangeListener { view, b ->
                if (b) {
                    recyclerViewSearchResult.visibility=View.VISIBLE
                }

        })
        inicio_btn_search.setOnClickListener{

        }
        inicio_btn_cancel_search.setOnClickListener{
            clearSearchEnviroment()
        }

    }

    private fun navigateTo(item: MenuItem): Boolean {
        item.isChecked=true
        return supportFragmentManager
            .beginTransaction()
            .replace(R.id.content, getFragmentFor(item))
            .commit() > 0
    }
    private fun getFragmentFor(item: MenuItem): Fragment {
        clearSearchEnviroment()
        if(item.itemId==R.id.navigation_inicio){
            MainSearchBar.visibility = View.VISIBLE
            toolBarTitle.text="Inicio"
            return InicioFragment()
        }else if(item.itemId==R.id.navigation_explorar){
            MainSearchBar.visibility = View.INVISIBLE
            toolBarTitle.text="Explorar"
            return ExplorarFragment()
        }else if(item.itemId==R.id.navigation_planes){
            MainSearchBar.visibility = View.INVISIBLE
            toolBarTitle.text="Planes"
            return PlanesFragment.newInstance(userId?:"XXXX")
        }else if(item.itemId==R.id.navigation_perfil){
            MainSearchBar.visibility = View.INVISIBLE
            toolBarTitle.text="Perfil"
            return PerfilFragment.newInstance(userId?:"xxxx")
        }
        return InicioFragment()
    }
    fun fetchSearchResults(){
        Log.d("search text",searchTextMain.text.toString())
        var db= FirebaseFirestore.getInstance()
        //val docRef = db.collection("centrosHistoricos").whereGreaterThanOrEqualTo("nombre",searchTextMain.text.toString()).whereLessThanOrEqualTo("nombre",searchTextMain.text.toString())
        val docRef = db.collection("centrosHistoricos").whereArrayContains("hints", searchTextMain.text.toString())
        var tmpData: MutableList<InicioSearchResult> = mutableListOf<InicioSearchResult>()
        docRef.get()
            .addOnSuccessListener { result ->
                if (result != null) {
                    if(result.documents.size>0){

                        for (document in result) {
                            tmpData.add(InicioSearchResult(document.id.toInt(),document.data?.get("nombre").toString(),document.data?.get("ubicacion").toString(),document.data?.get("url").toString()))
                            Log.d("Firebase",document.data?.get("nombre").toString())

                        }
                        inicioSearchAdapter.arrResults=tmpData
                        inicioSearchAdapter.notifyDataSetChanged()
                        //recomendadosAdapter.arrCentrosHistoricos=tmpData
                        //recomendadosAdapter.notifyDataSetChanged()
                    }else{
                        //Toast.makeText(context,"Sitio no encontrado", Toast.LENGTH_LONG).show()
                        inicioSearchAdapter.arrResults=tmpData
                        inicioSearchAdapter.notifyDataSetChanged()
                    }

                } else {
                   // Log.d(TAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                //Log.d(TAG, "get failed with ", exception)
            }

    }
    fun clearSearchEnviroment(){
        recyclerViewSearchResult.visibility=View.GONE
        searchTextMain.text.clear()
        searchTextMain.clearFocus()
        searchTextMain.onEditorAction(EditorInfo.IME_ACTION_DONE)
        val inputManager:InputMethodManager =getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(currentFocus?.windowToken, InputMethodManager.SHOW_FORCED)
    }
}
