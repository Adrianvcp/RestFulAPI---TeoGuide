package com.example.teoguideas

import android.R
import android.app.ListActivity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import com.example.teoguideas.util.applyWindowInsets

class MainNav : ListActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val list = ListView(this).apply {
            id = android.R.id.list
            applyWindowInsets(top = true)
        }
        setContentView(list)

        val entities = arrayOf("Horizontal orientation", "Vertical orientation")
        val adapter = ArrayAdapter(this, R.layout.simple_list_item_1, entities)
        listAdapter = adapter
    }

    override fun onListItemClick(l: ListView, v: View, position: Int, id: Long) {
        when (position) {
            0 -> startActivity(Intent(this, HorizontalModeActivity::class.java))
            1 -> startActivity(Intent(this, MainActivity::class.java))
        }
    }
}