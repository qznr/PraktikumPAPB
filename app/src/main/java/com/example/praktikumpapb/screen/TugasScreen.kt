package com.example.praktikumpapb.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.praktikumpapb.TugasViewModel
import com.example.praktikumpapb.local.Tugas

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TugasScreen(
    viewModel: TugasViewModel = viewModel()
) {
    var matkul by remember { mutableStateOf("") }
    var detailTugas by remember { mutableStateOf("") }

    val tugas by viewModel.getAllTugas().observeAsState(initial = emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Form Section
        OutlinedTextField(
            value = matkul,
            onValueChange = { matkul = it },
            label = { Text("Mata Kuliah") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        OutlinedTextField(
            value = detailTugas,
            onValueChange = { detailTugas = it },
            label = { Text("Detail Tugas") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        Button(
            onClick = {
                if (matkul.isNotBlank() && detailTugas.isNotBlank()) {
                    viewModel.insert(
                        Tugas(
                            matkul = matkul,
                            detailTugas = detailTugas,
                            selesai = false
                        )
                    )
                    matkul = ""
                    detailTugas = ""
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text("Submit")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Tasks List
        LazyColumn {
            items(tugas) { task ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically // Align items vertically
                    ) {
                        Column(Modifier.weight(1f)) { // Occupy available space
                            Text(
                                text = task.matkul,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = task.detailTugas,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }

                        IconButton(onClick = { viewModel.delete(task) }) {
                            Icon(Icons.Filled.Delete, contentDescription = "Delete")
                        }
                        Checkbox(
                            checked = task.selesai,
                            onCheckedChange = { isChecked ->
                                task.selesai = isChecked
                                viewModel.update(task)
                            }
                        )
                    }
                }
            }
        }
    }
}