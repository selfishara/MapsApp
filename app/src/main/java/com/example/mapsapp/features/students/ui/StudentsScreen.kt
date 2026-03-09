package com.example.mapsapp.features.students.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mapsapp.features.students.StudentsViewModel
import androidx.compose.foundation.background
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.ui.unit.dp

@Composable
fun StudentsScreen(navigateToDetail: (String) -> Unit) {
    val vm: StudentsViewModel = viewModel()

    val studentName by vm.studentName.observeAsState("")
    val studentMark by vm.studentMark.observeAsState("")
    val studentsList by vm.studentsList.observeAsState(emptyList())

    LaunchedEffect(Unit) {
        vm.getAllStudents()
    }

    Column(Modifier.fillMaxSize()) {

        // Form (arriba)
        Column(
            Modifier.fillMaxWidth().weight(0.4f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Create new student", fontSize = 28.sp, fontWeight = FontWeight.Bold)

            TextField(
                value = studentName,
                onValueChange = { vm.editStudentName(it) },
                label = { Text("Name") }
            )

            TextField(
                value = studentMark,
                onValueChange = { vm.editStudentMark(it) },
                label = { Text("Mark") }
            )

            Button(onClick = { vm.insertNewStudent(studentName, studentMark) }) {
                Text("Insert")
            }
        }

        // Lista (abajo)
        LazyColumn(
            Modifier.fillMaxWidth().weight(0.6f)
        ) {
            items(
                items = studentsList,
                key = {
                    it.id ?: it.hashCode()
                } // ✅ importante para que Compose no se líe al borrar
            ) { student ->

                val dismissState = rememberSwipeToDismissBoxState(
                    confirmValueChange = { value ->
                        if (value == SwipeToDismissBoxValue.EndToStart) {
                            student.id?.let { vm.deleteStudent(it.toString()) }
                            true
                        } else false
                    }
                )

                SwipeToDismissBox(
                    state = dismissState,
                    backgroundContent = {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Red),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete",
                                modifier = Modifier.padding(end = 16.dp),
                                tint = Color.White
                            )
                        }
                    }
                ) {
                    StudentItem(student) {
                        navigateToDetail(student.id.toString())
                    }
                }
            }
        }
    }
}