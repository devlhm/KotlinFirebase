package com.example.kotlinfirebase

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var googleClient : GoogleSignInClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = Firebase.auth

        val signUpText: TextView = findViewById(R.id.signUpText)

        signUpText.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("643120707972-tadq0huona0tppjceh5pr85jeba9rpmf.apps.googleusercontent.com")
            .requestEmail()
            .build()

        googleClient = GoogleSignIn.getClient(this, gso)

        val btnLogin: Button = findViewById(R.id.btnLogin)
        btnLogin.setOnClickListener {
            loginUser()
        }
        val btnGoogleLogin : Button = findViewById(R.id.btnGoogle)
        btnGoogleLogin.setOnClickListener {
            val intent = googleClient.signInIntent
            openActivity.launch(intent)
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

    private fun loginWithGoogle(token: String) {
        val credential = GoogleAuthProvider.getCredential(token, null)
        auth.signInWithCredential(credential).addOnCompleteListener(this) {
            task : Task<AuthResult> ->
            if(task.isSuccessful) {
                Toast.makeText(baseContext, "Login concluído!", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(baseContext, "Erro no login!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    var openActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        result : ActivityResult ->

        if(result.resultCode == RESULT_OK) {
            val intent = result.data
            val task = GoogleSignIn.getSignedInAccountFromIntent(intent)
            try {
                val conta = task.getResult(ApiException::class.java)
                loginWithGoogle(conta.idToken!!)
            } catch (exception: ApiException) {}

        }
    }
}