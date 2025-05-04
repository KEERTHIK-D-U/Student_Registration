package com.example.studentregistration

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
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
    private var isListItemClicked = false

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

        saveButton.setOnClickListener {
            if (isListItemClicked) {
                updateStudentData()
                clearInput()
            } else {
                saveStudentData()
                clearInput()
            }
        }
        clearButton.setOnClickListener{
            if(isListItemClicked){
                deleteStudentData()
                clearInput()
            }else {
                clearInput()
            }
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

    private fun updateStudentData(){
        viewModel.updateStudent(
            Student(
                selectedStudent.id,
                nameEditText.text.toString(),
                emailEditText.text.toString()
            )
        )
        //selectedStudent =null
        saveButton.text ="Save"
        clearButton.text ="Clear"
        isListItemClicked = false
    }

    private fun deleteStudentData(){
        viewModel.deleteStudent(
            Student(
                selectedStudent.id,
                nameEditText.text.toString(),
                emailEditText.text.toString()
            )
        )
      //  selectedStudent =null
        saveButton.text ="Save"
        clearButton.text ="Clear"
        isListItemClicked = false
    }

    //instance for clear the data
    private fun clearInput(){
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
//        Toast.makeText(
//            this,
//            "Student name is ${student.name}",
//            Toast.LENGTH_LONG
//        ).show()
        selectedStudent =student
        saveButton.text ="Update"
        clearButton.text ="Delete"
        isListItemClicked = true
        nameEditText.setText(selectedStudent.name)
        emailEditText.setText(selectedStudent.email)
    }

}