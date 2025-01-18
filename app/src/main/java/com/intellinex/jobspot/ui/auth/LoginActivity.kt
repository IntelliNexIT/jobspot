package com.intellinex.jobspot.ui.auth

import android.content.SharedPreferences
import android.os.Bundle
import android.text.InputType
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.intellinex.jobspot.R
import com.intellinex.jobspot.api.resource.SignInResponse
import com.intellinex.jobspot.api.services.AuthService
import com.intellinex.jobspot.api.services.SignInRequest
import com.intellinex.jobspot.utils.ToastMessage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class LoginActivity : AppCompatActivity() {

    private lateinit var editTextEmail: TextInputEditText
    private lateinit var editTextPassword: TextInputEditText
    private lateinit var buttonShowHidePassword: MaterialButton
    private lateinit var buttonSignIn: MaterialButton
    private lateinit var sharedPreferences: SharedPreferences
    private var isPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        sharedPreferences = getSharedPreferences("MY_PREFS", MODE_PRIVATE)

//        val client = Client(context).setProject("67850f17001aed2879be")

        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPassword = findViewById(R.id.editTextPassword)
        buttonShowHidePassword = findViewById(R.id.buttonShowHidePassword)
        buttonSignIn = findViewById(R.id.buttonLogin)

        buttonShowHidePassword.setOnClickListener {
            togglePasswordVisible()
        }

        buttonSignIn.setOnClickListener {
            sigin()
        }

    }

    private fun togglePasswordVisible(){
        if(isPasswordVisible){
            editTextPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD

            buttonShowHidePassword.setIconResource(R.drawable.eye_show)

        }else{
            editTextPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD

            buttonShowHidePassword.setIconResource(R.drawable.eye_hide)
        }

        isPasswordVisible = !isPasswordVisible

        // Move the cursor to the end of the text
        editTextPassword.text?.let { editTextPassword.setSelection(it.length) }
    }

    private fun sigin() {
        val email = editTextEmail.text.toString()
        val password = editTextPassword.text.toString()

        val baseUrl = this.getString(R.string.api_url)

        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val authService = retrofit.create(AuthService::class.java)
        val loginRequest = SignInRequest(email, password)

        authService.signin(loginRequest).enqueue(object : Callback<SignInResponse> {
            override fun onResponse(
                call: Call<SignInResponse>,
                response: Response<SignInResponse>
            ) {
                if(response.isSuccessful){
                    response.body()?.let {
                        saveToken(it.data.access_token)
                        val toastMessage = ToastMessage(this@LoginActivity, "Login Successfully!")
                        toastMessage.startToast()
                        toastMessage.setToastIcon(ContextCompat.getDrawable(this@LoginActivity, R.drawable.success)!!)
                        navigateToHomeFragment() // Navigate to HomeFragment
                    }
                }else{
                    val toastMessage = ToastMessage(this@LoginActivity, "Unable to login! Please try again")
                    toastMessage.startToast()
                    toastMessage.setToastIcon(ContextCompat.getDrawable(this@LoginActivity, R.drawable.failed)!!)
                }
            }

            override fun onFailure(call: Call<SignInResponse>, t: Throwable) {
                val toastMessage = ToastMessage(this@LoginActivity, t.message.toString())
                toastMessage.startToast()
                toastMessage.setToastIcon(ContextCompat.getDrawable(this@LoginActivity, R.drawable.failed)!!)
            }
        })
    }

    // Cookie | Session
    private fun saveToken(token: String){
        sharedPreferences.edit().putString("JWT_TOKEN", token).apply()
    }

    // Function to navigate to HomeFragment
    private fun navigateToHomeFragment() {
        finish() // Close the LoginActivity
    }
}