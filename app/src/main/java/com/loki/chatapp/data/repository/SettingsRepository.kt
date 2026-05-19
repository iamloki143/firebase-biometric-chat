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
    suspend fun isDarkTheme(): Boolean {
        return try {
            dao.getSettings()?.isDarkTheme ?: false
        } catch (e: Exception) {
            false
        }
    }

    suspend fun setDarkTheme(enabled: Boolean) {
        val current = dao.getSettings()
        dao.insertSettings(
            SettingsEntity(
                id = 0,
                authEnabled = current?.authEnabled ?: false,
                isDarkTheme = enabled
            )
        )
    }
    suspend fun getLanguage(): String {
        return dao.getSettings()?.language ?: "en"
    }

    suspend fun setLanguage(lang: String) {
        val current = dao.getSettings()

        dao.insertSettings(
            SettingsEntity(
                id = 0,
                authEnabled = current?.authEnabled ?: false,
                isDarkTheme = current?.isDarkTheme ?: false,
                language = lang
            )
        )
    }
}