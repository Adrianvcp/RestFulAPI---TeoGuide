package com.example.teoguideas

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.senthil.kotlin_bottomnavigationview.Fragment.FirstFragment
import com.example.senthil.kotlin_bottomnavigationview.Fragment.SecondFragment
import com.example.senthil.kotlin_bottomnavigationview.Fragment.ThirdFragment
import com.example.teoguideas.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers

class BottomNavigationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_bottom_navigation)

        setUpToolbar()

        val bottomNavigation: BottomNavigationView = findViewById(R.id.navigationView)
        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        // To open the first tab as default

        val firstFragment = FirstFragment()
        openFragment(firstFragment)
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener {
        when (it.itemId) {

            R.id.navigation_dashboard -> {
                val MainActivity = MainActivity()
                val firstFragment = FirstFragment()
                openFragment(firstFragment)
                return@OnNavigationItemSelectedListener true
            }

            R.id.navigation_billUpload -> {
                val secondFragment = SecondFragment()
                openFragment(secondFragment)
                return@OnNavigationItemSelectedListener true
            }

            R.id.navigation_settings -> {
                val thirdFragment = ThirdFragment()
                openFragment(thirdFragment)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    fun setUpToolbar() {

        // Hide action bar
        val actionBar = supportActionBar
        actionBar!!.hide()
    }
}
