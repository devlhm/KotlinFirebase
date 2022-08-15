package com.example.kotlinfirebase

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = Firebase.auth

        val signUpText: TextView = findViewById(R.id.signUpText)

        signUpText.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        val btnLogin: Button = findViewById(R.id.btnLogin)
        btnLogin.setOnClickListener {
            loginUser()
        }
    }

    private fun loginUser() {
        val emailField : EditText = findViewById(R.id.loginEmail)
        val passwordField : EditText = findViewById(R.id.loginSenha)

        if(passwordField.text.isEmpty() || emailField.text.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
            return
        }

        val emailText = emailField.text.toString()
        val passwordText = passwordField.text.toString()

        auth.signInWithEmailAndPassword(emailText, passwordText)
            .addOnCompleteListener(this) {task ->
                if(task.isSuccessful) {
                    Toast.makeText(baseContext, "Login concluído!", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(baseContext, "Erro no login!", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Erro: ${it.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
    }
}