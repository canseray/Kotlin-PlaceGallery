package com.example.kplacegallery

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_choose_place.*


var globalName = ""
var globalType = ""
var globalAtmosphere = ""
var globalImage : Bitmap? = null

class ChoosePlaceActivity : AppCompatActivity() {

    var choosenImage : Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_place)
    }



    @RequiresApi(Build.VERSION_CODES.M)
    fun selectImage(view: View) {
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 2)
        } else {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent,1)
        }
        fun next(view: View) {
        }
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode==2){
            if (grantResults.size>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                val intent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(intent,1)
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode==1 && resultCode == Activity.RESULT_OK && data != null){
            val selected = data.data
            try{
                choosenImage = MediaStore.Images.Media.getBitmap(this.contentResolver,selected)
                imageView.setImageBitmap(choosenImage)
            } catch (e: Exception){
                e.printStackTrace()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }





    fun next (view: View){
        globalImage = choosenImage
        globalName = nameText.text.toString()
        globalType = typeText.text.toString()
        globalAtmosphere = atmosphereText.text.toString()
        val intent = Intent(applicationContext,MainActivity::class.java)
        startActivity(intent)
    }



}