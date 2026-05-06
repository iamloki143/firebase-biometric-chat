package com.loki.chatapp.navigation

sealed class Screen(val route: String) {
    object Welcome: Screen("welcome")
    object Auth: Screen("auth")
    object ChatList: Screen("chat_list")
    object Chat : Screen("chat/{userId}/{userName}") {
        fun createRoute(userId: String, userName: String) =
            "chat/$userId/$userName"
    }
    object AddContact : Screen("add_contact")
    object Main: Screen("main")
    object Requests: Screen("requests")
    object Profile: Screen("profile")
    object UsernameSetup: Screen("username_setup")
    object ProfileImageSetup : Screen("profile_image_setup")

    object Lock: Screen("lock")
}