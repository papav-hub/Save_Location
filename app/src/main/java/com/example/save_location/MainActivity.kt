package com.example.save_location

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import kotlinx.android.synthetic.main.activity_main.*
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.util.*
import java.util.jar.Manifest


class MainActivity : AppCompatActivity() {

    var fname : String  = ""
    var str : String = ""

    var locationManager : LocationManager? = null
    private val REQUEST_CODE_LOCATION : Int = 2
    var currentLocation : String = ""
    var latitude : Double? = null
    var longitude : Double? = null

    private fun getLatLng() : Location {
        var currentLatLng : Location? = null
        if(ActivityCompat.checkSelfPermission(applicationContext, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(applicationContext, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), this.REQUEST_CODE_LOCATION)

            getLatLng()
        }else{
            val locationProvider = LocationManager.GPS_PROVIDER
            currentLatLng = locationManager?.getLastKnownLocation(locationProvider)
        }
        return currentLatLng!!
    }


    private fun getCurrentLoc(){
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager?
        var userLocation : Location = getLatLng()
        if(userLocation != null){
            latitude = userLocation.latitude
            Log.d("CheckCurrentLocation", "현재 내 위치 값 : $latitude, $longitude")
            var mGeocoder = Geocoder(applicationContext, Locale.KOREAN)
            var mResultList : List <Address>? = null
            try{
                mResultList = mGeocoder.getFromLocation(
                    latitude!!, longitude!!, 1
                )
            }catch(e: IOException){
                e.printStackTrace()
            }
            if(mResultList != null){
                Log.d("CheckCurrentLocation", mResultList[0].toString())
                /*Log.d("CheckCurrentLocation", mResultList[0].toString(), getAddressLine(0))*/
                currentLocation = mResultList[0].getAddressLine(0)
                /*currentLocation = currentLocation.substring(11)*/
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        @SuppressLint("WrongConstant")
        fun saveDiary(readyDay: String) {/////////////////////////////////////////////////////저장
            var fos: FileOutputStream? = null

            try {
                fos = openFileOutput(readyDay, MODE_NO_LOCALIZED_COLLATORS)
                var content: String = editTextTextMultiLine.getText().toString()
                fos.write(content.toByteArray())
                fos.close()

            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        L_Btn.setOnClickListener {
            getCurrentLoc()
            val L : String = "위도:" + latitude.toString() + " 경도:" + longitude.toString() + " 좌표:" + currentLocation
            L_textView.setText(L)
        }

        @SuppressLint("WrongConstant")
        fun removeDiary(readyDay: String) {/////////////////////////////////////////////////////삭제
            var fas: FileOutputStream? = null

            try {
                fas = openFileOutput(readyDay, MODE_NO_LOCALIZED_COLLATORS)
                var content: String = ""
                fas.write(content.toByteArray())
                fas.close()

            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        fun checkedDay (cYear : Int, cMonth : Int, cDay : Int) {
            fname = "" + cYear + "-" + (cMonth + 1) + "-" + cDay + ".txt"

            var fis: FileInputStream? = null

            try {
                fis = openFileInput(fname)
                val fileData = ByteArray(fis.available())
                fis.read(fileData)
                fis.close()

                str = String(fileData)
                textView2.text = "${str}"
                textView2.setText("${str}")

                cha_Btn.setOnClickListener {
                    editTextTextMultiLine.setText(str)
                    textView2.text = "${editTextTextMultiLine.getText()}"
                }

                del_Btn.setOnClickListener {
                    editTextTextMultiLine.setText("")
                    removeDiary(fname)
                    var t1 = Toast.makeText(this, fname + "데이터를 삭제했습니다.", Toast.LENGTH_SHORT)
                    t1.show()
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }

            false
        }

        calendarView.setOnDateChangeListener{view, year, month, dayOfMonth ->
            /*diaryTextView.visibility = View.VISIBLE
            editTextTextMultiLine.visibility = View.VISIBLE
            save_Btn.visibility = View.VISIBLE
            textView2.visibility = View.VISIBLE
            cha_Btn.visibility = View.VISIBLE
            del_Btn.visibility = View.VISIBLE*/

            diaryTextView.text = String.format("%d / %d / %d", year, month + 1, dayOfMonth)
            textView2.setText("")
            checkedDay(year, month, dayOfMonth)
        }

        save_Btn.setOnClickListener{
            saveDiary(fname)
            var t1 = Toast.makeText(this, fname + "데이터를 저장했습니다.", Toast.LENGTH_SHORT)
            t1.show()
            str = textView2.getText().toString()
            textView2.text = "${str}"
            textView2.setText("${str}")
        }
    }
}