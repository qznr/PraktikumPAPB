package com.example.praktikumpapb.screen

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.praktikumpapb.entity.Matkul
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@Composable
fun MatkulListScreen() {
    val db = Firebase.firestore
    var matkulList by remember { mutableStateOf<List<Matkul>?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            // Header
            Text(
                text = "Daftar Mata Kuliah",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp)
            )

            LaunchedEffect(Unit) {
                isLoading = true
                db.collection("matkul")
                    .get()
                    .addOnSuccessListener { result ->
                        Log.d("Firestore", "Successfully fetched documents: ${result.size()}")
                        if (result.isEmpty) {
                            errorMessage = "Tidak ada mata kuliah yang ditemukan"
                        } else {
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
                        errorMessage = "Gagal mengambil data: ${exception.message}"
                        matkulList = emptyList()
                        isLoading = false
                    }
            }

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                when {
                    isLoading -> {
                        CircularProgressIndicator(
                            modifier = Modifier.size(48.dp),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    matkulList == null || matkulList!!.isEmpty() -> {
                        Text(
                            text = errorMessage ?: "Tidak ada data",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                    else -> {
                        LazyColumn(
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(matkulList!!) { matkul ->
                                MatkulCard(matkul)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MatkulCard(matkul: Matkul) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.12f)),
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = matkul.matkul,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    InfoRow("Hari", matkul.hari)
                    InfoRow("Jam", matkul.jam_mulai)
                }
                Column {
                    InfoRow("Ruang", matkul.ruang)
                    Text(
                        text = if (matkul.praktikum) "✓ Praktikum" else "✗ Teori",
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (matkul.praktikum)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.secondary
                    )
                }
            }
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = "$label:",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}