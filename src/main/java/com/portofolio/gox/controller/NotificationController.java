package com.portofolio.gox.controller;

import com.portofolio.gox.model.FcmNotificationRequestDto;
import com.portofolio.gox.firebase.FcmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class NotificationController {

    private final FcmService fcmService;

    @Autowired
    public NotificationController(FcmService fcmService) {
        this.fcmService = fcmService;
    }

    @PostMapping("/send-to-device")
    public ResponseEntity<String> sendNotification(@RequestBody FcmNotificationRequestDto request) {
        // Dalam aplikasi nyata, validasi request di sini
        if (request.getTargetDeviceToken() == null || request.getTargetDeviceToken().isEmpty()) {
            return ResponseEntity.badRequest().body("Target device token is required.");
        }
        if (request.getTitle() == null || request.getBody() == null) {
            return ResponseEntity.badRequest().body("Title and body are required.");
        }

        String response = fcmService.sendNotificationToDevice(request);
        if (response.startsWith("Error")) {
            return ResponseEntity.internalServerError().body(response);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/send-to-topic")
    public ResponseEntity<String> sendToTopic(@RequestParam String topic) {
        String serverKey = "BFf9t9nB41wZLFM6KRAV67z5gHavgHans1J0MWvUnjqTpwkzBDZv4tksE3kgwNIZ9-6wMEaruNXsa3BTOxghpV0"; // dari Firebase Console > Project settings > Cloud Messaging
        String fcmUrl = "https://fcm.googleapis.com/fcm/send";

        String jsonPayload = """
    {
      "to": "/topics/%s",
      "notification": {
        "title": "Diskon Menarik!",
        "body": "Cek sekarang juga promo terbaru kami!"
      },
      "data": {
        "deeplink": "myapp://promo?id=123"
      }
    }
    """.formatted(topic);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "key=" + serverKey);

        HttpEntity<String> request = new HttpEntity<>(jsonPayload, headers);
        RestTemplate restTemplate = new RestTemplate();

        return restTemplate.postForEntity(fcmUrl, request, String.class);
    }
//    @Autowired
//    private FcmService fcmService;
//
//    @PostMapping("/send-fcm")
//    public String sendNotification(
//            @RequestParam String token,
//            @RequestParam String title,
//            @RequestParam String body,
//            @RequestParam String deeplink) {
//        return fcmService.sendNotification(token, title, body, deeplink);
//    }

}
