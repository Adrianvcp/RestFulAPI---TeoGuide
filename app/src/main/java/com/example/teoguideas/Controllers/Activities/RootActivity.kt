package com.example.teoguideas.Controllers.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.example.teoguideas.Controllers.Fragments.ExplorarFragment
import com.example.teoguideas.Controllers.Fragments.InicioFragment
import com.example.teoguideas.Controllers.Fragments.PerfilFragment
import com.example.teoguideas.Controllers.Fragments.PlanesFragment
import com.example.teoguideas.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_root.*
import kotlin.math.log

class RootActivity : AppCompatActivity() {


    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        navigateTo(item)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_root)
        val navView: BottomNavigationView = findViewById(R.id.navigationBar)
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
       navigateTo(navView.menu.findItem(R.id.navigation_inicio))

    }
    private fun navigateTo(item: MenuItem): Boolean {
        item.isChecked=true
        return supportFragmentManager
            .beginTransaction()
            .replace(R.id.content, getFragmentFor(item))
            .commit() > 0
    }
    private fun getFragmentFor(item: MenuItem): Fragment {
        if(item.itemId==R.id.navigation_inicio){
            return InicioFragment()
        }else if(item.itemId==R.id.navigation_explorar){
            return ExplorarFragment()
        }else if(item.itemId==R.id.navigation_planes){
            return PlanesFragment()
        }else if(item.itemId==R.id.navigation_perfil){
            return PerfilFragment()
        }
        return InicioFragment()
    }
}
