package com.example.save_location

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
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

    private lateinit var fusedLocationClient: FusedLocationProviderClient





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var L : String = ""

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)


        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationClient.lastLocation
                .addOnSuccessListener { location : Location? ->
                    // Got last known location. In some rare situations this can be null.
                    L = location.toString()
                }


        L_Btn.setOnClickListener {// 위치 불러오기 버튼
            L_textView.setText(L)

        }













        @SuppressLint("WrongConstant")
        fun saveDiary(readyDay: String) {/////////////////////////////////////////////////////저장
            var fos: FileOutputStream? = null

            try {
                fos = openFileOutput(readyDay, Context.MODE_PRIVATE)

                var content: String = editTextTextMultiLine.getText().toString()
                fos.write(content.toByteArray())
                fos.close()

            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        @SuppressLint("WrongConstant")
        fun removeDiary(readyDay: String) {/////////////////////////////////////////////////////삭제
            var fas: FileOutputStream? = null

            try {
                fas = openFileOutput(readyDay, Context.MODE_PRIVATE)
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

        calendarView.setOnDateChangeListener{view, year, month, dayOfMonth -> // 날자 바꾸기

            diaryTextView.text = String.format("%d / %d / %d", year, month + 1, dayOfMonth)
            textView2.setText("")
            checkedDay(year, month, dayOfMonth)
        }

        save_Btn.setOnClickListener{// save 버튼
            saveDiary(fname)
            var t1 = Toast.makeText(this, fname + "데이터를 저장했습니다.", Toast.LENGTH_SHORT)
            t1.show()
            str = textView2.getText().toString()
            textView2.text = "${str}"
            textView2.setText("${str}")
        }
    }
}