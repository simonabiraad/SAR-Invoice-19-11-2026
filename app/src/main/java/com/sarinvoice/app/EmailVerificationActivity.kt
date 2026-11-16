package com.sarinvoice.app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import android.app.AlertDialog

class EmailVerificationActivity : ComponentActivity() {
    private lateinit var auth: AuthManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email_verification)
        auth = AuthManager(this)

        val email = intent.getStringExtra("email") ?: auth.getCurrentUserEmail() ?: ""
        val etOtp = findViewById<EditText>(R.id.etOtpCode)

        Toast.makeText(this, "Verification Code sent to $email (Test code: 123456)", Toast.LENGTH_LONG).show()

        findViewById<Button>(R.id.btnVerify).setOnClickListener {
            val code = etOtp.text.toString()
            if (code.isBlank()) {
                Toast.makeText(this, "Please enter the verification code", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val result = auth.verifyOtp(email, code)
            when (result) {
                "SUCCESS" -> {
                    AlertDialog.Builder(this)
                        .setTitle("Success")
                        .setMessage("Email verified successfully!")
                        .setPositiveButton("OK – Done") { _, _ ->
                            startActivity(Intent(this, DashboardActivity::class.java))
                            finish()
                        }
                        .setCancelable(false)
                        .show()
                }
                "EXPIRED" -> {
                    Toast.makeText(this, "Verification code has expired (10 min limit). Please resend a new code.", Toast.LENGTH_LONG).show()
                }
                "ALREADY_USED" -> {
                    Toast.makeText(this, "This verification code has already been used.", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    Toast.makeText(this, "Invalid verification code. Try 123456", Toast.LENGTH_SHORT).show()
                }
            }
        }

        findViewById<Button>(R.id.btnResend).setOnClickListener {
            val res = auth.resendOtp(email)
            if (res == "COOLDOWN") {
                Toast.makeText(this, "Please wait 60 seconds before requesting another code.", Toast.LENGTH_SHORT).show()
            } else if (res != "ERROR") {
                Toast.makeText(this, "New verification code sent to $email ($res)", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Error resending code", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
