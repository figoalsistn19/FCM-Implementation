package com.portofolio.gox.firebase;

import com.google.firebase.messaging.*;

import com.portofolio.gox.model.FcmNotificationRequestDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

//@Service
//public class FcmService {
//    public String sendNotification(String token, String title, String body, String deeplink) {
//        try {
//            Message message = Message.builder()
//                    .setToken(token)
//                    .setNotification(Notification.builder()
//                            .setTitle(title)
//                            .setBody(body)
//                            .setBody(deeplink)
//                            .build())
//                    .build();
//
//            return FirebaseMessaging.getInstance().send(message);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "Error sending FCM: " + e.getMessage();
//        }
//    }
//}

import com.google.auth.oauth2.GoogleCredentials;
import okhttp3.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;

@Service
public class FcmService {
    private static final Logger logger = LoggerFactory.getLogger(FcmService.class);
    private static final String DEEPLINK_KEY = "deeplink";
    private static final String FCM_URL = "https://fcm.googleapis.com/v1/projects/opakuapps/messages:send";

    private final OkHttpClient client = new OkHttpClient();

    // Ganti path JSON sesuai lokasi file service account
    private final String SERVICE_ACCOUNT_JSON = "src/main/resources/opakuapps-firebase-adminsdk-fbsvc-ea1d34af2e.json";

