package com.loki.chatapp.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.loki.chatapp.data.local.dao.SettingsDao
import com.loki.chatapp.data.local.entity.SettingsEntity

@Database(entities = [SettingsEntity::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun settingsDao(): SettingsDao
}