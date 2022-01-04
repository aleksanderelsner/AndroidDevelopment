package com.example.mathapp.Model

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mathapp.R

class RecyclerViewAdapter(private val dataSet: ArrayList<User>): RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val username: TextView = view.findViewById(R.id.userName)
        val grade: TextView = view.findViewById(R.id.userGrade)
        val date: TextView = view.findViewById(R.id.userDate)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.listitem, parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.username.text = "Name: " + dataSet[position].username
        holder.grade.text = "Grade: " + dataSet[position].score
        holder.date.text = "Date: " + dataSet[position].date
    }

    override fun getItemCount() = dataSet.size
}