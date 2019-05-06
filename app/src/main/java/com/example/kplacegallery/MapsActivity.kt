package com.example.kplacegallery

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.parse.ParseFile
import com.parse.ParseObject
import com.parse.ParseUser
import java.io.ByteArrayOutputStream

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    var latitude = ""
    var longitude = ""
    var locationManager : LocationManager? = null
    var locationListener : LocationListener? = null





    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.menu2,menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item?.itemId == R.id.save_place){
            saveParse()
        }
        return super.onOptionsItemSelected(item)
    }







    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }







    @RequiresApi(Build.VERSION_CODES.M)
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.setOnMapLongClickListener { myListener } //XZ

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location?) {
                if (location != null){
                    var userLocation = LatLng(location.latitude,location.longitude)
                    mMap.clear()
                    mMap.addMarker(MarkerOptions().position(userLocation).title("My location"))
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation,15f))
                }
            }
            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
            }
            override fun onProviderEnabled(provider: String?) {
            }
            override fun onProviderDisabled(provider: String?) {
            }
        } //izin varsa
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                                                                    != PackageManager.PERMISSION_GRANTED){
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),1)
        } else {
            locationManager!!.requestLocationUpdates(LocationManager.GPS_PROVIDER,2,2f,locationListener)

            //harita açıldıgında bulundugum yere zoom
            mMap.clear()
            var lastLocation = locationManager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            var lastUserLocation = LatLng(lastLocation.latitude,lastLocation.longitude)
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastUserLocation,15f))

        }
    }



    //yoksa ve istediyse
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (grantResults.size > 0){
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
                locationManager!!.requestLocationUpdates(LocationManager.GPS_PROVIDER,2,2f,locationListener)
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }



    //tıklayarak yer secme
    val myListener = object : GoogleMap.OnMapClickListener{  //XZ
        override fun onMapClick(p0: LatLng?) {
            mMap.clear()
            mMap.addMarker(MarkerOptions().position(p0!!).title(globalName))
            latitude = p0.latitude.toString()
            longitude = p0.longitude.toString()


        }

    }


    fun saveParse(){

        //resimi veriye,veriyi dosyaya
        val byteArrayOutputStream = ByteArrayOutputStream()
        globalImage!!.compress(Bitmap.CompressFormat.PNG,50,byteArrayOutputStream)
        val bytes = byteArrayOutputStream.toByteArray()

        val parseFile = ParseFile("image.png",bytes)

        val parseObject = ParseObject("Locations")

        parseObject.put("name", globalName)
        parseObject.put("type", globalType)
        parseObject.put("atmosphere", globalAtmosphere)
        parseObject.put("latitude",latitude)
        parseObject.put("longitude",longitude)
        parseObject.put("username", ParseUser.getCurrentUser().username.toString())
        parseObject.put("image",parseFile)
        parseObject.saveInBackground { e ->
            if (e != null){
                Toast.makeText(applicationContext,"error",Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(applicationContext,"location created",Toast.LENGTH_LONG).show()
                val intent = Intent(applicationContext,LocationActivity::class.java)
                startActivity(intent)
            }
        }
    }


}
