package com.example.teoguideas.view

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.example.teoguideas.Model.MenuItem


internal abstract class MenuItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    abstract fun bind(item: MenuItem)

    abstract fun showBadge(count: Int = 0)

    abstract fun dismissBadge()

}