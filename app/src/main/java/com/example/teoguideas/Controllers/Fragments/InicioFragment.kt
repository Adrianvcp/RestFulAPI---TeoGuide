package com.example.teoguideas.Controllers.Fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.teoguideas.Adapter.AdapterInicioSearch
import com.example.teoguideas.Adapter.AdapterRecomendados
import com.example.teoguideas.Controllers.Activities.FichaTecnicaActivity
import com.example.teoguideas.Model.CentroHistorico
import com.example.teoguideas.Model.InicioSearchResult

import com.example.teoguideas.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_inicio.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [InicioFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [InicioFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class InicioFragment : Fragment() {
    lateinit var recomendadosAdapter:AdapterRecomendados
    var recomendadosList: List<CentroHistorico> = ArrayList()
    lateinit var inicioSearchAdapter:AdapterInicioSearch
    var searchResultList:List<InicioSearchResult> = ArrayList()
    private val TAG="FIREBASEEEE"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_inicio, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recomendadosAdapter= AdapterRecomendados(recomendadosList)
        recyclerView.apply{
            adapter=recomendadosAdapter
            layoutManager=LinearLayoutManager(this.context)
        }
        fetchRecomendados()
    }
    fun fetchRecomendados(){
        var db= FirebaseFirestore.getInstance()
        val docRef = db.collection("centrosHistoricos").whereGreaterThan("visitas",0)
        docRef.get()
            .addOnSuccessListener { result ->
                if (result != null) {

                        var tmpData: MutableList<CentroHistorico> = mutableListOf<CentroHistorico>()
                        for (document in result) {
                            tmpData.add(CentroHistorico(document.id.toInt(),document.data?.get("nombre").toString(),document.data?.get("ubicacion").toString(),document.data?.get("historia").toString(),document.data?.get("lat")?.toString()?.toFloat(),document.data?.get("long")?.toString()?.toFloat(),document.data?.get("imagen").toString(),document.data?.get("url").toString()))
                            Log.d(TAG,tmpData.size.toString())

                        }

                        recomendadosAdapter.arrCentrosHistoricos=tmpData
                        recomendadosAdapter.notifyDataSetChanged()


                } else {
                    Log.d(TAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }
    }
    override fun onResume() {
        super.onResume()
    }
}

