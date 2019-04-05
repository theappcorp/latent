package io.appcorp.latentlib

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.hardware.biometrics.BiometricPrompt
import android.os.Build
import android.os.CancellationSignal
import android.support.v4.app.ActivityCompat
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat


class Latent {

    fun displayBiometricPrompt(context: Context,
                               onAuthenticationSucceeded: ((result: BiometricPrompt.AuthenticationResult?) -> Unit)? = null,
                               onAuthenticationFailed: (() -> Unit)? = null,
                               onAuthenticationHelp: ((helpCode: Int, helpString: String?) -> Unit)? = null,
                               onAuthenticationError: ((errorCode: Int, errorString: String?) -> Unit)? = null,
                               onAuthenticationCancel: (() -> Unit)? = null) {
        val cancellationSignal = CancellationSignal()
        cancellationSignal.setOnCancelListener {
            onAuthenticationCancel?.invoke()
        }

        val callback = object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence?)
                    = onAuthenticationError?.invoke(errorCode, errString?.toString()) ?: Unit

            override fun onAuthenticationFailed() = onAuthenticationFailed?.invoke() ?: Unit

            override fun onAuthenticationHelp(helpCode: Int, helpString: CharSequence?)
                    = onAuthenticationHelp?.invoke(helpCode, helpString?.toString()) ?: Unit

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult?)
                    = onAuthenticationSucceeded?.invoke(result) ?: Unit
        }

        BiometricPrompt.Builder(context)
                .setTitle(context.getString(R.string.biometric_title))
                .setSubtitle(context.getString(R.string.biometric_subtitle))
                .setDescription(context.getString(R.string.biometric_description))
                .setNegativeButton(context.getString(R.string.biometric_cancel),
                        context.mainExecutor, DialogInterface.OnClickListener { _, _ -> onAuthenticationCancel?.invoke() })
                .build()
                .authenticate(cancellationSignal, context.mainExecutor, callback)

    }

    private val isBiometricPromptEnabled: Boolean
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.P

    /*
     * Condition I: Check if the device has fingerprint sensors.
     * Note: If you marked android.hardware.fingerprint as something that
     * your app requires (android:required="true"), then you don't need
     * to perform this check.
     *
     * */
    private fun isHardwareSupported(context: Context) = FingerprintManagerCompat.from(context).isHardwareDetected

    /*
     * Condition II: Fingerprint authentication can be matched with a
     * registered fingerprint of the user. So we need to perform this check
     * in order to enable fingerprint authentication
     *
     * */
    private fun isFingerprintAvailable(context: Context) = FingerprintManagerCompat.from(context).hasEnrolledFingerprints()


    /*
     * Condition III: Check if the permission has been added to
     * the app. This permission will be granted as soon as the user
     * installs the app on their device.
     *
     * */
    private fun isPermissionGranted(context: Context) = checkPermission(context, Manifest.permission.USE_FINGERPRINT)


    private fun checkPermission(context: Context, perm: String) = ActivityCompat.checkSelfPermission(context, perm) == PackageManager.PERMISSION_GRANTED
}
