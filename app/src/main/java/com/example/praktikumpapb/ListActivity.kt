package com.example.praktikumpapb

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.praktikumpapb.ui.theme.PraktikumPAPBTheme
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import com.example.praktikumpapb.entity.Matkul


class ListActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PraktikumPAPBTheme {
                // A surface container using the 'background' color from the theme
                MatkulListScreen()
            }
        }
    }
}


@Composable
fun MatkulListScreen() {
    val db = Firebase.firestore
    var matkulList by remember { mutableStateOf<List<Matkul>?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Mendapatkan data mata kuliah dari Firestore
    LaunchedEffect(Unit) {
        isLoading = true
        db.collection("matkul")
            .get()
            .addOnSuccessListener { result ->
                Log.d("Firestore", "Successfully fetched documents: ${result.size()}")
                if (result.isEmpty) {
                    Log.d("Firestore", "No documents found in 'matkul' collection")
                    errorMessage = "No documents found in 'matkul' collection"
                } else {
                    for (document in result) {
                        Log.d("Firestore", "Document ID: ${document.id}, Data: ${document.data}")
                    }
                    matkulList = result.documents.mapNotNull { document ->
                        try {
                            Matkul(
                                hari = document.getString("hari") ?: "",
                                jam_mulai = document.getString("jam_mulai") ?: "",
                                matkul = document.getString("matkul") ?: "",
                                praktikum = document.getBoolean("praktikum") ?: false,
                                ruang = document.getString("ruang") ?: ""
                            )
                        } catch (e: Exception) {
                            Log.e("Firestore", "Error parsing document ${document.id}: ${e.message}")
                            null
                        }
                    }
                }
                isLoading = false
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Error fetching documents: ${exception.message}")
                errorMessage = "Error fetching documents: ${exception.message}"
                matkulList = emptyList()
                isLoading = false
            }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (isLoading) {
            CircularProgressIndicator()
        } else if (matkulList == null || matkulList!!.isEmpty()) {
            Text(errorMessage ?: "No data found")
        } else {
            LazyColumn {
                items(matkulList!!) { matkul ->
                    OutlinedCardExample(matkul)
                }
            }
        }
    }
}


@Composable
fun OutlinedCardExample(matkul: Matkul) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        border = BorderStroke(1.dp, Color.Black),
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Mata Kuliah: ${matkul.matkul}", style = MaterialTheme.typography.headlineSmall)
            Text(text = "Hari: ${matkul.hari}")
            Text(text = "Jam Mulai: ${matkul.jam_mulai}")
            Text(text = "Ruang: ${matkul.ruang}")
            if (matkul.praktikum) {
                Text(text = "Praktikum: Ya")
            } else {
                Text(text = "Praktikum: Tidak")
            }
        }
    }
}
