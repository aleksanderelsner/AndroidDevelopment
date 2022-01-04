package com.example.mathapp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.mathapp.Model.Question
import com.example.mathapp.Model.QuestionDataBase
import java.io.IOException

private var question: String = ""
private var questionImage: ByteArray? = null
private lateinit var chosenPhoto: Uri
private var topic: String = ""
private var correctAnswer: String = ""
private var wrongAnswer: String = ""
class AdminPanel : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_panel)
        popupMenu()
        findViewById<Button>(R.id.buttonEditQuestions).setOnClickListener {
            editQuestions()
        }
    }
    fun chooseImage(view: View) {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 100)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 100){
            chosenPhoto = data?.data!!
            questionImage = readBytes(applicationContext, chosenPhoto)
        }
    }
    @Throws(IOException::class)
    private fun readBytes(context: Context, uri: Uri): ByteArray? =
        context.contentResolver.openInputStream(uri)?.buffered()?.use { it.readBytes() }
    fun back(view: View){
        finish()
    }

    private fun editQuestions(){
        val db = QuestionDataBase(applicationContext)
        if(db.getAllQuestions().size==0){
            val toast = Toast.makeText(this,"There is no questions to edit!",Toast.LENGTH_SHORT)
            toast.show()
        }else {
            val intent = Intent(this, EditQuestions::class.java).apply { }
            startActivity(intent)
        }
    }
    private fun popupMenu(){
        val buttonTopic = findViewById<Button>(R.id.buttonTopic)
        val popupMenu = PopupMenu(applicationContext,buttonTopic)
        popupMenu.inflate(R.menu.topic_menu)
        popupMenu.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.topic_addition_and_substraction -> {
                    topic = "Number - addition and substraction"
                    buttonTopic.text =topic
                    true
                }
                R.id.topic_fractions -> {
                    topic = "Numbers - fractions"
                    buttonTopic.text =topic
                    true
                }
                R.id.topic_measurement ->{
                    topic = "Measurement"
                    buttonTopic.text =topic
                    true
                }
                R.id.topic_multiplication_and_division ->{
                    topic = "Numbers - multiplication and divison"
                    buttonTopic.text =topic
                    true
                }
                R.id.topic_number_and_place_value -> {
                    topic = "Numbers - number and place value"
                    buttonTopic.text =topic
                    true
                }
                R.id.topic_position_and_directions -> {
                    topic = "Geometry - position and direction"
                    buttonTopic.text =topic
                    true
                }
                R.id.topic_property_of_shapes ->{
                    topic = "Geometry - property of shapes"
                    buttonTopic.text =topic
                    true
                }
                else -> true
            }
        }
        buttonTopic.setOnClickListener {
            val popup = PopupMenu::class.java.getDeclaredField("mPopup")
            popup.isAccessible = true
            val menu = popup.get(popupMenu)
            popupMenu.show()
        }
    }

    fun addQuestion(view: View){
        val dbHelper = QuestionDataBase(applicationContext)
        val toast = Toast.makeText(applicationContext,"Please input your username first", Toast.LENGTH_SHORT)
        question = findViewById<EditText>(R.id.editTextQuestion).text.toString()
        correctAnswer = findViewById<EditText>(R.id.editTextCorrectAnswer).text.toString()
        wrongAnswer = findViewById<EditText>(R.id.editTextWrongAnswer).text.toString()
        if(question==""){
            toast.setText("Please input a question")
        } else if(questionImage==null){
            toast.setText("Please choose a question image")
        } else if(topic==""){
            toast.setText("Please choose a topic")
        } else if(correctAnswer==""){
            toast.setText("Please input a correct answer")
        } else if(wrongAnswer==""){
            toast.setText("Please input a wrong answer")
        }else{
            val bool = dbHelper.addQuestion(Question(question, questionImage!!,topic, wrongAnswer, correctAnswer))
            if(bool){
                toast.setText("Question Added Successfully!")
            }
            else{
                toast.setText("Question Was Not Added")
            }
        }
        toast.show()
    }
}