    public String sendNotificationToTopic(String topic, String title, String body) throws IOException {
        // 1. Ambil akses token
        String accessToken = getAccessToken();

        // 2. Buat payload JSON
        String jsonPayload = """
        {
          "message": {
            "topic": "%s",
            "notification": {
              "title": "%s",
              "body": "%s"
            },
            "data": {
              "deeplink": "myapp://promo"
            }
          }
        }
        """.formatted(topic, title, body);

        // 3. Buat request
        Request request = new Request.Builder()
                .url(FCM_URL)
                .addHeader("Authorization", "Bearer " + accessToken)
                .addHeader("Content-Type", "application/json; UTF-8")
                .post(RequestBody.create(jsonPayload, MediaType.parse("application/json")))
                .build();

        // 4. Kirim request
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    public String sendNotificationToDevice(FcmNotificationRequestDto request) {
        Message message = Message.builder()
                .setToken(request.getTargetDeviceToken()) // Token registrasi perangkat target
                .setNotification(Notification.builder()
                        .setTitle(request.getTitle())
                        .setBody(request.getBody())
                        .build())
                .putData(DEEPLINK_KEY, request.getDeeplinkUrl()) // Menyisipkan deeplink
                // Anda bisa menambahkan data kustom lainnya di sini
                // .putData("utm_campaign", request.getUtmCampaign())
                // .putAllData(request.getCustomDataMap()) // Jika Anda punya Map data kustom
                .setAndroidConfig(AndroidConfig.builder() // Konfigurasi spesifik Android (opsional)
                        .setPriority(AndroidConfig.Priority.HIGH)
                        .setNotification(AndroidNotification.builder()
                                // .setIcon("stock_ticker_update") // Nama resource ikon di drawable Android
                                // .setColor("#FF5733") // Warna ikon notifikasi
                                .setSound("default") // Suara notifikasi default
                                .build())
                        .build())
                // .setApnsConfig(...) // Untuk konfigurasi spesifik iOS (opsional)
                .build();

        String response = null;
        try {
            // Kirim pesan secara sinkron
            response = FirebaseMessaging.getInstance().send(message);
            // Atau kirim secara asinkron
            // ApiFuture<String> future = FirebaseMessaging.getInstance().sendAsync(message);
            // response = future.get(); // Menunggu hasil (blocking)
            logger.info("Successfully sent message: " + response);
        } catch (FirebaseMessagingException e) {
            logger.error("Failed to send FCM message", e);
            // Tangani error spesifik (misalnya, token tidak valid, kuota terlampaui)
            // e.getMessagingErrorCode() bisa memberikan detail lebih lanjut
            if (e.getMessagingErrorCode() == MessagingErrorCode.UNREGISTERED || e.getMessagingErrorCode() == MessagingErrorCode.INVALID_ARGUMENT) {
                // Token mungkin sudah tidak valid, hapus dari database Anda
                logger.warn("Device token {} is no longer valid. Consider removing it.", request.getTargetDeviceToken());
            }
            response = "Error sending message: " + e.getMessage();
        }
        // Jika menggunakan sendAsync, tangani ExecutionException dan InterruptedException
        // catch (InterruptedException | ExecutionException e) {
        //    logger.error("Error sending FCM message asynchronously", e);
        //    response = "Error sending message: " + e.getMessage();
        // }
        return response;
    }

    private String getAccessToken() throws IOException {
        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new FileInputStream(SERVICE_ACCOUNT_JSON))
                .createScoped(Collections.singletonList("https://www.googleapis.com/auth/firebase.messaging"));
        googleCredentials.refreshIfExpired();
        return googleCredentials.getAccessToken().getTokenValue();
    }
}
//@Service
//public class FcmService {
//
//    private static final Logger logger = LoggerFactory.getLogger(FcmService.class);
//    private static final String DEEPLINK_KEY = "deeplink"; // Kunci yang sama dengan di aplikasi Android
//
//    public String sendNotificationToDevice(FcmNotificationRequestDto request) {
//        Message message = Message.builder()
//                .setToken(request.getTargetDeviceToken()) // Token registrasi perangkat target
//                .setNotification(Notification.builder()
//                        .setTitle(request.getTitle())
//                        .setBody(request.getBody())
//                        .build())
//                .putData(DEEPLINK_KEY, request.getDeeplinkUrl()) // Menyisipkan deeplink
//                // Anda bisa menambahkan data kustom lainnya di sini
//                // .putData("utm_campaign", request.getUtmCampaign())
//                // .putAllData(request.getCustomDataMap()) // Jika Anda punya Map data kustom
//                .setAndroidConfig(AndroidConfig.builder() // Konfigurasi spesifik Android (opsional)
//                        .setPriority(AndroidConfig.Priority.HIGH)
//                        .setNotification(AndroidNotification.builder()
//                                // .setIcon("stock_ticker_update") // Nama resource ikon di drawable Android
//                                // .setColor("#FF5733") // Warna ikon notifikasi
//                                .setSound("default") // Suara notifikasi default
//                                .build())
//                        .build())
//                // .setApnsConfig(...) // Untuk konfigurasi spesifik iOS (opsional)
//                .build();
//
//        String response = null;
//        try {
//            // Kirim pesan secara sinkron
//            response = FirebaseMessaging.getInstance().send(message);
//            // Atau kirim secara asinkron
//            // ApiFuture<String> future = FirebaseMessaging.getInstance().sendAsync(message);
//            // response = future.get(); // Menunggu hasil (blocking)
//            logger.info("Successfully sent message: " + response);
//        } catch (FirebaseMessagingException e) {
//            logger.error("Failed to send FCM message", e);
//            // Tangani error spesifik (misalnya, token tidak valid, kuota terlampaui)
//            // e.getMessagingErrorCode() bisa memberikan detail lebih lanjut
//            if (e.getMessagingErrorCode() == MessagingErrorCode.UNREGISTERED || e.getMessagingErrorCode() == MessagingErrorCode.INVALID_ARGUMENT) {
//                // Token mungkin sudah tidak valid, hapus dari database Anda
//                logger.warn("Device token {} is no longer valid. Consider removing it.", request.getTargetDeviceToken());
//            }
//            response = "Error sending message: " + e.getMessage();
//        }
//        // Jika menggunakan sendAsync, tangani ExecutionException dan InterruptedException
//        // catch (InterruptedException | ExecutionException e) {
//        //    logger.error("Error sending FCM message asynchronously", e);
//        //    response = "Error sending message: " + e.getMessage();
//        // }
//        return response;
//    }
//
//    // Anda juga bisa membuat metode untuk mengirim ke topik, atau multiple devices (multicast)
//    // public BatchResponse sendMulticastNotification(List<String> tokens, String title, String body, String deeplink) throws FirebaseMessagingException {
//    //     MulticastMessage message = MulticastMessage.builder()
//    //             .setNotification(Notification.builder().setTitle(title).setBody(body).build())
//    //             .putData(DEEPLINK_KEY, deeplink)
//    //             .addAllTokens(tokens)
//    //             .build();
//    //     return FirebaseMessaging.getInstance().sendEachForMulticast(message);
//    // }
//}
