package com.app.quick_links.View

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.app.quick_links.Controller.RegisterUserActivity
import com.app.quick_links.R
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity()
{
    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var txt_email: String
    private lateinit var txt_password:String

    private lateinit var emailEditText:EditText
    private lateinit var passwordEditText:EditText
    private lateinit var btnLogin: Button

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        FirebaseApp.initializeApp(this)
        firebaseAuth = FirebaseAuth.getInstance()

        emailEditText = findViewById(R.id.edit_text_login_email)
        passwordEditText = findViewById(R.id.edit_text_login_password)
        btnLogin = findViewById(R.id.btn_login)

        btnLogin.setOnClickListener {
            txt_email = emailEditText.text.toString()
            txt_password = passwordEditText.text.toString()

            if ((txt_email.isNotEmpty() || txt_email.isNotBlank()) && (txt_password.isNotEmpty() || txt_password.isNotBlank()))
            {
                loginUser(txt_email, txt_password)
            }
            else
            { Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show() }
        }
    }

    /* Authentication with users registered directed on the app */
    private fun loginUser(email: String, password: String)
    {
        if (email.isNotEmpty() && password.isNotEmpty())
        {
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener{
                if (it.isSuccessful)
                {
                    val intent = Intent(this, LoginActivity::class.java)
                    Toast.makeText(this, "Login Sucsessful!", Toast.LENGTH_SHORT).show()
                    startActivity(intent)
                    finish()
                }
                else
                {
                    Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                    Log.d("Error Login", it.exception.toString())
                }
            }
        }
        else
        {
            Toast.makeText(this, "Empty Fields Are not Allowed!", Toast.LENGTH_SHORT).show()
        }
    }

    fun openRegisterUserPage(view:View?)
    {
        val activity = Intent(this, RegisterUserActivity::class.java)
        startActivity(activity)
    }

}