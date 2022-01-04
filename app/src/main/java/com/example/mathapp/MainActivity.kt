package com.example.mathapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.sax.StartElementListener
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.mathapp.Model.QuestionDataBase

private const val username = "admin"
private const val password = "admin1"
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val db = QuestionDataBase(applicationContext)
        val sharedPref = getSharedPreferences("mySharedPreferences", Context.MODE_PRIVATE)
        val buttonStart = findViewById<Button>(R.id.buttonStart)
        val buttonAdmin = findViewById<Button>(R.id.buttonAdmin)
        val editTextUsername = findViewById<EditText>(R.id.editTextTextPersonName)
        if(sharedPref.getString("username","default")!="default"){
            editTextUsername.setText(sharedPref.getString("username", "default"))
        }
        buttonStart.setOnClickListener {
            val username = editTextUsername.text.toString()
            if(username!="") {
                sharedPref.edit().putString("username", username).commit()
                val intent = Intent(this, Quiz::class.java).apply {}
                startActivity(intent)
            }
            else{
                val toast = Toast.makeText(applicationContext,"Please input your username first",Toast.LENGTH_LONG)
                toast.show()
            }
        }
        buttonAdmin.setOnClickListener {
            showAdminLogin()
        }
    }
    private fun showAdminLogin() {
        val dialogBuilder = AlertDialog.Builder(this)
        val popupView = layoutInflater.inflate(R.layout.admin_login, null)
        dialogBuilder.setView(popupView)
        val buttonLogin = popupView.findViewById<Button>(R.id.buttonLogin)
        val buttonClose = popupView.findViewById<Button>(R.id.buttonClose)
        val dialog = dialogBuilder.create()
        dialog.show()
        buttonClose.setOnClickListener {
            dialog.dismiss()
        }
        buttonLogin.setOnClickListener {
            val adminUsername = popupView.findViewById<EditText>(R.id.editTextAdminUsername).text.toString()
            val adminPassword = popupView.findViewById<EditText>(R.id.editTextAdminPassword).text.toString()
            if(adminUsername==username&&adminPassword==password){
                adminLogin()
            }
            else{
                val toast = Toast.makeText(this,"Wrong Username/Password",Toast.LENGTH_SHORT)
                toast.show()
            }
        }
    }
    private fun adminLogin() {
        val intent = Intent(this, AdminPanel::class.java)
        startActivity(intent)
    }
}
