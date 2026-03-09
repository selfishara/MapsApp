package com.example.mapsapp.features.students.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mapsapp.features.students.StudentsViewModel

@Composable
fun StudentDetailScreen(
    studentId: String,
    navigateBack: () -> Unit
) {
    val vm: StudentsViewModel = viewModel()

    // Cargar datos 1 vez por id
    LaunchedEffect(studentId) {
        vm.clearSelectedStudent()
        vm.getStudent(studentId)
    }

    val studentName by vm.studentName.observeAsState("")
    val studentMark by vm.studentMark.observeAsState("")

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = studentName,
            onValueChange = { vm.editStudentName(it) },
            label = { Text("Name") }
        )

        Spacer(Modifier.height(12.dp))

        TextField(
            value = studentMark,
            onValueChange = { vm.editStudentMark(it) },
            label = { Text("Mark") }
        )

        Spacer(Modifier.height(16.dp))

        Button(onClick = {
            vm.updateStudent(studentId, studentName, studentMark)
            navigateBack()
        }) {
            Text("Update")
        }
    }
}