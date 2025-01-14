package com.intellinex.jobspot.ui.auth

import android.content.SharedPreferences
import android.os.Bundle
import android.text.InputType
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.intellinex.jobspot.R
import com.intellinex.jobspot.api.resource.SignInResponse
import com.intellinex.jobspot.api.services.AuthService
import com.intellinex.jobspot.api.services.SignInRequest

import io.appwrite.Client
import io.appwrite.services.Account
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

        val retrofit = Retrofit.Builder()
            .baseUrl("http://172.10.0.247:8001/api/")
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
                        Toast.makeText(this@LoginActivity, "Login Successfully!", Toast.LENGTH_LONG).show()
                    }
                }else{
                    Toast.makeText(this@LoginActivity, "Login Failed: ${response.message()}", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<SignInResponse>, t: Throwable) {
                Toast.makeText(this@LoginActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    // Cookie | Session
    private fun saveToken(token: String){
        sharedPreferences.edit().putString("JWT_TOKEN", token).apply()
    }
}