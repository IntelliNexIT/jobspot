package com.intellinex.jobspot.utils

import android.content.SharedPreferences
import android.util.Base64
import org.json.JSONObject

class Authentication(private val sharedPreferences: SharedPreferences) {


    fun isAuthenticated(): Boolean {
        val token = sharedPreferences.getString("JWT_TOKEN", null)
        return token != null && !isTokenExpired(token)
    }

    private fun isTokenExpired(token: String): Boolean {
        // Optionally decode the JWT to check its expiration
        // Example to decode JWT (you can use a library like jwt_decoder)
        val parts = token.split(".")
        if (parts.size != 3) return true // Not a valid JWT

        // Decode the payload (the second part)
        val payload = String(Base64.decode(parts[1], Base64.DEFAULT))
        val jsonObject = JSONObject(payload)
        val exp = jsonObject.getLong("exp") // Get expiration time

        // Check if the current time is past the expiration time
        return exp < System.currentTimeMillis() / 1000
    }
}