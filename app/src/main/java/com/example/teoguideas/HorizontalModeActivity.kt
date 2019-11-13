package com.example.teoguideas

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.senthil.kotlin_bottomnavigationview.Fragment.FirstFragment
import com.example.senthil.kotlin_bottomnavigationview.Fragment.SecondFragment
import com.example.senthil.kotlin_bottomnavigationview.Fragment.ThirdFragment

class HorizontalModeActivity : AppCompatActivity() {

    private val container by lazy { findViewById<View>(R.id.container) }
    private val title by lazy { findViewById<TextView>(R.id.title) }
    private val menu by lazy { findViewById<ChipNavigationBar>(R.id.bottom_menu) }

    private var lastColor: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_horizontal)

        lastColor = ContextCompat.getColor(this, R.color.blank)
        val MainActivity = MainActivity()

        menu.setOnItemSelectedListener {
            when (it) {


                R.id.activity -> {

                    val secondFragment = SecondFragment()
                    openFragment(secondFragment)
                    //return@OnNavigationItemSelectedListener true
                }

                R.id.navigation_settings -> {
                    val MainActivity = MainActivity()
                    openActivity(MainActivity)
                    //return@OnNavigationItemSelectedListener true
                }
            }


            /*
            val option = when (id) {

                R.id.activity -> R.color.activity to "Activity"
                R.id.favorites -> R.color.favorites to "Favorites"
                R.id.settings -> R.color.settings to "Settings"
                else -> R.color.white to ""
            }

            val color = ContextCompat.getColor(this@HorizontalModeActivity, option.first)
            container.colorAnimation(lastColor, color)
            lastColor = color

            title.text = option.second

             */
        }


    }
    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun openActivity(fragment: MainActivity) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

}

private fun FragmentTransaction.replace(container: Int, fragment: MainActivity) {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
}
