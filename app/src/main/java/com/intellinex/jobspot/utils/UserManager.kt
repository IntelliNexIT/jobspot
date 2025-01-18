package com.intellinex.jobspot.utils

import com.intellinex.jobspot.api.resource.User

object UserManager {
    var user: User? = null

    fun clearUser() {
        user = null // Clear the user data
        // Optionally, also clear any stored values in SharedPreferences if needed
    }
}