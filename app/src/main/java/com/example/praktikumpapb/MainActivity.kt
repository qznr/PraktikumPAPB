package com.example.praktikumpapb

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import android.util.Log
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.praktikumpapb.navigation.NavigationItem
import com.example.praktikumpapb.navigation.Screen
import com.example.praktikumpapb.screen.MatkulListScreen
import com.example.praktikumpapb.screen.ProfileScreen
import com.example.praktikumpapb.screen.TugasScreen
import com.example.praktikumpapb.ui.theme.PraktikumPAPBTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth

        enableEdgeToEdge()
        setContent {
            PraktikumPAPBTheme {
                var isLoggedIn by remember { mutableStateOf(false) }

                if (!isLoggedIn) {
                    NameAndNimScreen(auth) { isLoggedIn = true }
                } else {
                    MainScreen()
                }
            }
        }
    }
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val navigationItems = listOf(
        NavigationItem(
            title = "Matkul",
            icon = Icons.Default.Info,
            screen = Screen.Matkul
        ),
        NavigationItem(
            title = "Tugas",
            icon = Icons.Default.Favorite,
            screen = Screen.Tugas,
        ),
        NavigationItem(
            title = "Profile",
            icon = Icons.Default.Person,
            screen = Screen.Profile
        )
    )

    Scaffold(
        bottomBar = { BottomNavigationBar(navController, navigationItems) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Matkul.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Matkul.route) { MatkulListScreen() }
            composable(Screen.Tugas.route) { TugasScreen() }
            composable(Screen.Profile.route) { ProfileScreen() }
        }
    }
}

@Composable
fun BottomNavigationBar(
    navController: NavHostController,
    items: List<NavigationItem>
) {
    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.title) },
                label = { Text(item.title) },
                selected = currentRoute == item.screen.route,
                onClick = {
                    navController.navigate(item.screen.route) {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun NameAndNimScreen(auth: FirebaseAuth, onLoginSuccess: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var nim by remember { mutableStateOf("") }
    val context = LocalContext.current
    val isFormValid = name.isNotEmpty() && nim.length == 6

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

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    if (isFormValid) {
                        auth.signInWithEmailAndPassword(name + "@student.ub.ac.id", nim)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    onLoginSuccess()
                                } else {
                                    Log.w("MainActivity", "signInWithEmail:failure", task.exception)
                                    Toast.makeText(
                                        context,
                                        "Authentication failed.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                    } else {
                        Toast.makeText(context, "Please enter valid name and password.", Toast.LENGTH_SHORT).show()
                    }
                },
                enabled = isFormValid,
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Text("Login")
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
        label = { Text("Enter Password") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        leadingIcon = { Icon(imageVector = icon, contentDescription = "Password Icon") }
    )
}