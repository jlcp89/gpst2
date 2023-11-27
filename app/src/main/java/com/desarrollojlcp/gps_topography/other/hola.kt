package site.x3la

import android.Manifest
import android.app.ProgressDialog
import android.content.*
import android.content.ContentValues.TAG
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.os.Looper
import android.provider.Settings
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.sql.Date
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Arrays


class MainActivity : AppCompatActivity() {



    private lateinit var signInRequest: BeginSignInRequest
    private lateinit var mWindow: View
    private lateinit var mContents: View



    /** Demonstrates customizing the info window and/or its contents.  */
    open fun CustomInfoWindowAdapter() {

    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //Variables de la actividad


    //variables de inicio de sesión
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var mMap: GoogleMap

    //variables del servicio de ubicacion
    private var broadcastReceiver: BroadcastReceiver? = null
    private var mBound: Boolean = false

    //variables de funcionamiento de la actividad
    private var ubicacionActual : LatLng? = null

    var progressDialog: ProgressDialog? = null


    //constantes
    private val MY_PERMISSIONS_REQUEST_LOGIN = 67
    private val MY_PERMISSIONS_REQUEST_LOCATION = 68
    private val MY_PERMISSIONS_REQUEST_INTERNET = 69
    private val REQUEST_CHECK_SETTINGS = 129
    private var permisosOtorgados: Boolean = false
    private lateinit var googleMapT : GoogleMap
    private lateinit var locationCallback: LocationCallback
    private lateinit var currentLocation: Location
    private val permsRequestCode = MY_PERMISSIONS_REQUEST_LOCATION
    private val perms = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.INTERNET
    )
    private val ENDPOINT = "https://x3la.site/wp-json/wp/v2/c197p"
    private lateinit var gson: Gson
    private var primerUso= true
    private lateinit var autoTextViewNom: AutoCompleteTextView
    private lateinit var adapterNom: ArrayAdapter<String>
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth
    private lateinit var fBoton: FloatingActionButton


    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, MY_PERMISSIONS_REQUEST_LOGIN)
    }

    private lateinit var analytics: FirebaseAnalytics



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        analytics = Firebase.analytics

        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("80788162080-rip6phbnbu9u4uofuskuuj0d9s07l198.apps.googleusercontent.com")
            .requestEmail()
            .build()



        googleSignInClient = GoogleSignIn.getClient(this, gso)



        signIn()

    }

    fun AppCompatActivity.hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
    }






    ////////////////////////////////////////////////////////////////////////////////////////////////
    //Permisos
    //Que hacer con el resultado de la solicitud de permisos, si no se dan la app no deberia funcionar
    //Inicializar el servicio de ubicación, el cliente Fused y el mapa
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_LOCATION -> {


                return
            }

            MY_PERMISSIONS_REQUEST_INTERNET -> {
                if ((grantResults.isNotEmpty() && grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
                    Log.e("MainActivity:","Internet Permission Granted")
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(applicationContext,"Sin permiso de internet. La app no mostrara el mapa.",Toast.LENGTH_SHORT).show()
                }
                return
            }
        }
    }






    ////////////////////////////////////////////////////////////////////////////////////////////////
    //funciones generales de la actividad

    //override el onActivityResult para que haga lo que se desea
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {

            MY_PERMISSIONS_REQUEST_LOGIN-> {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                try {
                    // Google Sign In was successful, authenticate with Firebase

                    val account = task.getResult(ApiException::class.java)!!
                    Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                    firebaseAuthWithGoogle(account.idToken!!)
                    //Toast.makeText(applicationContext,"Google Sign In was successful, authenticate with Firebase",Toast.LENGTH_LONG).show()


                } catch (e: ApiException) {
                    // Google Sign In failed, update UI appropriately
                    Log.w(TAG, "Google sign in failed", e)
                }
            }

            REQUEST_CHECK_SETTINGS -> {
                try {

                } catch (_: Exception) {

                }
            }
        }
    }

    lateinit var user: FirebaseUser


    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    user = auth.currentUser!!
                    //updateUI(user)
                    if (user != null) {
                        //Toast.makeText(applicationContext,"Bienvenid@ "+user.email,Toast.LENGTH_SHORT).show()
                        var current : String
                        val formatter = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            current = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                        } else {
                            current = "2023-04-2023"
                        }

                        val parameters = Bundle().apply {
                            this.putString("usuario", user.email)
                            this.putString("fecha_log", current)
                        }
                        analytics.logEvent("LogIn", parameters)
                    }

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    //updateUI(null)
                    Toast.makeText(applicationContext,"La autenticación fallo...",Toast.LENGTH_LONG).show()

                }
            }
    }


    //Funciones estandar de la actividad
    override fun onStart() {
        super.onStart()
        val account = GoogleSignIn.getLastSignedInAccount(this)
    }

    override fun onResume() {
        super.onResume()
        val intentFilter = IntentFilter()
        intentFilter.addAction("NotifyUser")
        broadcastReceiver?.let {
            LocalBroadcastManager.getInstance(this).registerReceiver(it, intentFilter)
        }
    }

    override fun onPause() {
        broadcastReceiver?.let {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(it)
        }
        super.onPause()
    }

    override fun onStop() {
        if (mBound) {
            // Unbind from the service. This signals to the service that this activity is no longer
            // in the foreground, and the service can respond by promoting itself to a foreground
            // service.
            mBound = false
        }
        super.onStop()
    }






}
