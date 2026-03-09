package com.example.mapsapp.features.students

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mapsapp.MyApp
import com.example.mapsapp.data.model.Student
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StudentsViewModel : ViewModel() {

    private val database = MyApp.database

    private val _studentName = MutableLiveData("")
    val studentName = _studentName

    private val _studentMark = MutableLiveData("")
    val studentMark = _studentMark

    private val _studentsList = MutableLiveData<List<Student>>(emptyList())
    val studentsList = _studentsList

    // ✅ para UPDATE: evitar pedir el mismo student 20 veces
    private var selectedStudentId: String? = null

    fun editStudentName(value: String) { _studentName.value = value }
    fun editStudentMark(value: String) { _studentMark.value = value }

    fun insertNewStudent(name: String, mark: String) {
        val markDouble = mark.toDoubleOrNull() ?: return
        val newStudent = Student(name = name, mark = markDouble)

        viewModelScope.launch(Dispatchers.IO) {
            database.insertStudent(newStudent)
            refreshStudents()
        }
    }

    fun getAllStudents() {
        viewModelScope.launch(Dispatchers.IO) {
            refreshStudents()
        }
    }

    // -------------------------
    // ✅ UPDATE / SELECT 1 / DELETE (PDF)
    // -------------------------

    fun updateStudent(id: String, name: String, mark: String) {
        val markDouble = mark.toDoubleOrNull() ?: return

        viewModelScope.launch(Dispatchers.IO) {
            database.updateStudent(id, name, markDouble)
            // opcional: refrescar lista para ver el cambio al volver
            refreshStudents()
        }
    }

    fun getStudent(id: String) {
        if (selectedStudentId == id) return

        viewModelScope.launch(Dispatchers.IO) {
            val student = database.getStudent(id)
            withContext(Dispatchers.Main) {
                selectedStudentId = id
                _studentName.value = student.name
                _studentMark.value = student.mark.toString()
            }
        }
    }

    fun clearSelectedStudent() {
        selectedStudentId = null
    }

    fun deleteStudent(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            database.deleteStudent(id)
            refreshStudents()
        }
    }

    // -------------------------
    // helper
    // -------------------------
    private suspend fun refreshStudents() {
        val databaseStudents = database.getAllStudents()
        withContext(Dispatchers.Main) {
            _studentsList.value = databaseStudents
        }
    }
}