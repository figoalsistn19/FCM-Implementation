# FCM-Implementation

This repository is a sample implementation of **Firebase Cloud Messaging (FCM)** for Android applications. This project demonstrates how to set up and receive push notifications from Firebase.

---

## 🚀 Setup Steps

To run this project correctly, please follow the steps below.

### 1. Firebase Configuration

* Open your project in the [Firebase Console](https://console.firebase.google.com/).
* Download the `google-services.json` configuration file from your project settings.
* Copy the `google-services.json` file and place it inside the **`app/`** directory of your Android project.

The folder structure will look like this:

- YourProject/
  - app/
    - src/
    - build.gradle
    - google-services.json  <-- Place it here

### 2. Enable Notification Permissions

* After the application is successfully built and installed on your device.
* Open **Settings** on your Android device.
* Go to the **Apps** or **Application Management** menu.
* Find and select this application (**FCM-Implementation**).
* Select **Notifications**.
* **Enable** or **allow** all notification permissions for this app.

---

## 📝 Important Note

On modern Android versions (Android 13 and newer), the permission to display notifications (**POST_NOTIFICATIONS**) is not granted automatically. The user must grant it manually. If this permission is not enabled, the app **will not be able** to receive or display push notifications from FCM.
