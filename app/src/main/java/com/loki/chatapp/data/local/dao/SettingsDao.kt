package com.loki.chatapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.loki.chatapp.data.local.entity.SettingsEntity

@Dao
interface SettingsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSettings(settings: SettingsEntity)

    @Query("SELECT * FROM SettingsEntity WHERE id =0 LIMIT 1")
    suspend fun getSettings(): SettingsEntity?

}