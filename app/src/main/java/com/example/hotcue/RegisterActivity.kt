package com.example.hotcue

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.example.hotcue.ui.home.HomeFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()

    override fun onStart() {
        super.onStart()
        auth = FirebaseAuth.getInstance() // Initialize FirebaseAuth instance
        val currentUser = auth.currentUser
        if (currentUser != null) {
            currentUser.reload().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    if (currentUser.isEmailVerified) {
                        val intent = Intent(applicationContext, HomeFragment::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this, "Por favor, verifique seu email para continuar", Toast.LENGTH_SHORT).show()
                        auth.signOut() // Sign out the user
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_register)

        val editTextEmail = findViewById<EditText>(R.id.ed_email2)
        val editTextUsername = findViewById<EditText>(R.id.ed_user)
        val editTextPassword = findViewById<EditText>(R.id.ed_password2)
        val buttonRegister = findViewById<Button>(R.id.btn_register)
        auth = FirebaseAuth.getInstance()
        val progressBar = findViewById<ProgressBar>(R.id.progressBar2)
        val textView = findViewById<TextView>(R.id.loginClique)

        textView.setOnClickListener {
            val intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        buttonRegister.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            val email = editTextEmail.text.toString()
            val user = editTextUsername.text.toString()
            val password = editTextPassword.text.toString()

            // Check if any of the fields are empty
            if (email.isEmpty() || user.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show()
                progressBar.visibility = View.GONE
                return@setOnClickListener
            }

            // Create user with email and password
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    progressBar.visibility = View.GONE
                    if (task.isSuccessful) {
                        // Save user details to Firestore
                        saveUserToFirestore(user)

                        // Send email verification
                        auth.currentUser?.sendEmailVerification()?.addOnCompleteListener(this@RegisterActivity) { emailTask ->
                            if (emailTask.isSuccessful) {
                                Toast.makeText(baseContext, "Usuário registrado com sucesso. Por favor, verifique o seu email para fazer login.", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(baseContext, "Falha ao enviar o email de verificação", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        Toast.makeText(baseContext, "Falha no registro. Verifique os detalhes e tente novamente.", Toast.LENGTH_SHORT).show()
                    }
                }

        }

    }

    private fun saveUserToFirestore(username: String) {
        val user = auth.currentUser
        user?.let {
            val userEmail = user.email
            val userMap = hashMapOf(
                "username" to username
            )
            if (userEmail != null) {
                db.collection("utilizadores")
                    .document(userEmail)
                    .set(userMap)
                    .addOnSuccessListener {
                        Log.d("RegisterActivity", "Username saved successfully under $userEmail in Firestore")
                    }
                    .addOnFailureListener { e ->
                        Log.e("RegisterActivity", "Error saving username under $userEmail in Firestore", e)
                    }
            }
        }
    }
}
