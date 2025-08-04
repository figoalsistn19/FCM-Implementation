package com.gox.fcmimplementation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.google.firebase.messaging.FirebaseMessaging
import com.gox.fcmimplementation.ui.theme.FCMImplementationTheme
import dagger.hilt.android.AndroidEntryPoint
import java.util.UUID

data class NavigationEvent(
    val destination: String,
    val id: UUID = UUID.randomUUID()
)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val TAG = "FCM_INTENT_DEBUG"

    // Ubah state untuk menyimpan NavigationEvent, bukan String
    private val navigationEventState = mutableStateOf<NavigationEvent?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseMessaging.getInstance().token.addOnSuccessListener {
            Log.d(TAG, "Token FCM: $it")
        }
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
        Log.d(TAG, "handleIntent dipanggil.")

        // Cek apakah intent dan ekstranya tidak null
        if (intent?.extras != null) {
            // Loop semua isi 'extras' untuk melihat apa saja yang dikirim
            for (key in intent.extras!!.keySet()) {
                val value = intent.extras!!.get(key)
                Log.d(TAG, "Extra Ditemukan -> Key: $key, Value: $value")
            }
        } else {
            Log.d(TAG, "Intent atau extras bernilai null.")
            return
        }

        // Proses data spesifik yang kita cari
        intent.getStringExtra("action_click")?.let { destination ->
            Log.d(TAG, "✅ 'action_click' berhasil dibaca, nilainya: $destination")
            if (destination.isNotBlank()) {
                navigationEventState.value = NavigationEvent(destination)
                intent.removeExtra("action_click")
            }
        } ?: Log.d(TAG, "❌ 'action_click' tidak ditemukan di dalam extras.")
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