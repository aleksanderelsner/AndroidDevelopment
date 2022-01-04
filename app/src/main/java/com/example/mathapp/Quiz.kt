package com.example.mathapp

import android.content.Intent
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
import kotlin.random.Random

private var count = 0
private var rightAnswers = 0
private lateinit var wrongAnswers: ArrayList<String>
private lateinit var allQuestions: ArrayList<Question>
private lateinit var quizQuestions: ArrayList<Question>
private lateinit var currentQuestion: Question

class Quiz : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)
        val db = QuestionDataBase(applicationContext)
        allQuestions = db.getAllQuestions()
        wrongAnswers = pullAllWrongAnswers(allQuestions)
        quizQuestions = chooseRandom14Questions(allQuestions)
        currentQuestion = quizQuestions[count]
        displayQuestion(currentQuestion)
    }

    override fun onResume() {
        super.onResume()
        count = 0
        rightAnswers = 0
        quizQuestions = chooseRandom14Questions(allQuestions)
        currentQuestion = quizQuestions[count]
        displayQuestion(currentQuestion)

    }

    private fun chooseRandom14Questions(questions: ArrayList<Question>): ArrayList<Question> {
        val randomQuestions: ArrayList<Question> = ArrayList()
        val topics: HashMap<String,Int> = HashMap()
        questions.forEach {
            topics[it.topic] = 0
        }
        do {
            var questionID = Random.nextInt(0, questions.size)
            if (!randomQuestions.contains(questions[questionID])) {
                if(topics[questions[questionID].topic]!!<2){
                    topics[questions[questionID].topic] = topics[questions[questionID].topic]?.inc()!!
                    randomQuestions.add(questions[questionID])
                }
            }
        } while (randomQuestions.size < 14)
        return randomQuestions
    }

    private fun pullAllWrongAnswers(questions: ArrayList<Question>): ArrayList<String> {
        var wrongAnswers: ArrayList<String> = ArrayList()
        questions.forEach {
            wrongAnswers.add(it.wrongAnswer)
        }
        return wrongAnswers
    }

    private fun displayQuestion(question: Question) {
        var rightAnswer = Random.nextInt(0, 4)
        var buttons: ArrayList<Button> = arrayListOf()
        buttons.add(findViewById(R.id.buttonA))
        buttons.add(findViewById(R.id.buttonB))
        buttons.add(findViewById(R.id.buttonC))
        buttons.add(findViewById(R.id.buttonD))
        buttons.add(findViewById(R.id.buttonE))
        var counter = 0;
        while (counter < 5) {
            var answer = wrongAnswers[Random.nextInt(0, wrongAnswers.size)]
            if (answer != question.rightAnswer) {
                buttons[counter].text = answer
                counter += 1
            }
        }
        buttons[rightAnswer].text = question.rightAnswer
        findViewById<TextView>(R.id.textViewQuestionNumber).text = "Question : ${count + 1}/14"
        findViewById<TextView>(R.id.textViewQuestion).text = question.question
        findViewById<TextView>(R.id.textViewTopic).text = question.topic
        val imageBitMap =
            BitmapFactory.decodeByteArray(question.questionImage, 0, question.questionImage.size)
        val questionImage = findViewById<ImageView>(R.id.imageView)
        questionImage.setImageBitmap(imageBitMap)

    }

    fun buttonPressed(view: View) {
        if (count < 13) {
            val chosenAnswer = (view as Button).text
            if (chosenAnswer == currentQuestion.rightAnswer) {
                rightAnswers += 1
            }
            count += 1
            currentQuestion = quizQuestions[count]
            displayQuestion(currentQuestion)
        } else {
            val intent = Intent(this, QuizFinished::class.java).apply {}
            intent.putExtra("score", rightAnswers)
            startActivity(intent)
            finish()
        }
    }
}