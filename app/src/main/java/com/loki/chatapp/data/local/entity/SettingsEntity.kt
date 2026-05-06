package com.loki.chatapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SettingsEntity (
    @PrimaryKey
    val id: Int=0,
    val authEnabled: Boolean = false
)