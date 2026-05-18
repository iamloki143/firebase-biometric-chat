# ChatApp

A real-time Android chat application built with **Jetpack Compose**, **Firebase**, and **Clean Architecture**. Features biometric authentication, contact requests, and a modern glassmorphism UI.

---

##  Features

-  **Email / Password Authentication** via Firebase Auth
-  **Biometric Lock** — Fingerprint, Face Unlock, or Device PIN
-  **Real-time Messaging** powered by Firestore
-  **Username Setup** with uniqueness validation
-  **Contact Request System** — Send, receive & accept requests
-  **Dark / Light Theme** with dynamic background
-  **Clean Architecture** — Domain, Data, Presentation layers
-  **Dependency Injection** with Hilt
-  **Local Settings** with Room Database

---

##  Architecture

This project follows **Clean Architecture** with an **MVVM** presentation pattern.

```
┌─────────────────────────────────────┐
│           Presentation Layer        │
│  (Screens, ViewModels, State, Nav)  │
└────────────────┬────────────────────┘
                 │
┌────────────────▼────────────────────┐
│             Domain Layer            │
│     (Use Cases, Models, Repo        │
│          Interfaces)                │
└────────────────┬────────────────────┘
                 │
┌────────────────▼────────────────────┐
│              Data Layer             │
│  (Repository Impl, Firebase, Room)  │
└─────────────────────────────────────┘
```

### Layer Responsibilities

| Layer | Responsibility |
|-------|---------------|
| **Presentation** | Compose UI, ViewModels, Navigation |
| **Domain** | Use Cases, Model classes, Repository interfaces |
| **Data** | Firebase/Room implementations, DAOs, Entities |

---

##  File Structure

```
com.loki.chatapp/
│
├── auth/
│   └── DeviceAuthManager.kt          # Biometric & device credential auth
│
├── data/
│   ├── local/
│   │   ├── dao/
│   │   │   └── SettingsDao.kt        # Room DAO for settings
│   │   ├── database/
│   │   │   └── AppDatabase.kt        # Room database definition
│   │   └── entity/
│   │       └── SettingsEntity.kt     # Settings table entity
│   └── repository/
│       ├── AuthRepositoryImp.kt      # Firebase Auth implementation
│       ├── ChatRepository.kt         # Firestore chat & messages
│       └── SettingsRepository.kt     # Local settings persistence
│
├── di/
│   └── AppModule.kt                  # Hilt dependency injection module
│
├── domain/
│   ├── model/
│   │   ├── Message.kt                # Message data model
│   │   └── User.kt                   # User data model
│   ├── repository/
│   │   └── AuthRepository.kt         # Auth repository interface
│   └── usecase/
│       ├── ListenMessagesUseCase.kt  # Listen to real-time messages
│       ├── LoginUseCase.kt           # Login use case
│       ├── SendMessageUseCase.kt     # Send message use case
│       └── SignupUseCase.kt          # Signup use case
│
├── navigation/
│   ├── AppNavigation.kt              # Root NavHost & lock gate
│   ├── MainScreen.kt                 # Bottom nav scaffold
│   └── Screen.kt                     # Sealed route definitions
│
├── presentation/
│   ├── screen/
│   │   ├── addcontact/
│   │   │   └── AddContactScreen.kt  # Search & add contacts
│   │   ├── authscreen/
│   │   │   ├── AuthScreen.kt        # Login / Sign Up tabs
│   │   │   └── WelcomeScreen.kt     # App entry welcome
│   │   ├── chatscreen/
│   │   │   ├── ChatListScreen.kt    # List of contacts/chats
│   │   │   └── ChatScreen.kt        # Real-time chat view
│   │   ├── lock/
│   │   │   └── LockScreen.kt        # Biometric lock UI
│   │   ├── profilescreen/
│   │   │   └── ProfileScreen.kt     # User profile & settings
│   │   ├── profilesetup/
│   │   │   └── UsernameSetupScreen.kt # Post-signup username
│   │   └── requestscreen/
│   │       └── RequestScreen.kt     # Incoming contact requests
│   ├── state/
│   │   └── AuthState.kt             # Auth UI state sealed class
│   └── viewmodel/
│       ├── AppLockViewModel.kt      # Lock state & auth toggle
│       ├── AuthViewModel.kt         # Login / signup state
│       ├── ChatViewModel.kt         # Messages, contacts, requests
│       └── SettingsViewModel.kt     # App settings toggle
│
├── ui/
│   └── theme/
│       └── BackgroundWrapper.kt     # Dark/light background wrapper
│
├── utils/
│   └── ProfileCircle.kt             # Reusable avatar composable
│
├── MainActivity.kt                   # Entry point (FragmentActivity)
└── MyApp.kt                          # Hilt Application class```

```
##  FireBase Structure
```
Firestore/
│
├── users/{uid}
│   ├── userId: String
│   ├── name: String
│   ├── email: String
│   └── profileImageUrl: String
│
├── chats/{chatId}
│   ├── participants: [uid1, uid2]
│   └── messages/{messageId}
│       ├── senderId: String
│       ├── receiverId: String
│       ├── text: String
│       └── timestamp: Long
│
├── contacts/{uid}
│   └── userList/{contactUid}
│       └── addedAt: Long
│
└── requests/{requestId}
    ├── fromUserId: String
    ├── toUserId: String
    ├── status: "pending" | "accepted"
    └── timestamp: Long
```
##  Tech Stack

| Technology | Usage |
|------------|-------|
| Jetpack Compose | UI framework |
| Firebase Auth | User authentication |
| Cloud Firestore | Real-time database |
| Hilt | Dependency injection |
| Room | Local database |
| Navigation Compose | In-app navigation |
| BiometricPrompt | Biometric authentication |
| Coil | Image loading |
| Kotlin Coroutines | Async operations |
| Kotlin Flow | Reactive state |


##  Getting Started

### Prerequisites

- Android Studio Hedgehog or later
- JDK 17
- A Firebase project

### Setup

1. **Clone the repository**
   ```bash
   git clone https://github.com/your-username/chatapp.git
   cd chatapp
   ```

2. **Connect Firebase**
   - Go to Firebase Console
   - Create a new project and register your Android app
   - Download `google-services.json` and place it in the `app/` directory

3. **Enable Firebase services**
   - Authentication → Email/Password
   - Cloud Firestore → Start in test mode

4. **Build & Run**
   ```bash
   ./gradlew assembleDebug
   ```

---

##  Requirements

- `minSdk`: 24 (Android 7.0)
- `targetSdk`: 36
- `compileSdk`: 36
- Java: 17

---
