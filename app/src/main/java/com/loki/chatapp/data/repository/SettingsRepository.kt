package com.loki.chatapp.data.repository

import com.loki.chatapp.data.local.dao.SettingsDao
import com.loki.chatapp.data.local.entity.SettingsEntity
import javax.inject.Inject

class SettingsRepository @Inject constructor(
    private val dao: SettingsDao
) {
    suspend fun isAuthEnabled(): Boolean {
        return try {
            dao.getSettings()?.authEnabled ?: false
        } catch (e: Exception) {
            false
        }
    }

    suspend fun setAuthEnable(enable: Boolean) {
        dao.insertSettings(
            SettingsEntity(
                id = 0,
                authEnabled = enable
            )
        )
    }
}