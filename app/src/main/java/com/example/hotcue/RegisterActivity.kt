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

class RegisterActivity : AppCompatActivity() {

    lateinit var auth:FirebaseAuth

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val intent = Intent(applicationContext, HomeFragment::class.java)
            startActivity(intent)
            finish()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_register)

        val editTextEmail = findViewById<EditText>(R.id.ed_email2)
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
            val password = editTextPassword.text.toString()

            if (email.isEmpty()) {
                Toast.makeText(this, "introduza um email", Toast.LENGTH_SHORT).show()
            }
            if (password.isEmpty()) {
                Toast.makeText(this, "introduza uma password", Toast.LENGTH_SHORT).show()
            }

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    progressBar.visibility = View.GONE
                    if (task.isSuccessful) {

                        auth.currentUser?.sendEmailVerification()?.addOnCompleteListener(this@RegisterActivity) { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(baseContext, "User registado, por favor verifique o seu email para dar login", Toast.LENGTH_SHORT,).show()
                            }
                            else{
                                Toast.makeText(baseContext, "Falha no registo", Toast.LENGTH_SHORT,).show()
                            }
                        }
                    } else {
                        Toast.makeText(baseContext, "Falha no registo", Toast.LENGTH_SHORT,).show()
                    }
                }




        }
    }
}