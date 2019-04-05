package io.appcorp.latenttestapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import io.appcorp.latentlib.Latent
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        redact()

        tv_super_secret.text = UUID.randomUUID().toString().replace("-", "").substring(0..6)

        authenticate()
    }

    override fun onResume() {
        super.onResume()

        redact()

        authenticate()
    }

    private fun redact() = v_censor.setBackgroundResource(R.drawable.bg_redacted)

    private fun unredact() = v_censor.setBackgroundResource(R.drawable.bg_unredacted)

    private fun authenticate() {

        Latent().displayBiometricPrompt(this,
                onAuthenticationSucceeded = { result ->
                    print(result)
                    unredact()
                })
    }
}
