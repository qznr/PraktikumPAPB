package com.example.praktikumpapb

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.praktikumpapb.ui.theme.PraktikumPAPBTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PraktikumPAPBTheme {
                NameAndNimScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NameAndNimScreen() {
    var name by remember { mutableStateOf("") }
    var submittedNim by remember { mutableStateOf("") }
    var nim by remember { mutableStateOf("") }
    val context = LocalContext.current

    // Button enabled state based on validation
    val isFormValid = name.isNotEmpty() && nim.length == 15

    Scaffold(
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text("Enter Name and NIM")
                }
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            NameField(
                icon = Icons.Filled.Person,
                onNameChanged = { newName -> name = newName }
            )

            Spacer(modifier = Modifier.height(16.dp))

            NimField(
                icon = Icons.Filled.Info,
                nim = nim,
                onNimChanged = { if (it.all { char -> char.isDigit() }) nim = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("Submitted NIM: $submittedNim")

            Spacer(modifier = Modifier.height(8.dp))

            // Preview Button
            Button(
                onClick = {
                    Toast.makeText(context, "Preview - Name: $name, NIM: $nim", Toast.LENGTH_SHORT).show()
                },
                enabled = name.isNotEmpty() && nim.isNotEmpty(),
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Text("Preview")
            }

            // Submit Button
            Button(
                onClick = {
                    submittedNim = nim
                },
                enabled = isFormValid,
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Text("Submit")
            }
        }
    }
}

@Composable
fun NameField(icon: ImageVector, onNameChanged: (String) -> Unit) {
    var name by remember { mutableStateOf("") }

    TextField(
        value = name,
        onValueChange = {
            name = it
            onNameChanged(it)
        },
        label = { Text("Enter your name") },
        leadingIcon = { Icon(imageVector = icon, contentDescription = "Name Icon") }
    )
}

@Composable
fun NimField(icon: ImageVector, nim: String, onNimChanged: (String) -> Unit) {
    TextField(
        value = nim,
        onValueChange = onNimChanged,
        label = { Text("Enter NIM (15 digits)") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        leadingIcon = { Icon(imageVector = icon, contentDescription = "NIM Icon") }
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    PraktikumPAPBTheme {
        NameAndNimScreen()
    }
}