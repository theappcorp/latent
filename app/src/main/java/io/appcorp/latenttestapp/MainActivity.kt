package io.appcorp.latenttestapp

import android.os.Bundle
import android.support.design.widget.BottomSheetDialog
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        redact()

        tv_super_secret.text = UUID.randomUUID().toString().replace("-", "").substring(0..6)

        fab_bio.setOnClickListener { authenticateBio() }
        fab_pin.setOnClickListener { authenticatePin() }
        fab_fp.setOnClickListener { authenticateFp() }
    }

    override fun onResume() {
        super.onResume()

        redact()
    }

    private fun redact() = v_censor.setBackgroundResource(R.drawable.bg_redacted)

    private fun unredact() = v_censor.setBackgroundResource(R.drawable.bg_unredacted)

    private fun authenticateFp() {

    }

    private fun authenticateBio() {

//        Latent().displayBiometricPrompt(this,
//                onAuthenticationSucceeded = { result ->
//                    print(result)
//                    unredact()
//                })

    }

    private fun authenticatePin() {
        val view = layoutInflater.inflate(R.layout.pin_bottom_sheet, null)
        val dialog = BottomSheetDialog(this, R.style.AppBottomSheetDialogTheme)
        dialog.setContentView(view)
        dialog.show()
    }
}
