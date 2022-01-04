package com.example.mathapp.Model

data class Question(val question: String,val questionImage: ByteArray,val topic: String,val wrongAnswer: String,val rightAnswer: String) {
}