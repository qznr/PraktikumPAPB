package com.example.praktikumpapb

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.praktikumpapb.ui.theme.PraktikumPAPBTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PraktikumPAPBTheme {
                CatScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatScreen() {
    val isDarkTheme = isSystemInDarkTheme()
    var catName by remember { mutableStateOf("") }
    var isCatRegistered by remember { mutableStateOf(false) }
    var submittedNim by remember { mutableStateOf("") }
    var nim by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text("Name your cat!")
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
            if (catName.isNotEmpty()) {
                Text(
                    text = "Meow! My name is $catName",
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            } else {
                Text(
                    text = "Meow!",
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Image(
                painter = painterResource(id = R.drawable.cat),
                contentDescription = "Cat Image",
                modifier = Modifier.height(250.dp),
                contentScale = ContentScale.Fit,
                colorFilter = if (isDarkTheme) {
                    ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
                } else null
            )
            Spacer(modifier = Modifier.height(16.dp))
            NameField { newName ->
                catName = newName
            }

            if (isCatRegistered) {
                TextField(
                    value = nim,
                    onValueChange = {
                        if (it.all { char -> char.isDigit() }) {
                            nim = it
                        }
                    },
                    label = { Text("Enter NIM") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                Text("Submitted NIM: $submittedNim")
            }
            Button(
                onClick = {
                    isCatRegistered = true
                    submittedNim = nim },
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Text("Register Cat")
            }
        }
    }
}

@Composable
fun NameField(onNameChanged: (String) -> Unit) {
    var name by remember { mutableStateOf("") }

    TextField(
        value = name,
        onValueChange = {
            name = it
            onNameChanged(it)
        },
        label = { Text("Enter your cat's name") }
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    PraktikumPAPBTheme {
        CatScreen()
    }
}