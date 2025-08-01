package com.gox.fcmimplementation

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.gox.fcmimplementation.ui.theme.FCMImplementationTheme
import dagger.hilt.android.AndroidEntryPoint
import java.util.UUID

data class NavigationEvent(
    val destination: String,
    val id: UUID = UUID.randomUUID()
)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    // Ubah state untuk menyimpan NavigationEvent, bukan String
    private val navigationEventState = mutableStateOf<NavigationEvent?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handleIntent(intent)
        setContent {
            val navController = rememberNavController()

            LaunchedEffect(navigationEventState.value) {
                navigationEventState.value?.let { event ->
                    navController.navigate(event.destination)
                    navigationEventState.value = null
                }
            }
            MainNavigation(navController = navController)
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent?) {
        intent?.getStringExtra("action_click")?.let { destination ->
            if (destination.isNotBlank()) {
                navigationEventState.value = NavigationEvent(destination)
                intent.removeExtra("action_click")
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FCMImplementationTheme {
        Greeting("Android")
    }
}