package com.loki.chatapp.auth

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.loki.chatapp.R


enum class BiometricType{
    FACE, FINGERPRINT, DEVICE_CREDENTIAL,NONE
}
object DeviceAuthManager {

    fun getBiometricType(context: Context): BiometricType {
        val bm = BiometricManager.from(context)
        val pm = context.packageManager
        val strongOK = bm.canAuthenticate(BIOMETRIC_STRONG) ==BiometricManager.BIOMETRIC_SUCCESS
        val hasFace=Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && pm.hasSystemFeature(
            PackageManager.FEATURE_FACE)
        val hasFingerprint =
            pm.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)
        return when {
            hasFace && strongOK -> BiometricType.FACE
            strongOK && hasFingerprint -> BiometricType.FINGERPRINT
            strongOK -> BiometricType.DEVICE_CREDENTIAL
            else -> BiometricType.NONE
        }
    }
    fun biometricLabel(context: Context): String {
        return when (getBiometricType(context)) {
            BiometricType.FACE              -> "Face Unlock"
            BiometricType.FINGERPRINT       -> "Fingerprint"
            BiometricType.DEVICE_CREDENTIAL -> "Device PIN"
            BiometricType.NONE              -> "Device Lock"
        }
    }
    fun biometricIcon(context: Context): Int {
        return when (getBiometricType(context)) {
            BiometricType.FACE              -> R.drawable.whiteface
            BiometricType.FINGERPRINT       -> R.drawable.whitefprint
            else                            -> R.drawable.whitelock
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