package com.example.teoguideas

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Matrix
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.util.Rational
import android.util.Size
import android.view.Surface
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.camera.core.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.example.kotlincomicreader.Adapter.MyComicAdapter
import com.example.teoguideas.Common.Common
import com.example.teoguideas.Retrofit.IComicAPI
import com.example.teoguideas.Service.PicassoImageLoadingService
import dmax.dialog.SpotsDialog
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import ss.com.bannerslider.Slider
import java.io.File
import java.lang.StringBuilder
import java.nio.ByteBuffer
import java.util.concurrent.TimeUnit



class MainActivity : AppCompatActivity() {

    internal var compositeDisposable = CompositeDisposable()
    internal lateinit var iComicAPI: IComicAPI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //Init API
        iComicAPI = Common.api

        Slider.init(PicassoImageLoadingService(this))

        recycler_comic.setHasFixedSize(true)
        recycler_comic.layoutManager = GridLayoutManager(this,2)

        swipe_refresh.setColorSchemeResources(R.color.colorPrimary,android.R.color.holo_orange_dark,android.R.color.background_dark)
        swipe_refresh.setOnRefreshListener {
            if (Common.isConnectedToInternet(baseContext)){

                fetchComic()
            }
            else{
                Toast.makeText(baseContext,"Please check u connection",Toast.LENGTH_SHORT).show()

            }
        }
        swipe_refresh.post(Runnable {
            if (Common.isConnectedToInternet(baseContext)){

                fetchComic()
            }
            else{
                Toast.makeText(baseContext,"Please check u connection",Toast.LENGTH_SHORT).show()

            }
        })

        btn_camara.setOnClickListener(View.OnClickListener {
            val intent:Intent = Intent(this,CamaraActivity::class.java)
            startActivity(intent)
        })


        btnProbando.setOnClickListener(View.OnClickListener {
            val intent:Intent = Intent(this,perfilRecursoActivity::class.java)
            startActivity(intent)
        })

    }
    

    private fun fetchComic() {
        val dialog = SpotsDialog.Builder()
            .setContext(this)
            .setMessage("Please wait...")
            .build()
        if (!swipe_refresh.isRefreshing)
            dialog.show()
        compositeDisposable.add(iComicAPI.CHistoList
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({comicList ->
                txt_comic.text = StringBuilder("Populares ")
                    .append(comicList.size)
                    .append("")
                recycler_comic.adapter = MyComicAdapter(baseContext,comicList)
                if (!swipe_refresh.isRefreshing)
                    dialog.dismiss()
                swipe_refresh.isRefreshing = false
            },
                {thr ->
                    Toast.makeText(baseContext,"No se cargaron los datos",Toast.LENGTH_SHORT).show()
                    if (!swipe_refresh.isRefreshing)
                        dialog.dismiss()
                    swipe_refresh.isRefreshing = false
                }))
    }

}
