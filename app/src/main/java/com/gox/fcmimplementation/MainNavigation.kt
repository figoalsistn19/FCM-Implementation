package com.gox.fcmimplementation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

object AppRoutes {
    const val HOME = "home"
    const val PROFILE = "profile"
    const val SETTINGS = "settings"
}


@Composable
fun MainNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = AppRoutes.HOME // Halaman awal saat aplikasi dibuka
    ) {
        // Mendefinisikan halaman untuk route "home"
        composable(route = AppRoutes.HOME) {
            GenericScreen(screenName = "Home Screen")
        }

        // Mendefinisikan halaman untuk route "profile"
        // Ini adalah route yang bisa Anda panggil dari notifikasi
        composable(route = AppRoutes.PROFILE) {
            GenericScreen(screenName = "Profile Screen")
        }

        // Mendefinisikan halaman untuk route "settings"
        composable(route = AppRoutes.SETTINGS) {
            GenericScreen(screenName = "Settings Screen")
        }
    }
}

/**
 * Composable sederhana untuk menampilkan nama halaman di tengah layar.
 * Digunakan kembali oleh semua halaman untuk contoh ini.
 */
@Composable
fun GenericScreen(screenName: String) {
    Box(
        modifier = Modifier.fillMaxSize().background(Color.Gray),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = screenName,
            fontSize = 24.sp
        )
    }
}