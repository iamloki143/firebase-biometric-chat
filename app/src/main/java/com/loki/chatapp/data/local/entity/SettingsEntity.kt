package com.loki.chatapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.intellij.lang.annotations.Language

@Entity
data class SettingsEntity (
    @PrimaryKey
    val id: Int=0,
    val authEnabled: Boolean = false,
    val isDarkTheme: Boolean = false,
    val language: String = "en"
)