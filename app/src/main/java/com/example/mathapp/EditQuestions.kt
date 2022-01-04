package com.example.mathapp

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.mathapp.Model.Question
import com.example.mathapp.Model.QuestionDataBase
import org.w3c.dom.Text

private lateinit var  currentQuestion: Question
private lateinit var questionPool: ArrayList<Question>
private lateinit var db: QuestionDataBase;
private var counter: Int = 0
private var questionPoolSize: Int = 0

class EditQuestions : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_questions)
        db = QuestionDataBase(applicationContext)
        questionPool = db.getAllQuestions()
        questionPoolSize = questionPool.size
        currentQuestion = questionPool[counter]
        updateQuestion(currentQuestion)
        val buttonPrevious = findViewById<Button>(R.id.buttonPrevious)
        buttonPrevious.setOnClickListener {
            previousQuestion()
        }
        val buttonBack = findViewById<Button>(R.id.buttonBack)
        buttonBack.setOnClickListener {
            back()
        }
        val buttonDelete = findViewById<Button>(R.id.buttonDelete)
        buttonDelete.setOnClickListener {
            deleteQuestion()
        }
    }
    private fun updateQuestion(question: Question){
        findViewById<TextView>(R.id.questionCounter).text = "Question " + (counter+1).toString() + "/$questionPoolSize"
        findViewById<TextView>(R.id.question).text = "Question: " + question.question
        findViewById<TextView>(R.id.topic).text = "Topic: " + question.topic
        findViewById<TextView>(R.id.correctAnswer).text = "Correct Answer: " + question.rightAnswer
        findViewById<TextView>(R.id.wrongAnswer).text = "Wrong Answer: " + question.wrongAnswer
        val imageBitMap = BitmapFactory.decodeByteArray(question.questionImage, 0 ,question.questionImage.size)
        val questionImage = findViewById<ImageView>(R.id.questionImage)
        questionImage.setImageBitmap(imageBitMap)
    }
    fun nextQuestion(view: View){
        if(counter+1<questionPoolSize){
            counter+=1
            currentQuestion = questionPool[counter]
            updateQuestion(currentQuestion)
        } else{
            val toast = Toast.makeText(applicationContext,"This is the last question", Toast.LENGTH_SHORT)
            toast.show()
        }
    }
    private fun previousQuestion(){
        if(counter>0){
            counter-=1
            currentQuestion = questionPool[counter]
            updateQuestion(currentQuestion)
        } else {
            val toast = Toast.makeText(applicationContext,"This is a first question", Toast.LENGTH_SHORT)
            toast.show()
        }
    }
    private fun back(){
        finish()
    }
    private fun deleteQuestion(){
        val toast = Toast.makeText(applicationContext,"", Toast.LENGTH_SHORT)
        when {
            questionPoolSize==0 -> {
                toast.setText("There is no questions to delete!")
                toast.show()
            }
            counter==0&&questionPoolSize==1 ->{
                val emptyImage = ByteArray(0)
                val emptyQuestion = Question("",emptyImage,"","","")
                updateQuestion(emptyQuestion)
                questionPool.removeAt(counter)
                questionPoolSize = questionPool.size
                val result = db.deleteQuestion(counter+1)
                toast.setText("Question deleted: $result!")
                toast.show()
            }
            counter==0&&questionPoolSize>1 ->{
                questionPool.removeAt(counter)
                questionPoolSize = questionPool.size
                val result = db.deleteQuestion(counter+1)
                toast.setText("Question deleted: $result!")
                toast.show()
                currentQuestion = questionPool[counter]
                updateQuestion(currentQuestion)
            }
            else ->{
                questionPool.removeAt(counter)
                questionPoolSize = questionPool.size
                val result = db.deleteQuestion(counter+1)
                toast.setText("Question deleted: $result!")
                toast.show()
                counter-=1
                currentQuestion = questionPool[counter]
                updateQuestion(currentQuestion)

            }
        }
    }
}