package com.androidstudio.kotlin.pockemon

import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    var ACCESS_LOCATION = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        checkPermission()
    }

    /**
     * @see checkPermission
     * REQUEST LOCATION ACCESS FROM USER
     */
    fun checkPermission(){
        if(Build.VERSION.SDK_INT>=23){
            if (ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
                requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),ACCESS_LOCATION)
                return
            }
        }
        getUserLocation()
        loadPockemon()
    }

    fun getUserLocation(){
        Toast.makeText(this,"User location access on.",Toast.LENGTH_LONG).show()

        var myLocation = MyLocationListner()
        var locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,3,3f,myLocation)

        var myThread = myThread()
        myThread.start()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            ACCESS_LOCATION->{
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    getUserLocation()
                }
                else{
                    Toast.makeText(this,"We can not access your location.",Toast.LENGTH_LONG).show()
                }
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

    }
    var location:Location?=null

    inner class MyLocationListner:LocationListener{



        constructor(){
            location = Location("start")
            location!!.longitude = 0.0
            location!!.latitude = 0.0
        }

        override fun onLocationChanged(Location: Location) {
            location = Location
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
            //super.onStatusChanged(provider, status, extras)
        }

        override fun onProviderEnabled(provider: String) {
           // super.onProviderEnabled(provider)
        }

        override fun onProviderDisabled(provider: String) {
           //super.onProviderDisabled(provider)
        }
    }

    var oldLocation:Location?=null
    inner class myThread:Thread{

        constructor():super(){
            oldLocation = Location("start")
            oldLocation!!.longitude = 0.0
            oldLocation!!.latitude = 0.0
        }

        override fun run() {
            while(true){

                try {

                    if(oldLocation!!.distanceTo(location)==0f){
                        continue
                    }

                    oldLocation = location

                    runOnUiThread {
                        // Add a marker in Sydney and move the camera
                        mMap.clear()
                        val sydney = LatLng(location!!.latitude, location!!.longitude)
                        mMap.addMarker(MarkerOptions()
                            .position(sydney)
                            .title("Me")
                            .snippet("Here is my location")
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.mario)))
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,14f))

                        // show pockemons
                        for (i in 0..pockemonList.size-1){
                            var newPockemon = pockemonList[i]

                            if (!newPockemon.isCatch!!){
                                val packemonLoc = LatLng(newPockemon.lat!!,newPockemon.log!!)
                                mMap.addMarker(MarkerOptions()
                                    .position(packemonLoc )
                                    .title(newPockemon.name!!)
                                    .snippet(newPockemon.des!!)
                                    .icon(BitmapDescriptorFactory.fromResource(newPockemon.image!!)))

                            }
                        }
                    }
                    Thread.sleep(1000)
                }catch (err:Exception){

                }
            }
        }
    }

    var pockemonList = ArrayList<Pockemon>()

    fun loadPockemon(){
        pockemonList.add(
            Pockemon("Charmander",
            "Here is from Japan",
            R.drawable.charmander,
            55.0,
            37.7789994893035,-122.401846647263)
        )

        pockemonList.add(Pockemon("Bulbasaur",
            "Bulbasaur living in USA",
            R.drawable.bulbasaur,
            90.5,
            37.7949568502667,-122.410494089127))

        pockemonList.add(Pockemon("Squirtle",
            "Squirtle living in Iraq",
            R.drawable.squirtle,
            33.5,
            37.7816621152613,-122.41225361824))
    }
}