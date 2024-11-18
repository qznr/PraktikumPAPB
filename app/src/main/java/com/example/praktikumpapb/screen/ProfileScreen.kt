package com.example.praktikumpapb.screen

import android.util.Log
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
import com.example.praktikumpapb.R
import com.example.praktikumpapb.entity.Github
import com.example.praktikumpapb.retrofit.ApiConfig
import kotlinx.coroutines.launch
import retrofit2.HttpException

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
                // Handle HTTP errors (e.g., API not available, unauthorized)
                Log.e("ProfileScreen", "HTTP Exception: ${e.message}")
                // Consider showing an error message to the user or retrying the request.
            } catch (e: Exception) {
                // Handle other exceptions (e.g., network issues)
                Log.e("ProfileScreen", "Exception: ${e.message}")
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
                placeholder = painterResource(R.drawable.ic_launcher_foreground), // Placeholder
                contentDescription = "Profile Picture",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape)
            )

            Spacer(Modifier.height(16.dp))

            githubUser?.let { user ->  // Use let for null safety
                DisplayField("Username", user.login)
                DisplayField("Name", user.name ?: "") // Handle possibly null fields
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
    Row(
        modifier = Modifier.padding(vertical = 4.dp) // Add some spacing
    ) {
        Text(
            text = "$label: ",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp // Adjust font size as needed
        )
        Text(text = value, fontSize = 16.sp)
    }
}