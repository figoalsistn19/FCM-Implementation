package com.portofolio.gox.service;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class FirebaseStorageService {
    public String uploadImage(MultipartFile file) throws IOException {
        Bucket bucket = StorageClient.getInstance().bucket();
        String originalFilename = file.getOriginalFilename();
        assert originalFilename != null;

        String sanitizedFilename = originalFilename.replaceAll("\\s+", "_").replaceAll("[^a-zA-Z0-9._-]", "");

        String fileName = UUID.randomUUID().toString() + "-" + sanitizedFilename;

        Blob blob = bucket.create(fileName, file.getBytes(), file.getContentType());
        return "https://firebasestorage.googleapis.com/v0/b/" + bucket.getName() + "/o/" + fileName + "?alt=media";
    }
}
