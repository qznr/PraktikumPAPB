package com.example.praktikumpapb

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.praktikumpapb.entity.Github
import com.example.praktikumpapb.retrofit.ApiConfig
import com.example.praktikumpapb.ui.theme.PraktikumPAPBTheme
import kotlinx.coroutines.launch
import retrofit2.HttpException

class ProfileActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PraktikumPAPBTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ProfileScreen()
                }
            }
        }
    }
}

@Composable
fun ProfileScreen() {
    val context = LocalContext.current
    var githubUser by remember { mutableStateOf<Github?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            try {
                val response = ApiConfig.getApiService().getUser("qznr")
                githubUser = response
            } catch (e: HttpException) {
                // Handle error, e.g., show an error message
            } catch (e: Exception) {
                // Handle other exceptions
            } finally {
                isLoading = false
            }
        }
    }

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(githubUser?.avatarUrl)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(R.drawable.ic_launcher_foreground),
                contentDescription = "Profile Picture",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape)
            )

            Spacer(Modifier.height(16.dp))

            githubUser?.let { user ->
                DisplayField("Username", user.login)
                DisplayField("Name", user.name ?: "")
                DisplayField("Bio", user.bio ?: "")
                DisplayField("Location", user.location ?: "")
                DisplayField("Company", user.company ?: "")
                DisplayField("Followers", user.followers.toString())
                DisplayField("Following", user.following.toString())
                DisplayField("Public Repos", user.publicRepos.toString())
                DisplayField("Public Gists", user.publicGists.toString())
            }

        }
    }
}

@Composable
fun DisplayField(label: String, value: String) {
    Row {
        Text("$label: ", fontWeight = FontWeight.Bold)
        Text(value)
    }
}