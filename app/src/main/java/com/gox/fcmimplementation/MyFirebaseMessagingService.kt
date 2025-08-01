package com.gox.fcmimplementation

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyFirebaseMessagingService : FirebaseMessagingService() {

    private val TAG = "FCM_Service"

    /**
     * Dipanggil setiap kali FCM token diperbarui.
     * Tugas utama fungsi ini adalah mengirim token baru ke server Anda.
     */
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "New token received: $token")

        // Di sini Anda akan memanggil fungsi untuk mengirim token ke backend/Firestore
        // Contoh: sendTokenToFirestore(token)
    }

    /**
     * Dipanggil saat pesan diterima dari FCM.
     * Fungsi ini akan berjalan jika aplikasi di foreground, atau jika pesan
     * yang dikirim adalah tipe "data-only" saat aplikasi di background.
     */
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d(TAG, "Message received from: ${remoteMessage.from}")

        // Ekstrak data dari pesan yang masuk
        val notificationPayload = remoteMessage.notification
        val dataPayload = remoteMessage.data

        // Ambil judul & pesan, prioritaskan dari data payload,
        // lalu fallback ke notification payload.
        val title = dataPayload["title"] ?: notificationPayload?.title
        val message = dataPayload["message"] ?: notificationPayload?.body

        // Ambil data navigasi (hanya ada di data payload)
        val deeplink = dataPayload["deeplink"]
        val actionClick = dataPayload["action_click"]
        Log.d(TAG, "This is the payload data title: $title")
        Log.d(TAG, "This is the payload data body: $message")
        Log.d(TAG, "This is the payload data actionClick: $actionClick")
        Log.d(TAG, "This is the payload data deeplink: $deeplink")

        // Pastikan title dan message tidak kosong sebelum membuat notifikasi
        if (title.isNullOrBlank() || message.isNullOrBlank()) {
            Log.e(TAG, "Received notification with empty title or message.")
            return
        }

        // Panggil fungsi untuk membangun dan menampilkan notifikasi
        sendNotification(title, message, actionClick)
    }

    private fun sendNotification(title: String, message: String, actionClick: String?) {
        val intent = Intent(this, MainActivity::class.java).apply {
            action = "NOTIFICATION_CLICK_ACTION_${System.currentTimeMillis()}"
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            putExtra("action_click", actionClick)
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val channelId = getString(R.string.fcm_default_channel) // Pastikan ID ini ada
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        // Selalu buat channel untuk kompatibilitas
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "General Notifications",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground) // Ganti dengan ikon default Anda
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        notificationManager.notify(System.currentTimeMillis().toInt(), notificationBuilder.build())
    }
}

