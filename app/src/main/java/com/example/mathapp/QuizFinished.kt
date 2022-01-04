package com.example.mathapp

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mathapp.Model.QuestionDataBase
import com.example.mathapp.Model.RecyclerViewAdapter
import com.example.mathapp.Model.User
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList

private lateinit var grade: String
private lateinit var username: String
private lateinit var date: String
class QuizFinished : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_finished)
        val bundle: Bundle? = intent.extras
        val score = bundle?.getInt("score")
        var percent: Float? = (score?.toFloat()?.div(14f))
        val db = QuestionDataBase(applicationContext)
        username =
            getSharedPreferences("mySharedPreferences", Context.MODE_PRIVATE).getString("username","default")
                .toString()
        findViewById<TextView>(R.id.name).text = "Name: $username"
        date = LocalDate.now().toString()
        findViewById<TextView>(R.id.date).text = "Date: $date"
        val grade: String = when{
            percent!! >= 0.9 ->{"A*"}
            percent!! >= 0.8 ->{"A"}
            percent!! >= 0.7 ->{"B"}
            percent!! >= 0.6 ->{"C"}
            percent!! >= 0.5 ->{"D"}
            else ->{"F"}
        }
        db.addUser(User(username,grade,date))
        var allUsers:ArrayList<User> = db.getAllUsers()
        findViewById<TextView>(R.id.score).text = "Current grade: $grade"
        val recyclerView = findViewById<RecyclerView>(R.id.list)
        recyclerView.adapter = RecyclerViewAdapter(allUsers)
        recyclerView.layoutManager = LinearLayoutManager(this)

    }
}