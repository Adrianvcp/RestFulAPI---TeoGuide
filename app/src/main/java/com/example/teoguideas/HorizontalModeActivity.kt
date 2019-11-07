package com.example.teoguideas

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.senthil.kotlin_bottomnavigationview.Fragment.FirstFragment
import com.example.senthil.kotlin_bottomnavigationview.Fragment.SecondFragment
import com.example.senthil.kotlin_bottomnavigationview.Fragment.ThirdFragment
import com.example.teoguideas.util.colorAnimation
import com.example.teoguideas.ChipNavigationBar
import com.example.teoguideas.util.colorAnimation

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

        menu.setOnItemSelectedListener { id ->


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

            val option = when (id) {
                R.id.home -> {
                    val MainActivity = MainActivity()
                    val firstFragment = FirstFragment()
                    openFragment(firstFragment)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.activity -> R.color.activity to "Activity"
                R.id.favorites -> R.color.favorites to "Favorites"
                R.id.settings -> R.color.settings to "Settings"
                else -> R.color.white to ""
            }

            val color = ContextCompat.getColor(this@HorizontalModeActivity, option.first)
            container.colorAnimation(lastColor, color)
            lastColor = color

            title.text = option.second
        }
    }

}
