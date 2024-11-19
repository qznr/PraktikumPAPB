package com.example.praktikumpapb

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
//import androidx.compose.material.icons.rounded.*
//import androidx.compose.material.icons.sharp.*
//import androidx.compose.material.icons.twotone.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
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
                    LoginScreen(auth) { isLoggedIn = true }
                } else {
                    MainScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(auth: FirebaseAuth, onLoginSuccess: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current
    val isFormValid = name.isNotEmpty() && password.length == 6

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        // Gradient Circle Background
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(600.dp)
                    .clip(CircleShape)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                                MaterialTheme.colorScheme.background
                            )
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Welcome Message
                Text(
                    text = "Welcome Back!",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Text(
                    text = "Please sign in to continue",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 8.dp, bottom = 32.dp)
                )

                // Login Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(24.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        CustomTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = "Email",
                            icon = Icons.Filled.Person,
                            keyboardType = KeyboardType.Email
                        )

                        CustomTextField(
                            value = password,
                            onValueChange = { if (it.all { char -> char.isDigit() }) password = it },
                            label = "Password",
                            icon = Icons.Filled.Lock,
                            keyboardType = KeyboardType.NumberPassword,
                            isPassword = true
                        )

                        Button(
                            onClick = {
                                if (isFormValid) {
                                    auth.signInWithEmailAndPassword(name + "@student.ub.ac.id", password)
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
                                    Toast.makeText(context, "Please enter valid credentials", Toast.LENGTH_SHORT).show()
                                }
                            },
                            enabled = isFormValid,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = "Sign In",
                                modifier = Modifier.padding(vertical = 4.dp),
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: ImageVector,
    keyboardType: KeyboardType,
    isPassword: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        leadingIcon = {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline
        )
    )
}

// MainScreen and BottomNavigationBar components remain the same
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val navigationItems = listOf(
        NavigationItem(
            title = "Matkul",
            icon = Icons.Outlined.School,
            screen = Screen.Matkul
        ),
        NavigationItem(
            title = "Tugas",
            icon = Icons.Outlined.Assignment,
            screen = Screen.Tugas,
        ),
        NavigationItem(
            title = "Profile",
            icon = Icons.Outlined.AccountCircle,
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