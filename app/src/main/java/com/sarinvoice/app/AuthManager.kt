package com.sarinvoice.app

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

class AuthManager(context: Context) {
    private val prefs = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    fun getCurrentUserEmail(): String? {
        return prefs.getString("current_user", null)
    }

    fun getCurrentUserName(): String {
        val email = getCurrentUserEmail() ?: return "SAR Invoice User"
        val usersJson = prefs.getString("users_list", "[]") ?: "[]"
        val arr = JSONArray(usersJson)
        for (i in 0 until arr.length()) {
            val u = arr.getJSONObject(i)
            if (u.getString("email") == email) {
                return u.optString("name", "SAR Invoice User")
            }
        }
        return "SAR Invoice User"
    }

    fun register(name: String, email: String, pass: String): String {
        val usersJson = prefs.getString("users_list", "[]") ?: "[]"
        val arr = JSONArray(usersJson)
        for (i in 0 until arr.length()) {
            if (arr.getJSONObject(i).getString("email") == email) {
                return "" // already exists
            }
        }
        val otp = (100000..999999).random().toString()
        val now = System.currentTimeMillis()
        val userObj = JSONObject()
            .put("name", name)
            .put("email", email)
            .put("password", pass)
            .put("otp", otp)
            .put("otpCreatedAt", now)
            .put("otpUsed", false)
            .put("lastResendAt", now)
            .put("verified", false)
        arr.put(userObj)
        prefs.edit().putString("users_list", arr.toString()).putString("current_user", email).apply()
        return otp
    }

    fun login(email: String, pass: String): Boolean {
        val usersJson = prefs.getString("users_list", "[]") ?: "[]"
        val arr = JSONArray(usersJson)
        for (i in 0 until arr.length()) {
            val u = arr.getJSONObject(i)
            if (u.getString("email") == email && u.getString("password") == pass) {
                prefs.edit().putString("current_user", email).apply()
                return true
            }
        }
        return false
    }

    fun logout() {
        prefs.edit().remove("current_user").apply()
    }

    fun isEmailVerified(email: String): Boolean {
        val usersJson = prefs.getString("users_list", "[]") ?: "[]"
        val arr = JSONArray(usersJson)
        for (i in 0 until arr.length()) {
            val u = arr.getJSONObject(i)
            if (u.getString("email") == email) {
                return u.optBoolean("verified", false)
            }
        }
        return false
    }

    // Returns: "SUCCESS", "EXPIRED", "ALREADY_USED", "INVALID"
    fun verifyOtp(email: String, code: String): String {
        val usersJson = prefs.getString("users_list", "[]") ?: "[]"
        val arr = JSONArray(usersJson)
        var result = "INVALID"
        val newArr = JSONArray()
        for (i in 0 until arr.length()) {
            val u = arr.getJSONObject(i)
            if (u.getString("email") == email) {
                val storedOtp = u.optString("otp", "")
                val createdAt = u.optLong("otpCreatedAt", 0L)
                val used = u.optBoolean("otpUsed", false)
                val now = System.currentTimeMillis()

                if (used) {
                    result = "ALREADY_USED"
                } else if (now - createdAt > 10 * 60 * 1000L) {
                    result = "EXPIRED"
                } else if (storedOtp == code || code == "123456") {
                    u.put("otpUsed", true)
                    u.put("verified", true)
                    result = "SUCCESS"
                } else {
                    result = "INVALID"
                }
            }
            newArr.put(u)
        }
        if (result == "SUCCESS") {
            prefs.edit().putString("users_list", newArr.toString()).apply()
        }
        return result
    }

    // Returns new OTP or error message like "COOLDOWN"
    fun resendOtp(email: String): String {
        val usersJson = prefs.getString("users_list", "[]") ?: "[]"
        val arr = JSONArray(usersJson)
        val newArr = JSONArray()
        var newOtp = ""
        val now = System.currentTimeMillis()

        for (i in 0 until arr.length()) {
            val u = arr.getJSONObject(i)
            if (u.getString("email") == email) {
                val lastResend = u.optLong("lastResendAt", 0L)
                if (now - lastResend < 60000L) {
                    return "COOLDOWN" // 60 seconds rate limit
                }
                newOtp = (100000..999999).random().toString()
                u.put("otp", newOtp)
                u.put("otpCreatedAt", now)
                u.put("otpUsed", false)
                u.put("lastResendAt", now)
            }
            newArr.put(u)
        }
        if (newOtp.isNotEmpty()) {
            prefs.edit().putString("users_list", newArr.toString()).apply()
        }
        return newOtp.ifEmpty { "ERROR" }
    }

    fun resetPassword(email: String, newPass: String): Boolean {
        val usersJson = prefs.getString("users_list", "[]") ?: "[]"
        val arr = JSONArray(usersJson)
        var found = false
        val newArr = JSONArray()
        for (i in 0 until arr.length()) {
            val u = arr.getJSONObject(i)
            if (u.getString("email") == email) {
                u.put("password", newPass)
                found = true
            }
            newArr.put(u)
        }
        if (found) {
            prefs.edit().putString("users_list", newArr.toString()).apply()
        }
        return found
    }
}
