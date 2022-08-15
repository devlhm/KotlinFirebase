package com.example.kotlinfirebase

import android.content.Intent
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_register)
        auth = Firebase.auth

        val btnCadastrar: Button = findViewById(R.id.btnCadastrar)

        btnCadastrar.setOnClickListener {
            registerUser()
        }

    }

    private fun registerUser() {
        val emailField : EditText = findViewById(R.id.registerEmail)
        val passwordField : EditText = findViewById(R.id.registerSenha)

        if(passwordField.text.isEmpty() || emailField.text.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
            return
        }

        val emailText = emailField.text.toString()
        val passwordText = passwordField.text.toString()

        auth.createUserWithEmailAndPassword(emailText, passwordText)
            .addOnCompleteListener(this) { task ->
                if(task.isSuccessful) {
                    Toast.makeText(baseContext, "Registro concluído!", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(baseContext, "Falha no registro!", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Erro: ${it.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
    }
}

