package com.example.studentregistration

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.studentregistration.db.Student
import com.example.studentregistration.db.StudentDatabase


class MainActivity : AppCompatActivity() {
    private lateinit var nameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var clearButton: Button

    //instance of view model
    private lateinit var viewModel: StudentViewModel

    //refrence variable for recycler view
    private lateinit var studentRecyclerView: RecyclerView
    private lateinit var adapter: StudentRecyclerViewAdapter

    private lateinit var selectedStudent: Student

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        nameEditText=findViewById(R.id.etName)
        emailEditText=findViewById(R.id.etEmail)
        saveButton=findViewById(R.id.btnSave)
        clearButton=findViewById(R.id.btnClear)
        studentRecyclerView=findViewById(R.id.rvStudent)

        //instance of dao
        val dao = StudentDatabase.getInstance(application).studentDao()
        val factory = StuduentViewModelFactory(dao)
        viewModel = ViewModelProvider(this, factory).get(StudentViewModel::class.java)// Corrected line

        saveButton.setOnClickListener{
            saveStudentData()
            claerInput()
        }
        clearButton.setOnClickListener{
            claerInput()
        }
        initRecyclerView()
    }

    //funcion for saving the data
    private fun saveStudentData(){
        //this also will work
//        val name = nameEditText.text.toString()
//        val email=emailEditText.text.toString()
//        val student = Student(0,name,email)
//        viewModel.insertStudent(student)

        //but this is a consise code han the above
        viewModel.insertStudent(
            Student(
                0,
                nameEditText.text.toString(),
                emailEditText.text.toString()
            )
        )
    }

    //instance for clear the data
    private fun claerInput(){
        nameEditText.setText("")
        emailEditText.setText("")
    }

    private fun initRecyclerView(){
        studentRecyclerView.layoutManager = LinearLayoutManager(this)
        adapter = StudentRecyclerViewAdapter{
            selectedItem:Student -> listItemClicked(selectedItem)
        }
        studentRecyclerView.adapter = this.adapter
        diaplaySudentList()

    }

    private fun diaplaySudentList(){
        viewModel.students.observe( this, {
            adapter.setList(it)
            adapter.notifyDataSetChanged()
        })
    }

    private fun listItemClicked(student: Student){
        Toast.makeText(
            this,
            "Student name is ${student.name}",
            Toast.LENGTH_LONG
        ).show()
    }

}