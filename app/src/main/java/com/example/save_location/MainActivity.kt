package com.example.save_location

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.activity_main.*
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.util.*
import java.util.jar.Manifest


class MainActivity : AppCompatActivity() {

    var fname : String  = ""
    var str : String = ""

    var lat : Double = 0.0
    var log : Double = 0.0

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


        L_Btn.setOnClickListener {////////////////////////////////////////////////////////////////////////////// 위치 불러오기 버튼
            L_textView.setText(L)
            Log.d("L", L)
            // L = Location[fused 37.421998,-122.084000 hAcc=20 et=+1d13h40m16s333ms alt=5.0 vel=0.0 vAcc=40 sAcc=??? bAcc=??? {Bundle[mParcelledData.dataSize=52]}]
            // fused A, B = 위도, 경도

            L = L.replace("Location[fused", "")
            L = L.replace("hAcc", "*")

            Log.d("L", L)


            var check : Int = 0
            var end : Int = 0
            for(i in L.indices){ // 필터링
                if(L[i]=='*'){
                    check = i
                }
            }
            //Log.d("L", L)
            Log.d("L", (check-1).toString())
            L = L.substring(0, check-1)

            for(i in L.indices){ // 필터링
                if(L[i]==','){
                    check = i
                }
                end = i
            }

            lat = L.substring(0, check).toDouble() // 위도
            log = L.substring(check+1, end).toDouble() // 경도



            var asdf : String = "위도 : " + lat.toString() + " 경도 : " + log.toString()

            L_textView.setText(asdf)

            val geocoder = Geocoder(this)
            val list = geocoder.getFromLocation(lat, log, 1)
            val address = list[0].getAddressLine(0) // 위치정보

            L_textView.setText(address)
            Log.d("L", address)




            editTextTextMultiLine.setText(textView2.getText().toString()) // 저장된 내용 불러오기

            if(editTextTextMultiLine.getText().toString()==""){ // 아무것도 없다면 줄바꿈 없음
                var a : String = editTextTextMultiLine.getText().toString() + address
                editTextTextMultiLine.setText(a) // editText에 위치정보 추가하기
            }else{
                var a : String = editTextTextMultiLine.getText().toString() + "\n" + address
                editTextTextMultiLine.setText(a) // editText에 위치정보 추가하기
            }

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

                cha_Btn.setOnClickListener {// 수정 버튼
                    editTextTextMultiLine.setText(str)
                    textView2.text = "${editTextTextMultiLine.getText()}"
                }

                del_Btn.setOnClickListener {// 삭제 버튼
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