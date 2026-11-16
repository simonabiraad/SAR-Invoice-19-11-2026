package com.sarinvoice.app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity

class RegisterActivity : ComponentActivity() {
    private lateinit var auth: AuthManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        auth = AuthManager(this)

        val etName = findViewById<EditText>(R.id.etName)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val etConfirm = findViewById<EditText>(R.id.etConfirmPassword)
        val btnRegister = findViewById<Button>(R.id.btnRegister)
        val btnGoogle = findViewById<Button>(R.id.btnGoogle)
        val txtLogin = findViewById<TextView>(R.id.txtLoginLink)

        btnRegister.setOnClickListener {
            val name = etName.text.toString()
            val email = etEmail.text.toString()
            val pass = etPassword.text.toString()
            val confirm = etConfirm.text.toString()

            if (name.isBlank() || email.isBlank() || pass.isBlank()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (pass != confirm) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val otp = auth.register(name, email, pass)
            if (otp.isNotEmpty()) {
                Toast.makeText(this, "Account created! Verification code sent ($otp)", Toast.LENGTH_LONG).show()
                startActivity(Intent(this, EmailVerificationActivity::class.java).putExtra("email", email))
                finish()
            } else {
                Toast.makeText(this, "Email already registered", Toast.LENGTH_SHORT).show()
            }
        }

        btnGoogle.setOnClickListener {
            val gEmail = "google_user@gmail.com"
            auth.register("Google User", gEmail, "google123")
            auth.verifyOtp(gEmail, "123456")
            startActivity(Intent(this, DashboardActivity::class.java))
            finish()
        }

        txtLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}
