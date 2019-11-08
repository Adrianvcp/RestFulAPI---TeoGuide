package com.example.teoguideas.Controllers.Fragments

import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.core.app.ActivityCompat

import com.example.teoguideas.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_explorar.*
import java.io.IOException
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [ExplorarFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [ExplorarFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ExplorarFragment : Fragment(), OnMarkerClickListener {
    private lateinit var fusedLocationClient:FusedLocationProviderClient

    private lateinit var lastLocation: Location
    companion object{
        private const val LOCATION_PERMISSION_REQUIREST_CODE=1
    }
    override fun onMarkerClick(p0: Marker?): Boolean {
        return false
    }

    lateinit var mapFragment:SupportMapFragment
    lateinit var googleMap: GoogleMap
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fusedLocationClient=LocationServices.getFusedLocationProviderClient(requireActivity())

        return inflater.inflate(R.layout.fragment_explorar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        mapFragment=childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(OnMapReadyCallback {
            googleMap=it
            googleMap.setOnMarkerClickListener(this)
            googleMap.uiSettings.isZoomControlsEnabled=true
            setUpMap()
            initSearchingMethods()
        })
    }

    override fun onResume() {
        super.onResume()
    }
    private fun setUpMap(){
        if(ActivityCompat.checkSelfPermission(requireContext(),android.Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
           requestPermissions(
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUIREST_CODE)
            return
        }
        googleMap.isMyLocationEnabled = true
        fusedLocationClient.lastLocation.addOnSuccessListener{
            if(it!=null){

                lastLocation=it
                var currentLatLong=LatLng(it.latitude,it.longitude)
                placeMarker(currentLatLong,"Mi ubicacion")
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom((currentLatLong),13f))
            }

        }
    }
    private fun placeMarker(location:LatLng,title:String){
        var marckerOption= MarkerOptions().position(location).title(title)
        googleMap.addMarker(marckerOption)
    }
    private fun searchAction(){
        geoLocate()
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }
    private fun initSearchingMethods(){
        ic_magnify.setOnClickListener({
            searchAction()
        })
        mSearchText.setOnEditorActionListener{v,actionId,event->
            if(actionId== EditorInfo.IME_ACTION_DONE
                || actionId==EditorInfo.IME_ACTION_DONE
                || event.action==KeyEvent.ACTION_DOWN
                || event.action==KeyEvent.KEYCODE_ENTER){
                geoLocate()
                val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view?.windowToken, 0)
                true
            }else{
                false
            }

        }
    }
    fun geoLocate(){
        var searchString=mSearchText.text.toString()
        var geocoder:Geocoder= Geocoder(context)
        var list:List<Address>? =null
        try {
            list=geocoder.getFromLocationName(searchString,1)
        }catch (e:IOException){

        }
        var size:Int= list?.size?:0
        if(size > 0){
            var address:Address?=list?.get(0)
            var coords=LatLng(address?.latitude?:0.0,address?.longitude?:0.0)
            placeMarker(coords,address?.getAddressLine(0)?:"")
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom((coords),13f))

        }
    }
}
