package com.example.teoguideas.Controllers.Fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.teoguideas.Common.Common

import com.example.teoguideas.R
import com.example.teoguideas.Retrofit.IComicAPI
import com.example.teoguideas.Service.PicassoImageLoadingService
import com.squareup.picasso.Picasso
import dmax.dialog.SpotsDialog
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_perfil_recurso.*
import ss.com.bannerslider.Slider
import java.lang.StringBuilder

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [PerfilFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [PerfilFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PerfilFragment : Fragment() {

    internal var compositeDisposable = CompositeDisposable()
    internal lateinit var iComicAPI: IComicAPI

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.activity_perfil_recurso, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //Init API
        iComicAPI = Common.api

        Slider.init(PicassoImageLoadingService(requireContext()))

        swipe_refresh.setColorSchemeResources(R.color.colorPrimary,android.R.color.holo_orange_dark,android.R.color.background_dark)
        swipe_refresh.setOnRefreshListener {
            if (Common.isConnectedToInternet(requireActivity().baseContext)){
                fetchComic()
            }
            else{
                Toast.makeText(requireActivity().baseContext,"Please check u connection", Toast.LENGTH_SHORT).show()

            }
        }
        swipe_refresh.post(Runnable {
            if (Common.isConnectedToInternet(requireActivity().baseContext)){
                fetchComic()
            }
            else{
                Toast.makeText(requireActivity().baseContext,"Please check u connection", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun fetchComic() {
        val dialog = SpotsDialog.Builder()
            .setContext(requireContext())
            .setMessage("Cargando...")
            .build()
        if (!swipe_refresh.isRefreshing)
            dialog.show()
        compositeDisposable.add(iComicAPI.PerfilList
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({comicList ->
                txtNombre.text = StringBuilder()
                    .append(comicList.size)
                    .append("")

                Picasso.get().load(comicList[0].imgportada).into(imageView)
                txtNombre.text = comicList[0].nNombre
                println(comicList[0].dHistoria)
                txtHistoria.text = comicList[0].tUbicacion
                //recycler_comic.adapter = MyComicAdapter(baseContext,comicList)

                if (!swipe_refresh.isRefreshing)
                    dialog.dismiss()
                swipe_refresh.isRefreshing = false
            },
                {thr ->
                    Toast.makeText(requireContext(),"No se cargaron los datos", Toast.LENGTH_SHORT).show()
                    if (!swipe_refresh.isRefreshing)
                        dialog.dismiss()
                    swipe_refresh.isRefreshing = false
                }))
    }
}
