package com.example.hotcue

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.example.hotcue.ui.home.HomeFragment
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth

    override fun onStart() {
        super.onStart()
        auth = FirebaseAuth.getInstance() // Initialize FirebaseAuth instance
        val currentUser = auth.currentUser
        if (currentUser != null) {
            currentUser.reload().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    if (currentUser.isEmailVerified) {
                        val intent = Intent(applicationContext, drawerActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this, "Por favor, verifique seu email para continuar", Toast.LENGTH_SHORT).show()
                        auth.signOut() // Sign out the user if email is not verified
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_login)

        val editTextEmail = findViewById<EditText>(R.id.ed_email)
        val editTextPassword = findViewById<EditText>(R.id.ed_password)
        val buttonLogin = findViewById<Button>(R.id.btn_login)
        auth = FirebaseAuth.getInstance()
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        val textView = findViewById<TextView>(R.id.registerClique)

        textView.setOnClickListener {
            val intent = Intent(applicationContext, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }

        buttonLogin.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()

            if (email.isEmpty()) {
                Toast.makeText(this, "introduza um email", Toast.LENGTH_SHORT).show()
                progressBar.visibility = View.GONE
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                Toast.makeText(this, "introduza uma password", Toast.LENGTH_SHORT).show()
                progressBar.visibility = View.GONE
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    progressBar.visibility = View.GONE
                    if (task.isSuccessful) {
                        val currentUser = auth.currentUser
                        currentUser?.reload()?.addOnCompleteListener { reloadTask ->
                            if (reloadTask.isSuccessful && currentUser.isEmailVerified) {
                                Toast.makeText(baseContext, "Login efetuado com sucesso", Toast.LENGTH_SHORT).show()
                                val intent = Intent(applicationContext, drawerActivity::class.java)
                                startActivity(intent)
                                finish()
                            } else {
                                Toast.makeText(baseContext, "Por favor, verifique seu email para continuar", Toast.LENGTH_SHORT).show()
                                auth.signOut()
                            }
                        }
                    } else {
                        Toast.makeText(baseContext, "Falha na autenticação", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}