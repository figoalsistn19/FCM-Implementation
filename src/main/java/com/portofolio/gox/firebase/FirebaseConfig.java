package com.portofolio.gox.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

@Configuration
public class FirebaseConfig {
    @Bean
    public FirebaseApp initializeFirebase() throws IOException{
        // ✅ Cek apakah Firebase sudah diinisialisasi sebelumnya
        List<FirebaseApp> firebaseApps = FirebaseApp.getApps();
        if (!firebaseApps.isEmpty()) {
            return firebaseApps.getFirst(); // Gunakan instance yang sudah ada
        }
        FileInputStream serviceAccount = new FileInputStream("src/main/resources/goxwebsite-firebase-adminsdk-fbsvc-91d9d7ecf1.json");
        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setStorageBucket("goxwebsite.firebasestorage.app")
                .build();
        return FirebaseApp.initializeApp(options);
    }

//    @PostConstruct
//    public void initialize() {
//        try {
//            FileInputStream serviceAccount =
//                    new FileInputStream("src/main/resources/opakuapps-firebase-adminsdk-fbsvc-77075e3d53.json");
//
//            FirebaseOptions options = new FirebaseOptions.Builder()
//                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
//                    .build();
//
//            if (FirebaseApp.getApps().isEmpty()) {
//                FirebaseApp.initializeApp(options);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    @PostConstruct
    public void initializeOpakuEcommerce() {
        try {
            FileInputStream serviceAccount =
                    new FileInputStream("src/main/resources/opakuecommerce-firebase-adminsdk-fbsvc-9a57947813.json");

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
