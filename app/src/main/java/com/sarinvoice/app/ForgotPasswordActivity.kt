package com.sarinvoice.app

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity

class ForgotPasswordActivity : ComponentActivity() {
    private lateinit var auth: AuthManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        auth = AuthManager(this)

        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etNewPass = findViewById<EditText>(R.id.etNewPassword)
        val btnReset = findViewById<Button>(R.id.btnReset)

        btnReset.setOnClickListener {
            val email = etEmail.text.toString()
            val newPass = etNewPass.text.toString()
            if (email.isBlank() || newPass.isBlank()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val success = auth.resetPassword(email, newPass)
            if (success) {
                Toast.makeText(this, "Password reset successfully! Please sign in.", Toast.LENGTH_LONG).show()
                finish()
            } else {
                Toast.makeText(this, "Email not found", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
