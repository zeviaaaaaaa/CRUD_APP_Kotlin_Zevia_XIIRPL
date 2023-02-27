package com.example.crud_app_zevia_xiirpl

import android.annotation.SuppressLint
import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.crud_app_zevia_xiirpl.helper.DBHelper
import com.google.android.material.textfield.TextInputEditText
import java.lang.Exception
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    lateinit var inputName: TextInputEditText
    lateinit var inputAge: TextInputEditText
    lateinit var buttonSubmit: Button
    lateinit var buttonPrint: Button
    lateinit var textName: TextView
    lateinit var textAge: TextView
    lateinit var textId: TextView
    var progresDialog : ProgressDialog? = null

    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //inisialisasikan widget
        inputName = findViewById(R.id.inputName)
        inputAge = findViewById(R.id.inputAge)
        buttonSubmit = findViewById(R.id.btnAdd)
        buttonPrint = findViewById(R.id.btnPrint)
        textId = findViewById(R.id.textId)
        textName = findViewById(R.id.textName)
        textAge = findViewById(R.id.textAge)


        //laod handler
        loadHandler()

        //event ketika button ada di klik
        buttonSubmit.setOnClickListener {
            val db = DBHelper(this,null)
            val name = inputName.text.toString()
            val age = inputAge.text.toString()

            db.addProfile(name,age)

            inputName.text!!.clear()
            inputAge.text!!.clear()
        }

        buttonPrint.setOnClickListener {
            loadHandler()
        }

    }

    @SuppressLint("Range")
    fun loadHandler(){
        val db = DBHelper(this,null)
        val cursor = db.getProfile()

        progresDialog = ProgressDialog(this@MainActivity)
        progresDialog!!.setTitle("Loading")
        progresDialog!!.setMessage("Wait For a Minutes ... data Will Show")
        progresDialog!!.max=100
        progresDialog!!.progress=1
        progresDialog!!.show()

        Thread ( Runnable {
            try {
                Thread.sleep(1000)
            }catch (e : Exception){
                e.printStackTrace()
            }
            progresDialog!!.dismiss()
        } ).start()

        if(cursor!!.moveToFirst()) {
            textId.text= "ID\n\n"
            textName.text= "Name\n\n"
            textAge.text= "Age\n\n"
            textId.append(
                cursor.getString(
                    cursor.getColumnIndex(DBHelper.ID_COL)) + "\n")
            textName.append(
                cursor.getString(
                    cursor.getColumnIndex(DBHelper.NAME_COL)) + "\n")
            textAge.append(
                cursor.getString(
                    cursor.getColumnIndex(DBHelper.AGE_COL)) + "\n")
        }

        if(cursor!!.moveToNext()) {
            while (cursor.moveToNext()) {
                textId.append(
                    cursor.getString(
                        cursor.getColumnIndex(DBHelper.ID_COL)) + "\n")
                textName.append(
                    cursor.getString(
                        cursor.getColumnIndex(DBHelper.NAME_COL)) + "\n")
                textAge.append(
                    cursor.getString(
                        cursor.getColumnIndex(DBHelper.AGE_COL)) + "\n")
            }
        }
        cursor.close()
    }
}