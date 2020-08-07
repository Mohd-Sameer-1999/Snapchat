package com.example.snapchat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    var emailEditText: EditText ?= null
    var passwordEditText: EditText ?= null
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.title = "Login"

        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)

        // Initialize Firebase Auth
        auth = Firebase.auth

        if(auth.currentUser != null){
            logIn()
        }

    }

    fun goClicked(view: View){

        auth.signInWithEmailAndPassword(emailEditText?.text.toString(), passwordEditText?.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    logIn()

                } else {
                    auth.createUserWithEmailAndPassword(emailEditText?.text.toString(), passwordEditText?.text.toString()).addOnCompleteListener(this) {  task ->
                        if (task.isSuccessful){

                            FirebaseDatabase.getInstance().getReference().child("users").child(task.result?.user?.uid!!).child("email").setValue(emailEditText?.text.toString())
                            logIn()
                        }
                        else{
                            Toast.makeText(this,"login failed. Try again.", Toast.LENGTH_LONG)
                        }
                    }

                }

            }

    }

    fun logIn(){
        // move to next activity
        val intent = Intent(this, SnapsActivity::class.java)
        startActivity(intent)

    }


}