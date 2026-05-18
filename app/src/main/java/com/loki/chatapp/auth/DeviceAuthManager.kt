package com.loki.chatapp.auth

import android.content.Context
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity

object DeviceAuthManager {
    fun biometricLabel(context: Context): String {
        val bm = BiometricManager.from(context)
        return when {
            bm.canAuthenticate(BIOMETRIC_STRONG) ==
                    BiometricManager.BIOMETRIC_SUCCESS -> "Fingerprint"
            else -> "Device PIN"
        }
    }
    fun isAvailable(context: Context): Boolean {
        val bm = BiometricManager.from(context)
        return bm.canAuthenticate(BIOMETRIC_STRONG or DEVICE_CREDENTIAL) ==
                BiometricManager.BIOMETRIC_SUCCESS
    }
    fun authenticate(
        activity: FragmentActivity,
        reason: String,
        onResult: (success: Boolean) -> Unit
    ) {
        val executor = ContextCompat.getMainExecutor(activity)

        val callback = object : BiometricPrompt.AuthenticationCallback() {

            override fun onAuthenticationSucceeded(
                result: BiometricPrompt.AuthenticationResult
            ) {
                onResult(true)
            }

            override fun onAuthenticationError(
                errorCode: Int,
                errString: CharSequence
            ) {
                onResult(false)
            }

            override fun onAuthenticationFailed() {
            }
        }

        val prompt = BiometricPrompt(activity, executor, callback)

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("ChatApp")
            .setSubtitle(reason)
            .setAllowedAuthenticators(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)
            .build()

        prompt.authenticate(promptInfo)
    }
}