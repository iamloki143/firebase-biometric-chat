# Firebase Biometric Chat App

A secure real-time Android chat application built using **Firebase**, **Jetpack Compose**, and **Biometric Authentication**.  
This project focuses on modern Android development practices with authentication, real-time messaging, local storage, dependency injection, and biometric security.

---

##  Features

-  Biometric Authentication (Fingerprint / Face Unlock)
-  Firebase Authentication
-  Real-time Chat using Firestore
-  MVVM Architecture
-  Hilt Dependency Injection
-  Room Database for local storage
-  Live updates with Snapshot Listener
-  Modern UI with Jetpack Compose
-  Secure Login Flow
-  Clean and Responsive UI

---

##  Tech Stack

- Kotlin
- Jetpack Compose
- Firebase Authentication
- Firebase Firestore
- Room Database
- Hilt
- Coroutines
- Flow / StateFlow
- Biometric API
- MVVM Architecture

---

##  App Flow

```text
App Open
   ↓
Biometric Authentication
   ↓
Login / Signup
   ↓
Chat List Screen
   ↓
Real-Time Messaging
```

---

##  Screenshots

> Add your screenshots here

```md
![Login Screen](screenshots/login.png)
![Chat Screen](screenshots/chat.png)
![Biometric Screen](screenshots/biometric.png)
```

---

##  Project Structure

```text
com.loki.chatapp
│
├── data
│   ├── local
│   ├── remote
│   └── repository
│
├── di
│
├── ui
│   ├── auth
│   ├── chat
│   └── components
│
├── viewmodel
│
├── utils
│
└── MyApp.kt
```

---

##  Firebase Setup

1. Go to Firebase Console
2. Create a new project
3. Enable:
   - Authentication
   - Firestore Database
4. Download `google-services.json`
5. Place it inside:

```text
app/google-services.json
```

---

##  Installation

### Clone Repository

```bash
git clone https://github.com/iamloki143/firebase-biometric-chat.git
```

### Open in Android Studio

- Open the project
- Sync Gradle
- Connect Firebase
- Run the app

---

##  Dependencies

```kotlin
implementation("androidx.biometric:biometric:1.1.0")

implementation(platform("com.google.firebase:firebase-bom:33.1.0"))
implementation("com.google.firebase:firebase-auth-ktx")
implementation("com.google.firebase:firebase-firestore-ktx")

implementation("com.google.dagger:hilt-android:2.57.1")
ksp("com.google.dagger:hilt-compiler:2.57.1")
```

---

##  Learning Concepts Used

- Firebase Real-Time Updates
- Snapshot Listener
- State Management
- Dependency Injection
- Secure Authentication
- Offline Persistence
- Clean Architecture
- Reactive UI

---

##  Future Improvements

-  Push Notifications
-  Media Sharing
-  Online/Offline Status
-  Typing Indicator
-  End-to-End Encryption
-  Group Chats

---
