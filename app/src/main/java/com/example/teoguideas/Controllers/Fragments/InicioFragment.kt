package com.example.teoguideas.Controllers.Fragments

import android.Manifest
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.app.AlertDialog
import android.content.ComponentName
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.provider.ContactsContract
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlincomicreader.Adapter.MyComicAdapter
import com.example.teoguideas.CamaraActivity
import com.example.teoguideas.Common.Common
import com.example.teoguideas.Controllers.Activities.RootActivity

import com.example.teoguideas.R
import com.example.teoguideas.Retrofit.IComicAPI
import com.example.teoguideas.Service.PicassoImageLoadingService
import com.example.teoguideas.perfilRecursoActivity
import com.google.android.gms.location.LocationServices
import dmax.dialog.SpotsDialog
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ss.com.bannerslider.Slider
import java.io.*
import java.lang.StringBuilder
import java.util.ArrayList

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


    internal var compositeDisposable = CompositeDisposable()
    internal lateinit var iComicAPI: IComicAPI
    internal var mBitmap: Bitmap? = null
    private var permissionsToRequest: ArrayList<String>? = null
    private val permissions = ArrayList<String>()
    private val permissionsRejected = ArrayList<String>()
    lateinit var imageView:ImageView


    private fun getPathFromURI(contentUri: Uri?): String {

        val proj = arrayOf(MediaStore.Audio.Media.DATA)

        var sorting = ContactsContract.Contacts.DISPLAY_NAME + " DESC"
        var cursor = getActivity()?.getContentResolver()
            ?.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, sorting)
        //val cursor = contentResolver.query(contentUri!!, proj, null, null, null)
        val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
        cursor.moveToFirst()
        return cursor.getString(column_index)
    }
    val pickImageChooserIntent: Intent
        get() {

            val outputFileUri = captureImageOutputUri

            val allIntents = ArrayList<Intent>()
            val packageManager = activity!!.packageManager

            val captureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            val listCam = packageManager.queryIntentActivities(captureIntent, 0)
            for (res in listCam) {
                val intent = Intent(captureIntent)
                intent.component = ComponentName(res.activityInfo.packageName, res.activityInfo.name)
                intent.setPackage(res.activityInfo.packageName)
                if (outputFileUri != null) {
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri)
                }
                allIntents.add(intent)
            }

            val galleryIntent = Intent(Intent.ACTION_GET_CONTENT)
            galleryIntent.type = "image/*"
            val listGallery = packageManager.queryIntentActivities(galleryIntent, 0)
            for (res in listGallery) {
                val intent = Intent(galleryIntent)
                intent.component = ComponentName(res.activityInfo.packageName, res.activityInfo.name)
                intent.setPackage(res.activityInfo.packageName)
                allIntents.add(intent)
            }

            var mainIntent = allIntents[allIntents.size - 1]
            for (intent in allIntents) {
                if (intent.component!!.className == "com.android.documentsui.DocumentsActivity") {
                    mainIntent = intent
                    break
                }
            }
            allIntents.remove(mainIntent)

            val chooserIntent = Intent.createChooser(mainIntent, "Select source")
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toTypedArray<Parcelable>())

            println(chooserIntent)
            return chooserIntent
        }

    private fun askPermissions() {
        permissions.add(Manifest.permission.CAMERA)
        permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        permissionsToRequest = findUnAskedPermissions(permissions)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


            if (permissionsToRequest!!.size > 0)
                requestPermissions(permissionsToRequest!!.toTypedArray<String>(), ALL_PERMISSIONS_RESULT)
        }
    }
    private fun findUnAskedPermissions(wanted: ArrayList<String>): ArrayList<String> {
        val result = ArrayList<String>()

        for (perm in wanted) {
            if (!hasPermission(perm)) {
                result.add(perm)
            }
        }

        return result
    }
    private fun hasPermission(permission: String): Boolean {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return ActivityCompat.checkSelfPermission(activity!!.applicationContext,permission) == PackageManager.PERMISSION_GRANTED
            }
        }
        return true
    }
    private fun showMessageOKCancel(message: String, okListener: DialogInterface.OnClickListener) {
        AlertDialog.Builder(activity!!.baseContext)
            .setMessage(message)
            .setPositiveButton("OK", okListener)
            .setNegativeButton("Cancel", null)
            .create()
            .show()
    }
    private fun canMakeSmores(): Boolean {
        return Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1
    }
    private val captureImageOutputUri: Uri?
        get() {
            var outputFileUri: Uri? = null
            println("URI :" + outputFileUri)

            //val getImage = getExternalFilesDir("")
            //val getImage = Environment.getExternalStorageDirectory().toString()
            val getImage = activity!!.applicationContext.getFilesDir().getPath()
            println("getImage :" + getImage)
            if (getImage != null) {
                println("image :" + getImage)
                outputFileUri = Uri.fromFile(File(getImage))
            }
            return outputFileUri
        }

    private fun getImageFromFilePath(data: Intent?): String? {
        val isCamera = data == null || data.data == null

        return if (isCamera)
            captureImageOutputUri!!.path
        else {
            println(data)
            getPathFromURI(data!!.data)
        }

    }

    fun getImageFilePath(data: Intent?): String? {
        return getImageFromFilePath(data)
    }


    @TargetApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {

        when (requestCode) {

            ALL_PERMISSIONS_RESULT -> {
                for (perms in permissionsToRequest!!) {
                    if (!hasPermission(perms)) {
                        permissionsRejected.add(perms)
                    }
                }

                if (permissionsRejected.size > 0) {


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(permissionsRejected[0])) {
                            showMessageOKCancel("These permissions are mandatory for the application. Please allow access.",
                                DialogInterface.OnClickListener { dialog, which -> requestPermissions(permissionsRejected.toTypedArray<String>(), ALL_PERMISSIONS_RESULT) })
                            return
                        }
                    }

                }
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var v = inflater.inflate(R.layout.activity_main, container, false)
        imageView = v.findViewById<ImageView>(R.id.imageView)
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        askPermissions()
        iComicAPI = Common.api


        Slider.init(PicassoImageLoadingService(activity!!.applicationContext))

        recycler_comic.setHasFixedSize(true)
        recycler_comic.layoutManager = GridLayoutManager(activity!!.applicationContext,2)

        swipe_refresh.setColorSchemeResources(R.color.colorPrimary,android.R.color.holo_orange_dark,android.R.color.background_dark)
        swipe_refresh.setOnRefreshListener {
            if (Common.isConnectedToInternet(activity!!.baseContext)){

                fetchComic()
            }
            else{
                Toast.makeText(activity!!.baseContext,"Please check u connection", Toast.LENGTH_SHORT).show()

            }
        }
        swipe_refresh.post(Runnable {
            if (Common.isConnectedToInternet(activity!!.baseContext)){

                fetchComic()
            }
            else{
                Toast.makeText(activity!!.baseContext,"Please check u connection", Toast.LENGTH_SHORT).show()

            }
        })

        btn_camara.setOnClickListener(View.OnClickListener {

        })


        btnProbando.setOnClickListener {
            println("Funcionando")

            startActivityForResult(pickImageChooserIntent, IMAGE_RESULT);


        }

        btnSubir.setOnClickListener {
            //val intent:Intent = Intent(this,perfilRecursoActivity::class.java)
            if (mBitmap != null) {
                multipartImageUpload()
                //startActivity(intent)
            }
            else {
                Toast.makeText(activity!!.applicationContext, "Bitmap is null. Try again", Toast.LENGTH_SHORT).show()
            }
        }


    }




    @SuppressLint("MissingSuperCall")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {



            //val imageView = findViewById<ImageView>(R.id.imageView)

            if (requestCode == IMAGE_RESULT) {


                val filePath = getImageFilePath(data)
                if (filePath != null) {
                    mBitmap = BitmapFactory.decodeFile(filePath)

                    imageView.setImageBitmap(mBitmap)
                }
            }




    }

    private fun multipartImageUpload() {
        try {
            val filesDir = activity!!.baseContext.filesDir
            val file = File(filesDir, "image" +".png")


            val bos = ByteArrayOutputStream()

            mBitmap!!.compress(Bitmap.CompressFormat.PNG, 0, bos)
            val bitmapdata = bos.toByteArray()


            val fos = FileOutputStream(file)
            fos.write(bitmapdata)
            fos.flush()
            fos.close()


            val reqFile = RequestBody.create(MediaType.parse("image/*"), file)
            val body = MultipartBody.Part.createFormData("upload", file.name, reqFile)
            val name = RequestBody.create(MediaType.parse("text/plain"), "upload")

            //val req = apiService.postImage(body, name)
            val req = iComicAPI.postImage(body, name)
            req.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {

                    if (response.code() == 200) {
                        //textView.text = "Uploaded Successfully!"
                        //textView.setTextColor(Color.BLUE)
                        println("OKKKKKKKKKKKKKKKKKKKKKKKKKK")
                    }
                    Toast.makeText(activity!!.applicationContext, response.code().toString() + " ", Toast.LENGTH_SHORT).show()
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    //textView.text = "Uploaded Failed!"
                    //textView.setTextColor(Color.RED)
                    println("MALLLLLLLLLLLL")
                    Toast.makeText(activity!!.applicationContext, "Request failed", Toast.LENGTH_SHORT).show()
                    t.printStackTrace()
                }
            })


        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }



    private fun fetchComic() {

        val dialog = SpotsDialog.Builder()
            .setContext(activity)
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
                recycler_comic.adapter =
                    activity?.baseContext?.let { MyComicAdapter(context = it, centroList = comicList) }
                if (!swipe_refresh.isRefreshing)
                    dialog.dismiss()
                swipe_refresh.isRefreshing = false
            },
                {thr ->
                    Toast.makeText(activity!!.baseContext,"No se cargaron los datos", Toast.LENGTH_SHORT).show()
                    if (!swipe_refresh.isRefreshing)
                        dialog.dismiss()
                    swipe_refresh.isRefreshing = false
                }))
    }

    companion object {
        private val ALL_PERMISSIONS_RESULT = 107
        private val IMAGE_RESULT = 200
    }
}

