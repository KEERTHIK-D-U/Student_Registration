package com.example.studentregistration

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.studentregistration.db.Student

class StudentRecyclerViewAdapter(
    private val clickListener:(Student)->Unit
): RecyclerView.Adapter<StudentViewHolder>() {
    private val studentList = ArrayList<Student>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context) // a layout object to get the list item view
        val listItem = layoutInflater.inflate(R.layout.list_item,parent,false)
        return StudentViewHolder(listItem)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        holder.bind(studentList[position],clickListener)

    }

    override fun getItemCount(): Int {
        return studentList.size
    }

    fun setList(students:List<Student>)
    {
        studentList.clear()
        studentList.addAll(students)
    }

}

class StudentViewHolder(private val view: View):RecyclerView.ViewHolder(view){
    fun bind(student: Student,clickListener:(Student)->Unit){
        val nameTexView = view.findViewById<TextView>(R.id.tvName)
        val emailTextView = view.findViewById<TextView>(R.id.tvEmail)
        nameTexView.text = student.name
        emailTextView.text = student.email
        view.setOnClickListener {
            clickListener(student)
        }
    }
}