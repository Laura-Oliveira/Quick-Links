package com.app.fave_stores.Controller
import com.app.fave_stores.R

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.app.fave_stores.Model.UserApp
import com.app.fave_stores.View.LoginActivity
import com.google.firebase.FirebaseApp.initializeApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterUserActivity : AppCompatActivity()
{
    /* Get firebase database instance */
    private val databaseInstance: DatabaseReference = FirebaseDatabase.getInstance().getReference()
    private lateinit var auth: FirebaseAuth

    private lateinit var editText_username: EditText
    private lateinit var editText_email: EditText
    private lateinit var editText_password: EditText
    private lateinit var editText_phone: EditText

    private lateinit var username: String
    private lateinit var email: String
    private lateinit var phone: String
    private lateinit var password: String

    private lateinit var btnRegisterUser: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_user)
        /*
            Initialize the Firebase package inside the lifecycle of the app
            The FirebaseAuth is initialize as a part of the packge of the
            Firebase
         */
        initializeApp(this)
        auth = FirebaseAuth.getInstance()

        editText_username = findViewById(R.id.edit_text_register_username)
        editText_email = findViewById(R.id.edit_text_register_email)
        editText_phone = findViewById(R.id.edit_text_register_phone)
        editText_password =findViewById(R.id.edit_text_register_password)

        btnRegisterUser = findViewById(R.id.btn_register_user)

        btnRegisterUser.setOnClickListener({
            username = editText_username.toString()
            email = editText_email.text.toString()
            phone = editText_phone.text.toString()
            password = editText_password.text.toString()

            if((username.isNotEmpty() || username.isNotBlank()) &&
                (email.isNotEmpty() || email.isNotBlank()) &&
                (phone.isNotEmpty() || phone.isNotBlank()) &&
                (password.isNotEmpty()) || password.isNotEmpty())
            {
                registerUser(username, email, phone, password)
            }
            else
            { Toast.makeText(this, "Insert Data to Register User",Toast.LENGTH_LONG).show() }
        })
    }

    fun registerUser(username:String, email:String, phone:String, password:String)
    {
        val userApp = UserApp()
        val databaseReferenceInstance = databaseInstance.ref
        var databaseReference = databaseReferenceInstance.child("user")

        /* Select the database instance child user */
        databaseReference = databaseReferenceInstance.database.getReference()

        /* Setting data into ServiceApp class */
        userApp.setUserName(username)
        userApp.setEmail(email)
        userApp.setPhone(phone)
        userApp.setPassword(password)

        /* Transfering data into Firebase object reference */
        databaseReference.push().key?.let { userApp.setId(it) }

        /* Data transfered from object to Firebase database */
        userApp.getId()?.let {
            databaseReference.child("user-collection").child(it).setValue(userApp)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful)
                    {
                        //Registering User into FirebaseAuth to create credentials to login
                        registerUserAuth(email, password)
                        Log.d("firebase database", java.lang.String.valueOf(task.result))
                        Toast.makeText(this, "User Successfully Registered!", Toast.LENGTH_LONG).show()
                    }
                    else
                    {
                        Log.e("firebase database", "[User] Error saving data", task.exception)
                        Toast.makeText(this, "Error Trying to Register User", Toast.LENGTH_LONG).show()}
                }
        }
        val activity = Intent(this@RegisterUserActivity, LoginActivity::class.java)
        startActivity(activity)
    }

    fun registerUserAuth(email:String, password: String)
    {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful)
                {
                    Log.d("firebase auth", "[user] User Registered FirebaseAuth")
                } else {
                    // Registration failed
                    Log.w("firebase auth", "createUserWithEmail:failure", task.exception)
                }
            }
    }
}