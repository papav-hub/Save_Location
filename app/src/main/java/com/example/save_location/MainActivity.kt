package com.example.save_location

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.io.FileInputStream
import java.io.FileOutputStream

class MainActivity : AppCompatActivity() {

    var fname : String  = ""
    var str : String = ""

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
            diaryTextView.visibility = View.VISIBLE
            editTextTextMultiLine.visibility = View.VISIBLE
            save_Btn.visibility = View.VISIBLE
            textView2.visibility = View.VISIBLE
            cha_Btn.visibility = View.VISIBLE
            del_Btn.visibility = View.VISIBLE

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