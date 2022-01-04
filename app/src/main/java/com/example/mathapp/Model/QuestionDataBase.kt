package com.example.mathapp.Model

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.sql.SQLException
var ver = 2
class QuestionDataBase(context: Context) : SQLiteOpenHelper(context,"MathApp.db",null, ver) {

    //Question Table
    val QuestionTableName = "Questions"
    val QuestionColumn_ID = "ID"
    val Column_Question = "Question"
    val Column_QuestionImage = "QuestionImage"
    val Column_Topic = "Topic"
    val Column_CorrectAnswer = "CorrectAnswer"
    val Column_WrongAnswer = "WrongAnswer"
    //Users Table
    val UserTableName = "Users"
    val UserColumn_ID = "ID"
    val Column_Name = "Username"
    val Column_Score = "Score"
    val Column_Date = "Date"

    override fun onCreate(db: SQLiteDatabase?) {
        try{
            var sqlCreateStatement: String = "CREATE TABLE " + QuestionTableName + " ( "+
                    QuestionColumn_ID + " INTEGER PRIMARY KEY, " +
                    Column_Question + " TEXT NOT NULL, " +
                    Column_QuestionImage + " TEXT NOT NULL, " +
                    Column_Topic + " TEXT NOT NULL, " +
                    Column_CorrectAnswer + " TEXT NOT NULL, " +
                    Column_WrongAnswer + " TEXT NOT NULL )"
            db?.execSQL(sqlCreateStatement)
            sqlCreateStatement ="CREATE TABLE " + UserTableName + " ( "+
                    UserColumn_ID + " INTEGER PRIMARY KEY" +
                    Column_Name + " TEXT NOT NULL, " +
                    Column_Score + " TEXT NOT NULL, " +
                    Column_Date + " TEXT NOT NULL )"
            db?.execSQL(sqlCreateStatement)
        }catch(e: SQLException){}
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        try{
            var sqlCreateStatement ="CREATE TABLE " + UserTableName + " ( "+
                    UserColumn_ID + " INTEGER PRIMARY KEY, " +
                    Column_Name + " TEXT NOT NULL, " +
                    Column_Score + " TEXT NOT NULL, " +
                    Column_Date + " TEXT NOT NULL )"
            db?.execSQL(sqlCreateStatement)
        }catch(e: SQLException){}
    }

    fun getAllQuestions(): ArrayList<Question>{
        val questionList = ArrayList<Question>()
        val db: SQLiteDatabase = this.readableDatabase
        val sqlStatement = "SELECT * FROM $QuestionTableName"

        val cursor: Cursor = db.rawQuery(sqlStatement,null)

        if(cursor.moveToFirst())
            do{
                val question: String = cursor.getString(1)
                val questionImage: ByteArray = cursor.getBlob(2)
                val topic: String = cursor.getString(3)
                val correctAnswer: String = cursor.getString(4)
                val wrongAnswer:String = cursor.getString(5)

                val completeQuestion = Question(question,questionImage,topic,wrongAnswer,correctAnswer)
                questionList.add(completeQuestion)
            }while(cursor.moveToNext())

            cursor.close()
        db.close()
        return questionList
    }
    fun addQuestion(question: Question): Boolean{
        val db: SQLiteDatabase = this.writableDatabase
        val cv = ContentValues()

        cv.put(Column_Question,question.question)
        cv.put(Column_QuestionImage,question.questionImage)
        cv.put(Column_Topic,question.topic)
        cv.put(Column_CorrectAnswer,question.rightAnswer)
        cv.put(Column_WrongAnswer,question.wrongAnswer)

        val success = db.insert(QuestionTableName, null, cv)
        db.close()
        return success != -1L
    }
    fun deleteQuestion(id: Int):Boolean{
        val db: SQLiteDatabase = this.writableDatabase
        val result = db.delete(QuestionTableName, "$QuestionColumn_ID = $id", null) == 1
        db.execSQL("UPDATE $QuestionTableName SET $QuestionColumn_ID = ($QuestionColumn_ID - 1) WHERE $QuestionColumn_ID > $id")
        db.close()
        return result
    }
    fun getAllUsers():ArrayList<User>{
        var userList: ArrayList<User> = ArrayList()
        val db: SQLiteDatabase = this.readableDatabase
        val sqlStatement = "SELECT * FROM $UserTableName"

        val cursor: Cursor = db.rawQuery(sqlStatement,null)

        if(cursor.moveToFirst())
            do{
                val username: String = cursor.getString(1)
                val score: String = cursor.getString(2)
                val date: String = cursor.getString(3)

                val completeUser = User(username,score,date)
                userList.add(completeUser)
            }while(cursor.moveToNext())

        cursor.close()
        db.close()
        return userList
    }
    fun addUser(user: User):Boolean{
        val db: SQLiteDatabase = this.writableDatabase
        val cv = ContentValues()

        cv.put(Column_Name,user.username)
        cv.put(Column_Score,user.score)
        cv.put(Column_Date,user.date)
        val success = db.insert(UserTableName, null, cv)
        db.close()
        return success != -1L
    }
}